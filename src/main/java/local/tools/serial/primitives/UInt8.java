package local.tools.serial.primitives;

/**
 * Type represents 8 bits (1 byte) unsigned integer type
 * Alias: UByte, (Unsigned Byte)
 * Size: 8 bits (1 byte)
 * Range: 0 to 255
 */
public class UInt8 extends Number {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 255;
    
    private final int value;
    
    public UInt8(int value) {
        if (!withinRange(value)) throw new IllegalArgumentException(String.format("Value '%d' is out of 1-Byte Unsigned Integer range.", value));
        this.value = value;
    }
    
    public UInt8(byte b) {
        this.value = Byte.toUnsignedInt(b);
    }

    public static byte[] getBytes(int value, byte[] output) {
        output[0] = (byte) value;
        return output;
    }
    
    public byte[] getBytes() {
        return new byte[] { (byte) value };
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
        hash = 89 * hash + this.value;
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
        final UInt8 other = (UInt8) obj;
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
