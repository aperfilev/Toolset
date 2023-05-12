package local.tools.serial.bjson;

/**
 * BJSON Type enumeration represents all supported types and their type ordinal value
 */
public enum BJSONType {
    NULL        (0, 0),
    BJSONObject (1),
    BJSONArray  (2),
    String      (3),
    Boolean     (4, 1),
    Int8        (5, 1),//Byte
    Int16       (6, 2),//Short
    Int32       (7, 4),//Int
    Int64       (8, 8),//Long
    UInt8       (9, 1),
    UInt16      (10, 2),
    UInt32      (11, 4),
    UInt64      (12, 8),
    Float32     (13, 4),
    Float64     (14, 8);
    
    private final byte value;
    private final int size;
    
    BJSONType(int value, int size) {
        this.value = (byte) value;
        this.size = size;
    }
    
    BJSONType(int value) {
        this(value, -1);
    }
    
    public static BJSONType fromValue(int value) {
        switch ((byte)value) {
            case 0: return NULL;
            case 1: return BJSONObject;
            case 2: return BJSONArray;
            case 3: return String;
            case 4: return Boolean;
            case 5: return Int8;
            case 6: return Int16;
            case 7: return Int32;
            case 8: return Int64;
            case 9: return UInt8;
            case 10: return UInt16;
            case 11: return UInt32;
            case 12: return UInt64;
            case 13: return Float32;
            case 14: return Float64;
            default: return null;
        }
    }

    public byte getValue() {
        return value;
    }

    public int getSize() {
        return size;
    }
}
