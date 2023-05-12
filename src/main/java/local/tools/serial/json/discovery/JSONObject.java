package local.tools.serial.json.discovery;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import local.tools.logs.Logger;

public final class JSONObject implements Map<String, Object>, Serializable {

    // Reduces scientific notation
    // Reduces trailing zeros from double and floats
    static final DecimalFormat decimalFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.US));
    static {
        decimalFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
    }

    private final Map<String, Object> attributes = new LinkedHashMap<>();
    
    private boolean disabledIndents = false;

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public JSONObject() {
    }
       
    /**
     * Construct a JSONObject from a JSONParser.
     *
     * @param x A JSONParser object containing the source string.
     * @throws JSONException If there is a syntax error in the source string
     *                       or a duplicated key.
     */
    JSONObject(JSONTokener x) throws JSONException {
        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        String key;
        while (true) {
            char c = x.nextClean();
            switch (c) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    x.back();
                    key = x.nextValue().toString();
            }
            
            /* The key is followed by ':'. We will also tolerate '=' or '=>'. */
            c = x.nextClean();
            if (c == '=') {
                if (x.next() != '>') {
                    x.back();
                }
            } else if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            this.put(key, x.nextValue());
            
            /* Pairs are separated by ','. We will also tolerate ';'. */
            switch (x.nextClean()) {
                case ';':
                case ',':
                    if (x.nextClean() == '}') {
                        return;
                    }
                    x.back();
                    break;
                case '}':
                    return;
                default:
                    throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }
    
    /**
     * Construct a JSONObject from a Map.
     *
     * @param map A map object that can be used to initialize the contents of
     *            the JSONObject.
     * @throws JSONException
     */
    public JSONObject(Map<String, Object> map) {
        this.attributes.putAll(map);
    }
    
    /**
     * Construct a JSONObject from an Object using bean getters.
     * It reflects on all of the public methods of the object.
     * For each of the methods with no parameters and a name starting
     * with <code>"get"</code> or <code>"is"</code> followed by an uppercase letter,
 the method is invoked, and a key and the value returned from the getter method
 are putInternal into the new JSONObject.
 <p>
     * The key is formed by removing the <code>"get"</code> or <code>"is"</code> prefix.
     * If the second remaining character is not upper case, then the first
     * character is converted to lower case.
     * <p>
     * For example, if an object containsKey a method named <code>"getName"</code>, and
     * if the result of calling <code>object.getName()</code> is <code>"Larry Fine"</code>,
     * then the JSONObject will contain <code>"name": "Larry Fine"</code>.
     *
     * @param bean An object that containsKey getter methods that should be used
             to make a JSONObject.
     */
    public JSONObject(Object bean) {
        this(extractAllFieldsIntoMap(bean));
    }
    
    /**
     * Construct a JSONObject from a source JSON text string.
     * This is the most commonly used JSONObject constructor.
     *
     * @param source A string beginning
     *               with <code>{</code>&nbsp;<small>(left brace)</small> and ending
     *               with <code>}</code>&nbsp;<small>(right brace)</small>.
     * @throws JSONException If there is a syntax error in the source
     *                       string or a duplicated key.
     */
    public JSONObject(String source) throws JSONException {
        this(new JSONTokener(source));
    }
    
    //<editor-fold defaultstate="collapsed" desc="Copy From ReflexHelper">
    public static final Map<String, Object> extractAllFieldsIntoMap(Object obj) {
        return extractAllFieldsIntoMap(obj, true);
    }
    
    public static final Map<String, Object> extractAllFieldsIntoMap(Object obj, boolean withPrivates) {
        Map<String, Object> result = new LinkedHashMap<>();
        Field[] fields;
        if (withPrivates) {
            fields = obj.getClass().getDeclaredFields();
        } else {
            fields = obj.getClass().getFields();
        }
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                result.put(key, value);
            } catch (IllegalAccessException ignored) {
                Logger.debug(ignored.getMessage());
            }
        }
        return result;
    }
    //</editor-fold>
    //</editor-fold>

    public void setDisabledIndents(boolean disabledIndents) {
        this.disabledIndents = disabledIndents;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * Get the value object associated with a key.
     *
     * @param key A key string.
     * @return The object associated with the key.
     * @throws JSONException if the key is not found.
     */ 
    public Object get(String key) {
        return this.attributes.get(key);
    }
    
    public <Type extends Number> Type getNumber(String key) {
        Object o = this.attributes.get(key);
        try {
            return (Type) o;
        } catch (ClassCastException e) { 
            throw new JSONException("JSONObject[\"" + key + "\"] is not of expected type.");
        }
    }
    
    /**
     * Get the boolean value associated with a key.
     *
     * @param key A key string.
     * @return The truth.
     * @throws JSONException if the value is not a Boolean or the String "true" or "false".
     */
    public boolean getBoolean(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(object));
        } catch (JSONException e) {
            throw new JSONException("JSONObject[\"" + key + "\"] is not a Boolean.");
        }
    }
    
    /**
     * Get the double value associated with a key.
     *
     * @param key A key string.
     * @return The numeric value.
     * @throws JSONException if the key is not found or
     *                       if the value is not a Number object and cannot be converted to a number.
     */
    public double getDouble(String key) throws JSONException {
        Object object = this.get(key);
        try {
            if (object instanceof Number) { 
                return ((Number) object).doubleValue();
            }
            return Double.parseDouble((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONObject[\"" + key + "\"] is not a number.");
        }
    }
    
    /**
     * Get the int value associated with a key.
     *
     * @param key A key string.
     * @return The integer value.
     * @throws JSONException if the key is not found or if the value cannot
     *                       be converted to an integer.
     */
    public int getInt(String key) throws JSONException {
        Object object = this.get(key);
        try {
            if (object instanceof Number) {
                return ((Number) object).intValue();
            }
            return Integer.parseInt((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONObject[\"" + key + "\"] is not an int.");
        }
    }
    
    /**
     * Get the JSONArray value associated with a key.
     *
     * @param key A key string.
     * @return A JSONArray which is the value.
     * @throws JSONException if the key is not found or
     *                       if the value is not a JSONArray.
     */
    public JSONArray getJSONArray(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        throw new JSONException("JSONObject[\"" + key + "\"] is not a JSONArray.");
    }
    
    /**
     * Get the JSONObject value associated with a key.
     *
     * @param key A key string.
     * @return A JSONObject which is the value.
     * @throws JSONException if the key is not found or
     *                       if the value is not a JSONObject.
     */
    public JSONObject getJSONObject(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        throw new JSONException("JSONObject[\"" + key + "\"] is not a JSONObject.");
    }   
    
    /**
     * Get the long value associated with a key.
     *
     * @param key A key string.
     * @return The long value.
     * @throws JSONException if the key is not found or if the value cannot
     *                       be converted to a long.
     */
    public long getLong(String key) throws JSONException {
        Object object = this.get(key);
        try {
            if (object instanceof Number) {
                return ((Number) object).longValue();
            }
            return Long.parseLong((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONObject[\"" + key + "\"] is not a long.");
        }
    }
    
    /**
     * Get the string associated with a key.
     *
     * @param key A key string.
     * @return A string which is the value.
     * @throws JSONException if there is no string value for the key.
     */
    public String getString(String key) throws JSONException {
        return String.valueOf(get(key));
    }
    //</editor-fold>

    /**
     * Determine if the JSONObject contains a specific key.
     *
     * @param key A key string.
     * @return true if the key exists in the JSONObject.
     */
    public final boolean containsKey(final String key) {
        return this.attributes.containsKey(key);
    }

    /**
     * Get an enumeration of the keys of the JSONObject.
     *
     * @return An iterator of the keys.
     */
    public final Iterator<String> keys() {
        return this.attributes.keySet().iterator();
    }

    @Override
    public final JSONObject put(final String key, final Object value) throws JSONException {
        this.attributes.put(key, wrap(value));
        return this;
    }
    
    /**
     * Wrap an object, if necessary. If the object is null, return the NULL
     * object. If it is an array or collection, wrap it in a JSONArray. If
     * it is a map, wrap it in a JSONObject. If it is a standard property
     * (Double, String, et al) then it is already wrapped. Otherwise, if it
     * comes from one of the java packages, turn it into a string. And if
     * it doesn't, try to wrap it in a JSONObject. If the wrapping fails,
     * then null is returned.
     *
     * @param value The object to wrap
     * @return The wrapped value
     */
    final static Object wrap(final Object value) {
        if (value == null) return null;
        if (value instanceof JSONObject || 
            value instanceof JSONArray ||
            value instanceof Boolean ||
            value instanceof Byte || 
            value instanceof Character ||
            value instanceof Short || 
            value instanceof Integer ||
            value instanceof Long || 
            value instanceof Float || 
            value instanceof Double ||
            value instanceof String || 
            value instanceof Enum) 
        {
            return value;
        }

        if (value instanceof Collection) {
            return new JSONArray((Collection) value);
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value);
        }
        if (value instanceof Map) {
            return new JSONObject((Map) value);
        }

        //<editor-fold defaultstate="collapsed" desc="Cut off reflection fill for java native classes">
        Package objectPackage = value.getClass().getPackage();
        String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
        if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || value.getClass().getClassLoader() == null) {
            return value.toString();
        }
        //</editor-fold>

        return new JSONObject(value);
    }

    /**
     * Remove a name and its value, if present.
     *
     * @param key The name to be removed.
     * @return The value that was associated with the name,
     * or null if there was no value.
     */
    public final Object remove(final String key) {
        return this.attributes.remove(key);
    }

    //<editor-fold defaultstate="collapsed" desc="toString methods">
    /**
     * Make a JSON text of this JSONObject. For compactness, no whitespace
     * is added. If this would not result in a syntactically correct JSON text,
     * then null will be returned instead.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, portable, transmittable
     * representation of the object, beginning
     * with <code>{</code>&nbsp;<small>(left brace)</small> and ending
     * with <code>}</code>&nbsp;<small>(right brace)</small>.
     */
    @Override
    public String toString() {
        return this.toString(0);
    }
    
    /**
     * Make a prettyprinted JSON text of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of
     *                     indentation.
     * @return a printable, displayable, portable, transmittable
     * representation of the object, beginning
     * with <code>{</code>&nbsp;<small>(left brace)</small> and ending
     * with <code>}</code>&nbsp;<small>(right brace)</small>.
     * @throws JSONException If the object contains an invalid number.
     */
    public String toString(int indentFactor) throws JSONException {
        if (disabledIndents) indentFactor = 0;
        StringBuilder w = new StringBuilder();
        return this.write(w, indentFactor, 0).toString();
    }
    
    /**
     * Write the contents of the JSONObject as JSON text to a writer. For
     * compactness, no whitespace is added.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return The writer.
     * @throws JSONException
     */
    final StringBuilder write(final StringBuilder writer, int indentFactor, int indent) throws JSONException {
        if (disabledIndents) {
            indentFactor = 0;
            indent = 0;
        }
        try {
            boolean commanate = false;
            final int size = this.size();
            Iterator<String> keys = this.keys();
            writer.append('{');
            
            if (size == 1) {
                String key = keys.next();
                writer.append("\"");
                writer.append(key);
                writer.append("\"");
                writer.append(':');
                if (indentFactor > 0) {
                    writer.append(' ');
                }
                writeValue(writer, this.attributes.get(key), indentFactor, indent);
            } else if (size != 0) {
                final int newIndent = indent + indentFactor;
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (commanate) {
                        writer.append(',');
                    }
                    if (indentFactor > 0) {
                        writer.append('\n');
                    }
                    appendIndent(writer, newIndent);
                    writer.append("\"");
                    writer.append(key);
                    writer.append("\"");
                    writer.append(':');
                    if (indentFactor > 0) {
                        writer.append(' ');
                    }
                    writeValue(writer, this.attributes.get(key), indentFactor, newIndent);
                    commanate = true;
                }
                if (indentFactor > 0) {
                    writer.append('\n');
                }
                appendIndent(writer, indent);
            }
            writer.append('}');
            return writer;
        } catch (IOException exception) {
            throw new JSONException(exception);
        }
    }
    
    final static StringBuilder writeValue(StringBuilder writer, Object value, int indentFactor, int indent)
            throws JSONException, IOException
    {
        if (value == null) {
            writer.append("null");
        } else if (value instanceof JSONObject) {
            ((JSONObject) value).write(writer, indentFactor, indent);
        } else if (value instanceof JSONArray) {
            ((JSONArray) value).write(writer, indentFactor, indent);
        } else if (value instanceof Double) {
            writer.append(decimalFormat.format((double) value));
        } else if (value instanceof Float) {
            writer.append(decimalFormat.format((float) value));
        } else if (value instanceof Number) {
            writer.append(String.valueOf(value));
        } else if (value instanceof Boolean) {
            writer.append(value.toString());
        } else {
            quote(String.valueOf(value), writer);
        }
        return writer;
    }
    
    /**
     * Produce a string in double quotes with backslash sequences in all the
     * right places. A backslash will be inserted within </, producing <\/,
     * allowing JSON text to be delivered in HTML. In JSON text, a string
     * cannot contain a control character or an unescaped quote or backslash.
     *
     * @param value A String
     * @return A String correctly formatted for insertion in a JSON text.
     */
    final static String quote(final String value) {
        StringBuilder writer = new StringBuilder();
        return quote(value, writer).toString();
    }

    private final static StringBuilder quote(final String value, final StringBuilder w) {
        if (value == null || value.isEmpty()) {
            w.append("\"\"");
            return w;
        }

        char c = 0;
        w.append('"');
        for (int i = 0; i < value.length(); ++i) {
            char b = c;
            c = value.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    w.append('\\');
                    w.append(c);
                    break;
                case '/':
                    if (b == '<') {
                        w.append('\\');
                    }
                    w.append(c);
                    break;
                case '\b':
                    w.append("\\b");
                    break;
                case '\t':
                    w.append("\\t");
                    break;
                case '\n':
                    w.append("\\n");
                    break;
                case '\f':
                    w.append("\\f");
                    break;
                case '\r':
                    w.append("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
                        String hhhh = "000" + Integer.toHexString(c);
                        w.append("\\u");
                        w.append(hhhh.substring(hhhh.length() - 4));
                    } else {
                        w.append(c);
                    }
            }
        }
        w.append('"');
        return w;
    }
    
    final static void appendIndent(final StringBuilder writer, final int size) throws IOException {
        for (int i = 0; i < size; ++i) {
            writer.append(' ');
        }
    }
    //</editor-fold>

    //<editor-fold desc="Map interface methods">
    @Override
    public final int size() {
        return attributes.size();
    }

    @Override
    public final boolean isEmpty() {
        return attributes.isEmpty();
    }

    @Override
    public final boolean containsKey(final Object key) {
        return attributes.containsKey(key);
    }

    @Override
    public final boolean containsValue(final Object value) {
        return attributes.containsValue(value);
    }

    @Override
    public final Object get(final Object key) {
        return attributes.get(key);
    }

    @Override
    public final Object remove(final Object key) {
        return attributes.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ?> m) {
        attributes.putAll(m);
    }

    @Override
    public void clear() {
        attributes.clear();
    }

    @Override
    public final Set<String> keySet() {
        return attributes.keySet();
    }

    @Override
    public final Collection<Object> values() {
        return attributes.values();
    }

    @Override
    public final Set<Entry<String, Object>> entrySet() {
        return attributes.entrySet();
    }
    //</editor-fold>
}