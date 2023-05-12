package local.tools.serial.primitives;

/**
 * Type represents 64 bits (8 bytes) integer type
 * Alias: Long
 * Size: 64 bits (8 bytes)
 * Range: -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
 */
public class Int64 extends Number {

    public static final long MIN_VALUE = Long.MIN_VALUE;
    public static final long MAX_VALUE = Long.MAX_VALUE;
    
    private final long value;
    
    public Int64(long value) {
        this.value = value;
    }
    
    public Int64(int a, int b, int c, int d, int e, int f, int g, int h) {
        this((byte)a, (byte)b, (byte)c, (byte)d, (byte)e, (byte)f, (byte)g, (byte)h);
    }

    public Int64(byte a, byte b, byte c, byte d, byte e, byte f, byte g, byte h) {
        this.value = packBytes(a, b, c, d, e, f, g, h);
    }
    
    public Int64(byte[] data) {
        this(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
    }
    
    public static long packBytes(int a, int b, int c, int d, int e, int f, int g, int h) {
        return packBytes((byte)a, (byte)b, (byte)c, (byte)d, (byte)e, (byte)f, (byte)g, (byte)h);
    }
    
    public static long packBytes(byte a, byte b, byte c, byte d, byte e, byte f, byte g, byte h) {
        return (((long)(a & 0xff) << 56)|((long)(b & 0xff) << 48)|((long)(c & 0xff) << 40)|((long)(d & 0xff) << 32)|((long)(e & 0xff) << 24)|((long)(f & 0xff) << 16)|((long)(g & 0xff) << 8)|(long)(h & 0xff));
    }
    
    public static byte[] getBytes(long value, byte[] output) {
        output[0] = (byte) (value >>> 56);
        output[1] = (byte) (value >>> 48);
        output[2] = (byte) (value >>> 40);
        output[3] = (byte) (value >>> 32);
        output[4] = (byte) (value >>> 24);
        output[5] = (byte) (value >>> 16);
        output[6] = (byte) (value >>> 8);
        output[7] = (byte) (value);
        return output;
    }
    
    public byte[] getBytes() {
        return new byte[] {(byte) (value >>> 56), (byte) (value >>> 48), (byte)(value >>> 40), (byte)(value >>> 32), (byte) (value >>> 24), (byte) (value >>> 16), (byte)(value >>> 8), (byte)(value)};
    }
    
    @Override
    public int intValue() {
        return (int) value;
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
        hash = 97 * hash + (int) (this.value ^ (this.value >>> 32));
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
        final Int64 other = (Int64) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
