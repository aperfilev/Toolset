package local.tools.serial.primitives;

/**
 * Type represents 8 bits integer type
 * Alias: Byte, SByte (Signed Byte)
 * Size: 8 bits (1 byte)
 * Range: -128 to 127
 */
public class Int8 extends Number {

    public static final byte MIN_VALUE = Byte.MIN_VALUE;
    public static final byte MAX_VALUE = Byte.MAX_VALUE;
    
    private final byte value;
    
    public static byte[] getBytes(byte value, byte[] output) {
        output[0] = value;
        return output;
    }
    
    public byte[] getBytes() {
        return new byte[] { value };
    }
    
    public Int8(int value) {
        this((byte)value);
    }

    public Int8(byte value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Int8 other = (Int8) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    private boolean withinRange(int value) {
        return MIN_VALUE <= value && value <= MAX_VALUE;
    }
}
