package local.tools.bin;

import java.io.IOException;
import java.io.OutputStream;
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

public class LittleEndianNumberWriter implements NumberWriter {
    
    private final OutputStream target;

    public LittleEndianNumberWriter(OutputStream target) {
        this.target = target;
    }

    //<editor-fold defaultstate="collapsed" desc="Basic Java Types">
    @Override
    public void writeByte(int v) throws IOException {
        writeInt8(new Int8(v));
    }
    
    @Override
    public void writeShort(int v) throws IOException {
        writeInt16(new Int16(v));
    }
    
    @Override
    public void writeInt(int v) throws IOException {
        writeInt32(new Int32(v));
    }
    
    @Override
    public void writeLong(long v) throws IOException {
        writeInt64(new Int64(v));
    }
    
    @Override
    public void writeFloat(float v) throws IOException {
        writeFloat32(new Float32(v));
    }
    
    @Override
    public void writeDouble(double v) throws IOException {
        writeFloat64(new Float64(v));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Primitives">
    @Override
    public void writeInt8(Int8 v) throws IOException {
        target.write(v.getBytes());
    }
    
    @Override
    public void writeInt16(Int16 v) throws IOException {
        byte[] data = v.getBytes();
        target.write(data[1]);
        target.write(data[0]);
    }
    
    @Override
    public void writeInt32(Int32 v) throws IOException {
        byte[] data = v.getBytes();
        target.write(data[3]);
        target.write(data[2]);
        target.write(data[1]);
        target.write(data[0]);
    }
    
    @Override
    public void writeInt64(Int64 v) throws IOException {
        byte[] data = v.getBytes();
        target.write(data[7]);
        target.write(data[6]);
        target.write(data[5]);
        target.write(data[4]);
        target.write(data[3]);
        target.write(data[2]);
        target.write(data[1]);
        target.write(data[0]);
    }
    
    @Override
    public void writeFloat32(Float32 v) throws IOException {
        byte[] data = v.getBytes();
        target.write(data[3]);
        target.write(data[2]);
        target.write(data[1]);
        target.write(data[0]);
    }
    
    @Override
    public void writeFloat64(Float64 v) throws IOException {
        byte[] data = v.getBytes();
        target.write(data[7]);
        target.write(data[6]);
        target.write(data[5]);
        target.write(data[4]);
        target.write(data[3]);
        target.write(data[2]);
        target.write(data[1]);
        target.write(data[0]);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Unsigned Primitives">
    @Override
    public void writeUInt8(UInt8 v) throws IOException {
        target.write(v.getBytes());
    }
    
    @Override
    public void writeUInt16(UInt16 v) throws IOException {
        byte[] data = v.getBytes();
        target.write(data[1]);
        target.write(data[0]);
    }
    
    @Override
    public void writeUInt32(UInt32 v) throws IOException {
        byte[] data = v.getBytes();
        target.write(data[3]);
        target.write(data[2]);
        target.write(data[1]);
        target.write(data[0]);
    }
    
    @Override
    public void writeUInt64(UInt64 v) throws IOException {
        byte[] data = v.getBytes();
        target.write(data[7]);
        target.write(data[6]);
        target.write(data[5]);
        target.write(data[4]);
        target.write(data[3]);
        target.write(data[2]);
        target.write(data[1]);
        target.write(data[0]);
    }
    //</editor-fold>
}
