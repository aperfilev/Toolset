package local.tools.serial.bjson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import local.tools.serial.json.discovery.JSONArray;
import local.tools.serial.json.discovery.JSONObject;
import local.tools.serial.primitives.UInt16;
import local.tools.serial.primitives.UInt32;
import local.tools.serial.primitives.UInt64;
import local.tools.serial.primitives.UInt8;

/**
 * Binary JSON Array object.
 */
public class BJSONArray implements BJSONNode, List<Object>, Iterable<Object> {
    
    private final List<Object> items = new ArrayList<>();
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public BJSONArray() {
    }
    
    public BJSONArray(List<Object> array) {
        for (int i=0; i<array.size(); ++i) {
            put(array.get(i));
        }
    }
    //</editor-fold>

    @Override
    public BJSONType getType() {
        return BJSONType.BJSONArray;
    }

    @Override
    public int getSize() {
        return items.size();
    }
    
    public final BJSONArray put(final Object value) throws BJSONException {
        if (value == null) {
            put((BJSONNode)BJSONNullNode.INSTANCE);
            return this;
        } else if (value instanceof BJSONNode) {
            put((BJSONNode) value);
            return this;
        } else if (value instanceof String) {
            put((String) value);
            return this;
        } else if (value instanceof Byte) {
            put((byte) value);
            return this;
        } else if (value instanceof Short) {
            put((short) value);
            return this;
        } else if (value instanceof Integer) {
            put((int) value);
            return this;
        } else if (value instanceof Long) {
            put((long) value);
            return this;
        } else if (value instanceof UInt8) {
            put((UInt8) value);
            return this;
        } else if (value instanceof UInt16) {
            put((UInt16) value);
            return this;
        } else if (value instanceof UInt32) {
            put((UInt32) value);
            return this;
        } else if (value instanceof UInt64) {
            put((UInt64) value);
            return this;
        } else if (value instanceof Float) {
            put((float) value);
            return this;
        } else if (value instanceof Double) {
            put((double) value);
            return this;
        } else if (value instanceof JSONObject) {
            put(new BJSONObject((JSONObject)value));
            return this;
        } else if (value instanceof JSONArray) {
            put(new BJSONArray((JSONArray)value));
            return this;
        }
        throw new BJSONException("Unsupported type exception");
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters">   
    @Override
    public Object get(int index) {
        Object value = this.items.get(index);
        if (value instanceof BJSONNullNode) return null;
        return value;
    }
    
    public BJSONObject getJSONObject(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof BJSONObject) {
            return (BJSONObject) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public BJSONArray getBJSONArray(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof BJSONArray) {
            return (BJSONArray) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public String getString(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof String) {
            return (String) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Boolean getBoolean(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof Boolean) {
            return (Boolean) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Byte getByte(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof Byte) {
            return (Byte) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Short getShort(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof Short) {
            return (Short) node;
        }
        throw new BJSONException("Type mismatch exception.");
    } 
    
    public Integer getInt(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof Integer) {
            return (Integer) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Long getLong(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof Long) {
            return (Long) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Float getFloat(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof Float) {
            return (Float) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public Double getDouble(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof Double) {
            return (Double) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Unsigned Type Getters">   
    public UInt8 getUInt8(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof UInt8) {
            return (UInt8) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public UInt16 getUInt16(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof UInt16) {
            return (UInt16) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public UInt32 getUInt32(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof UInt32) {
            return (UInt32) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    
    public UInt64 getUInt64(int index) throws BJSONException {
        Object node = get(index);
        if (node == null) return null;
        if (node instanceof UInt64) {
            return (UInt64) node;
        }
        throw new BJSONException("Type mismatch exception.");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Basic Java Types Putters">
    public final BJSONArray put(final BJSONNode value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final String value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final byte value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final short value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final int value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final long value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final float value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final double value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Unsigned Types Putters">    
    public final BJSONArray put(final UInt8 value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final UInt16 value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final UInt32 value) throws BJSONException {
        this.items.add(value);
        return this;
    }
    
    public final BJSONArray put(final UInt64 value) throws BJSONException {
        this.items.add(value);
        return this;
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
    public Object[] toArray(Object[] a) {
        return items.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        return items.add(o);
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
    public boolean containsAll(Collection c) {
        return items.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        return items.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return items.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection c) {
        return items.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        return items.retainAll(c);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public Object set(int index, Object element) {
        return items.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        items.add(index, element);
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
