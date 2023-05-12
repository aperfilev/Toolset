package local.tools.serial.primitives;

/**
 * Type represents 16 bits (2 byte) unsigned integer type
 * Alias: UShort
 * Size: 16 bits (2 bytes)
 * Range: 0 to 65,535
 */
public class UInt16 extends Number {

    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 65535;
    
    private final int value;
    
    public UInt16(int value) {
        if (!withinRange(value)) throw new IllegalArgumentException(String.format("Value '%d' is out of 2-Byte Unsigned Integer range.", value));
        this.value = value;
    }

    public UInt16(int a, int b) {
        this((byte)a, (byte)b);
    }
    
    public UInt16(byte a, byte b) {
        this.value = Short.toUnsignedInt(Int16.packBytes(a, b));
    }
    
    public UInt16(byte[] data) {
        this(data[0], data[1]);
    }
    
    public static final byte[] getBytes(int value, byte[] output) {
        output[0] = (byte) (value >>> 8);
        output[1] = (byte) (value);
        return output;
    }
    
    public byte[] getBytes() {
        return new byte[] {(byte)(value >>> 8), (byte)(value)};
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
        hash = 37 * hash + this.value;
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
        final UInt16 other = (UInt16) obj;
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
