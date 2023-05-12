package local.tools.serial.json.discovery;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class JSONArray implements List<Object>, Iterable<Object> {

    private final List<Object> items = new ArrayList<>();
    
    private boolean disabledIndents = false;

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public JSONArray() {
    }
    
    public JSONArray(String source) throws JSONException {
        this(new JSONTokener(source));
    }
    
    public JSONArray(Collection<Object> collection) {
        for (Object item : collection) {
            this.items.add(JSONObject.wrap(item));
        }
    }
    
    public JSONArray(Object[] array) throws JSONException {
        for (int i = 0; i < array.length; ++i) {
            this.items.add(JSONObject.wrap(array[i]));
        }
    }
    
    public JSONArray(Object array) throws JSONException {
        if (array.getClass().isArray()) {
            int length = Array.getLength(array);
            for (int i = 0; i < length; ++i) {
                this.items.add(JSONObject.wrap(Array.get(array, i)));
            }
        } else {
            throw new JSONException("JSONArray initial value should be a string or collection or array.");
        }
    }
    
    JSONArray(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        if (x.nextClean() != ']') {
            x.back();
            while(true) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.items.add(null);
                } else {
                    x.back();
                    this.items.add(x.nextValue());
                }
                switch (x.nextClean()) {
                    case ';':
                    case ',':
                        if (x.nextClean() == ']') {
                            return;
                        }
                        x.back();
                        break;
                    case ']':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters">
    public void setDisabledIndents(boolean disabledIndents) {
        this.disabledIndents = disabledIndents;
    }
    
    @Override
    public Object get(int index) {
        return this.items.get(index);
    }
    
    public boolean getBoolean(int index) throws JSONException {
        Object object = this.get(index);
        if (object.equals(Boolean.FALSE) ||
            (object instanceof String && ((String)object).equalsIgnoreCase("false"))) {
            return false;
        } else if (object.equals(Boolean.TRUE) ||
            (object instanceof String && ((String)object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException("JSONArray[" + index + "] is not a boolean.");
    }
    
    public double getDouble(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return object instanceof Number ? 
                    ((Number)object).doubleValue() : 
                    Double.parseDouble((String)object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }
    
    public int getInt(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return object instanceof Number ? 
                    ((Number)object).intValue() : 
                    Integer.parseInt((String)object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }
    
    public JSONArray getJSONArray(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
    }
    
    public long getLong(int index) throws JSONException {
        Object object = this.get(index);
        try {
            return object instanceof Number ? 
                    ((Number)object).longValue() : 
                    Long.parseLong((String)object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }
    
    public String getString(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof String) {
            return (String)object;
        }
        throw new JSONException("JSONArray[" + index + "] not a string.");
    }
    //</editor-fold>

    /**
     * Append an object value. This increases the array's length by one.
     * @param value An object value.  The value should be a
     * Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or null.
     * @return this.
     */
    public JSONArray put(Object value) {
        this.items.add(JSONObject.wrap(value));
        return this;
    }

    //<editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return '[' + this.join(",") + ']';
    }
    
    public String toString(int indent) throws JSONException {
        if (disabledIndents) indent = 0;
        StringBuilder sb = new StringBuilder();
        return this.write(sb, indent, 0).toString();
    }
    
    public String join(String separator) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); ++i) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(valueToString(this.items.get(i)));
        }
        return sb.toString();
    }
    
    /**
     * Make a JSON text of an Object value. If the object containsKey an
     * value.toJSONString() method, then that method will be used to produce
     * the JSON text. The method is required to produce a strictly
     * conforming text. If the object does not contain a toJSONString
     * method (which is the most common case), then a text will be
     * produced by other means. If the value is an array or Collection,
     * then a JSONArray will be made from it and its toJSONString method
     * will be called. If the value is a MAP, then a JSONObject will be made
     * from it and its toJSONString method will be called. Otherwise, the
     * value's toString method will be called, and the result will be quoted.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param value The value to be serialized.
     * @return a printable, displayable, transmittable
     * representation of the object, beginning
     * with <code>{</code>&nbsp;<small>(left brace)</small> and ending
     * with <code>}</code>&nbsp;<small>(right brace)</small>.
     * @throws JSONException If the value is or contains an invalid number.
     */
    static String valueToString(Object value) throws JSONException {
        if (value == null) return "null";
        
        if (value instanceof Double) {
            return JSONObject.decimalFormat.format((double) value);
        }
        if (value instanceof Float) {
            return JSONObject.decimalFormat.format((float) value);
        }
        if (value instanceof Boolean ||
            value instanceof Number || 
            value instanceof JSONObject || 
            value instanceof JSONArray) 
        {
            return value.toString();
        }
        return JSONObject.quote(String.valueOf(value));
    }
    
    /**
     * Write the contents of the JSONArray as JSON text to a writer. For
     * compactness, no whitespace is added.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param indent
     *            The number of spaces to add to each level of indentation.
     * @param totalIndent
     *            The indention of the top level.
     * @return The writer.
     * @throws JSONException
     */
    StringBuilder write(final StringBuilder writer, int indent, int totalIndent) throws JSONException {
        if (disabledIndents) { 
            indent = 0; 
            totalIndent = 0;
        }
        try {
            boolean commanate = false;
            int length = this.items.size();
            writer.append('[');
            
            if (length == 1) {
                JSONObject.writeValue(writer, this.items.get(0), indent, totalIndent);
            } else if (length != 0) {
                final int newindent = totalIndent + indent;
                
                for (int i = 0; i < length; ++i) {
                    if (commanate) {
                        writer.append(',');
                    }
                    if (indent > 0) {
                        writer.append('\n');
                    }
                    JSONObject.appendIndent(writer, newindent);
                    JSONObject.writeValue(writer, this.items.get(i), indent, newindent);
                    commanate = true;
                }
                if (indent > 0) {
                    writer.append('\n');
                }
                JSONObject.appendIndent(writer, totalIndent);
            }
            writer.append(']');
            return writer;
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Iterable interface methods">
    @Override
    public Iterator<Object> iterator() {
        return items.iterator();
    }
    //</editor-fold>

    //<editor-fold desc="List interface methods">
    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return items.contains(o);
    }

    @Override
    public Object[] toArray() {
        return items.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) items.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        return items.add(JSONObject.wrap(o));
    }

    @Override
    public boolean remove(Object o) {
        return items.remove(o);
    }
    
    @Override
    public Object remove(int index) {
        return this.items.remove(index);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return items.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return items.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        return items.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return items.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return items.retainAll(c);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public Object set(int index, Object element) {
        return items.set(index, JSONObject.wrap(element));
    }

    @Override
    public void add(int index, Object element) {
        items.add(index, JSONObject.wrap(element));
    }

    @Override
    public int indexOf(Object o) {
        return items.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return items.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return items.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return items.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return items.subList(fromIndex, toIndex);
    }
    //</editor-fold>
}
