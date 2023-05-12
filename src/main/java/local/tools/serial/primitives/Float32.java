package local.tools.serial.primitives;

/**
 * Type represents 32 bits (4 bytes) float type
 * Alias: Float, float32
 * Size: 32 bits (4 bytes)
 */
public class Float32 extends Number {
    
    private final float value;
    
    public Float32(float value) {
        this.value = value;
    }
    
    public Float32(int a, int b, int c, int d) {
        this((byte)a, (byte)b, (byte)c, (byte)d);
    }
    
    public Float32(byte a, byte b, byte c, byte d) {
        int joinedBytes = Int32.packBytes(a, b, c, d);
        this.value = Float.intBitsToFloat(joinedBytes);
    }
    
    public Float32(byte[] data) {
        this(data[0], data[1], data[2], data[3]);
    }
    
    public static float packBytes(int a, int b, int c, int d) {
        return packBytes((byte)a, (byte)b, (byte)c, (byte)d);
    }
    
    public static float packBytes(byte a, byte b, byte c, byte d) {
        int joinedBytes = Int32.packBytes(a, b, c, d);
        return Float.intBitsToFloat(joinedBytes);
    }
    
    public static byte[] getBytes(float value, byte[] output) {
        int v = Float.floatToRawIntBits(value);
        output[0] = (byte) (v >>> 24);
        output[1] = (byte) (v >>> 16);
        output[2] = (byte) (v >>> 8);
        output[3] = (byte) (v);
        return output;
    }
    
    public byte[] getBytes() {
        int v = Float.floatToRawIntBits(this.value);
        return new byte[] {(byte) (v >>> 24), (byte) (v >>> 16), (byte)(v >>> 8), (byte)(v)};
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
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
        hash = 83 * hash + Float.floatToIntBits(this.value);
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
        final Float32 other = (Float32) obj;
        if (Float.floatToIntBits(this.value) != Float.floatToIntBits(other.value)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
