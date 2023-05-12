package local.tools.serial.bjson;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import local.tools.serial.json.discovery.JSONArray;
import local.tools.serial.json.discovery.JSONObject;
import local.tools.serial.primitives.UInt16;
import local.tools.serial.primitives.UInt32;
import local.tools.serial.primitives.UInt64;
import local.tools.serial.primitives.UInt8;

/**
 * Binary JSON Object.
 */
public final class BJSONObject implements BJSONNode, Map<String, Object>, Serializable  {

    private final Map<String, Object> attributes = new LinkedHashMap<>();
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public BJSONObject() {
    }
    
    public BJSONObject(Map<String, Object> object) {
        for (Map.Entry<String, Object> entry : object.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    //</editor-fold>

    @Override
    public BJSONType getType() {
        return BJSONType.BJSONObject;
    }

    @Override
    public int getSize() {
        return attributes.size();
    }
    
    public Object get(String key) {
        Object value = this.attributes.get(key);
        if (value instanceof BJSONNullNode) return null;
        return value;
    }
    
    @Override
    public final BJSONObject put(final String key, final Object value) throws BJSONException {
        if (value == null) {
            put(key, (BJSONNode) BJSONNullNode.INSTANCE);
            return this;
        } else if (value instanceof BJSONNode) {
            put(key, (BJSONNode) value);
            return this;
        } else if (value instanceof String) {
            put(key, (String) value);
            return this;
        } else if (value instanceof Byte) {
            put(key, (byte) value);
            return this;
        } else if (value instanceof Short) {
            put(key, (short) value);
            return this;
        } else if (value instanceof Integer) {
            put(key, (int) value);
            return this;
        } else if (value instanceof Long) {
            put(key, (long) value);
            return this;
        } else if (value instanceof UInt8) {
            put(key, (UInt8) value);
            return this;
        } else if (value instanceof UInt16) {
            put(key, (UInt16) value);
            return this;
        } else if (value instanceof UInt32) {
            put(key, (UInt32) value);
            return this;
        } else if (value instanceof UInt64) {
            put(key, (UInt64) value);
            return this;
        } else if (value instanceof Float) {
            put(key, (float) value);
            return this;
        } else if (value instanceof Double) {
            put(key, (double) value);
            return this;
        } else if (value instanceof JSONObject) {
            put(key, new BJSONObject((JSONObject)value));
            return this;
        } else if (value instanceof JSONArray) {
            put(key, new BJSONArray((JSONArray)value));
            return this;
        }
        throw new BJSONException("Unsupported type exception: " + value.getClass().getName());
    }

    //<editor-fold defaultstate="collapsed" desc="Basic Java Types Getters">    
    public BJSONObject getJSONObject(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof BJSONObject) {
            return (BJSONObject) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public BJSONArray getBJSONArray(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof BJSONArray) {
            return (BJSONArray) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public String getString(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof String) {
            return (String) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Boolean getBoolean(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof Boolean) {
            return (Boolean) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Byte getByte(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof Byte) {
            return (Byte) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Short getShort(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof Short) {
            return (Short) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Integer getInt(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof Integer) {
            return (Integer) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Long getLong(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof Long) {
            return (Long) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Float getFloat(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof Float) {
            return (Float) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Double getDouble(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof Double) {
            return (Double) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Unsigned Type Getters">
    public UInt8 getUInt8(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof UInt8) {
            return (UInt8) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public UInt16 getUInt16(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof UInt16) {
            return (UInt16) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public UInt32 getUInt32(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof UInt32) {
            return (UInt32) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public UInt64 getUInt64(String key) throws BJSONException {
        Object node = get(key);
        if (node == null) return null;
        if (node instanceof UInt64) {
            return (UInt64) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Basic Java Types Putters">
    public final BJSONObject put(final String key, final BJSONNode value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final String value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final byte value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final short value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final int value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final long value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final float value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final double value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Primitives Putters">   
    public final BJSONObject put(final String key, final UInt8 value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final UInt16 value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final UInt32 value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    
    public final BJSONObject put(final String key, final UInt64 value) throws BJSONException {
        this.attributes.put(key, value);
        return this;
    }
    //</editor-fold>
    
    public final Iterator<String> keys() {
        return this.attributes.keySet().iterator();
    }
    
    public final Object remove(final String key) {
        return this.attributes.remove(key);
    }

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
    public void putAll(final Map<? extends String, ? extends Object> m) {
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
