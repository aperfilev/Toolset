package local.tools.serial.primitives;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Type represents 64 bits (8 bytes) unsigned integer type
 * Alias: ULong
 * Size: 64 bits (8 bytes)
 * Range: 0 to 18,446,744,073,709,551,615
 */
public class UInt64 extends Number {

    public static final BigInteger MIN_VALUE = BigInteger.ZERO;
    public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");
    
    private final BigInteger value;
    
    public UInt64(BigInteger value) {
        if (!withinRange(value)) throw new IllegalArgumentException(String.format("Value '%s' is out of 8-Byte Unsigned Integer range.", value.toString()));
        this.value = value;
    }

    public UInt64(int a, int b, int c, int d, int e, int f, int g, int h) {
        this((byte)a, (byte)b, (byte)c, (byte)d, (byte)e, (byte)f, (byte)g, (byte)h);
    }
    
    public UInt64(byte[] bytes) {
        this(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
    }
    
    public UInt64(byte a, byte b, byte c, byte d, byte e, byte f, byte g, byte h) {
        value = new BigInteger(new byte[] {0 /*1st byte - positive sign*/, a, b, c, d, e, f, g, h});
    }
    
    public static byte[] getBytes(BigInteger value, byte[] output) {
        byte[] bytes = value.toByteArray();
        for (int i=7, j=bytes.length-1; i>=0 && j>=0; --i, --j) {
            output[i] = bytes[j];
        }
        return output;
    }

    public byte[] getBytes() {
        byte[] bytes = value.toByteArray();
        byte[] output = new byte[8];
        for (int i = 7, j = bytes.length - 1; i >= 0 && j >= 0; --i, --j) {
            output[i] = bytes[j];
        }
        return output;
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);
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
        final UInt64 other = (UInt64) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return value.toString();
    }

    private static boolean withinRange(BigInteger value) {
        return (value.compareTo(MIN_VALUE) >= 0 && value.compareTo(MAX_VALUE) <= 0);
    }
}
