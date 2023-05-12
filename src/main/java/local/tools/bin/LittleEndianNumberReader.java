package local.tools.bin;

import java.io.IOException;
import java.io.InputStream;
import local.tools.serial.primitives.Float32;
import local.tools.serial.primitives.Float64;
import local.tools.serial.primitives.Int16;
import local.tools.serial.primitives.Int32;
import local.tools.serial.primitives.Int64;
import local.tools.serial.primitives.Int8;
import local.tools.serial.primitives.UInt16;
import local.tools.serial.primitives.UInt32;
import local.tools.serial.primitives.UInt64;
import local.tools.serial.primitives.UInt8;

public class LittleEndianNumberReader implements NumberReader {

    private final InputStream source;
    
    public LittleEndianNumberReader(InputStream source) {
        this.source = source;
    }

    //<editor-fold defaultstate="collapsed" desc="Basic Java Types">
    @Override
    public byte readByte() throws IOException {
        return (byte) source.read();
    }
    
    @Override
    public short readShort() throws IOException {
        int b = source.read();
        int a = source.read();
        return Int16.packBytes(a, b);
    }
    
    @Override
    public int readInt() throws IOException {
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return Int32.packBytes(a, b, c, d);
    }
    
    @Override
    public long readLong() throws IOException {
        int h = source.read();
        int g = source.read();
        int f = source.read();
        int e = source.read();
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return Int64.packBytes(a, b, c, d, e, f, g, h);
    }
    
    @Override
    public float readFloat() throws IOException {
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return Float32.packBytes(a, b, c, d);
    }
    
    @Override
    public double readDouble() throws IOException {
        int h = source.read();
        int g = source.read();
        int f = source.read();
        int e = source.read();
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return Float64.packBytes(a, b, c, d, e, f, g, h);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Primitives">
    @Override
    public Int8 readInt8() throws IOException {
        int a = source.read();
        return new Int8(a);
    }
    
    @Override
    public Int16 readInt16() throws IOException {
        int b = source.read();
        int a = source.read();
        return new Int16(a, b);
    }
    
    @Override
    public Int32 readInt32() throws IOException {
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return new Int32(a, b, c, d);
    }
    
    @Override
    public Int64 readInt64() throws IOException {
        int h = source.read();
        int g = source.read();
        int f = source.read();
        int e = source.read();
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return new Int64(a, b, c, d, e, f, g, h);
    }
    
    @Override
    public Float32 readFloat32() throws IOException {
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return new Float32(a, b, c, d);
    }
    
    @Override
    public Float64 readFloat64() throws IOException {
        int h = source.read();
        int g = source.read();
        int f = source.read();
        int e = source.read();
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return new Float64(a, b, c, d, e, f, g, h);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Unsigned Primitives">
    @Override
    public UInt8 readUInt8() throws IOException {
        return new UInt8((byte)source.read());
    }
    
    @Override
    public UInt16 readUInt16() throws IOException {
        int b = source.read();
        int a = source.read();
        return new UInt16(a, b);
    }
    
    @Override
    public UInt32 readUInt32() throws IOException {
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return new UInt32(a, b, c, d);
    }
    
    @Override
    public UInt64 readUInt64() throws IOException {
        int h = source.read();
        int g = source.read();
        int f = source.read();
        int e = source.read();
        int d = source.read();
        int c = source.read();
        int b = source.read();
        int a = source.read();
        return new UInt64(a, b, c, d, e, f, g, h);
    }
    //</editor-fold>
}
