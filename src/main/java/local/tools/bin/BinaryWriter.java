package local.tools.bin;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class BinaryWriter extends OutputStream implements AutoCloseable {

    private final static char EOL = '\0';

    private final OutputStream target;
    private final EndianType endianType;
    
    private NumberWriter numberWriter;
       
    public BinaryWriter(OutputStream target, EndianType endianType) {
        this.target = target;
        this.endianType = endianType;
        
        init();
    }
    
    public BinaryWriter(String filename, EndianType endianType) throws FileNotFoundException {
        this(new FileOutputStream(filename), endianType);
    }
    
    public BinaryWriter(OutputStream target) {
        this(target, EndianType.LittleEndian);
    }
    
    public BinaryWriter(String filename) throws FileNotFoundException {
        this(filename, EndianType.LittleEndian);
    }
    
    private void init() {
        switch (endianType) {
            case BigEndian:
                numberWriter = new BigEndianNumberWriter(target);
                break;
            default:
            case LittleEndian:
                numberWriter = new LittleEndianNumberWriter(target);
                break;
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="OutputStream Proxy Methods">
    @Override
    public void write(int b) throws IOException {
        target.write(b);
    }
    
    @Override
    public void write(byte[] buffer) throws IOException {
        target.write(buffer);
    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        target.write(buffer, offset, length);
    }
    
    @Override
    public void flush() throws IOException {
        target.flush();
    }
    
    @Override
    public void close() throws IOException {
        target.close();
    }
    //</editor-fold>
       
    //<editor-fold defaultstate="collapsed" desc="Write Java Types">
    public void writeBoolean(boolean b) throws IOException {
        write(b ? 1 : 0);
    }
    
    public void writeByte(int b) throws IOException {
        write(b);
    }
    
    public void writeByte(byte b) throws IOException {
        write(b);
    }
    
    public void writeChar(char value) throws IOException {
        numberWriter.writeShort(value);
    }
    
    public final void writeShort(short value) throws IOException {
        numberWriter.writeShort(value);
    }
    
    public final void writeInt(int value) throws IOException {
        numberWriter.writeInt(value);
    }
    
    public final void writeLong(long value) throws IOException {
        numberWriter.writeLong(value);
    }
    
    public void writeFloat(float value) throws IOException {
        //TODO check NaN values to processed correctly
        int bits = Float.floatToRawIntBits(value);
        numberWriter.writeInt(bits);
    }
    
    public void writeDouble(double value) throws IOException {
        long bits = Double.doubleToRawLongBits(value);
        numberWriter.writeLong(bits);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Write Primitives">
    public void writeInt8(Int8 value) throws IOException {
        numberWriter.writeInt8(value);
    }
    
    public void writeInt16(Int16 value) throws IOException {
        numberWriter.writeInt16(value);
    }
    
    public void writeInt32(Int32 value) throws IOException {
        numberWriter.writeInt32(value);
    }
    
    public void writeInt64(Int64 value) throws IOException {
        numberWriter.writeInt64(value);
    }
    
    public void writeUInt8(UInt8 value) throws IOException {
        numberWriter.writeUInt8(value);
    }
    
    public void writeUInt16(UInt16 value) throws IOException {
        numberWriter.writeUInt16(value);
    }
    
    public void writeUInt32(UInt32 value) throws IOException {
        numberWriter.writeUInt32(value);
    }
    
    public void writeUInt64(UInt64 value) throws IOException {
        numberWriter.writeUInt64(value);
    }
    
    public void writeFloat32(Float32 value) throws IOException {
        numberWriter.writeFloat32(value);
    }
    
    public void writeFloat64(Float64 value) throws IOException {
        numberWriter.writeFloat64(value);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Write Strings">
    public final void writeBytes(String str) throws IOException {
        byte[] data = str.getBytes();
        target.write(data);
        target.write(EOL);
    }
    
    public final void writeChars(char[] chars) throws IOException {
        for (int i = 0; i < chars.length ; ++i) {
            writeChar(chars[i]);
        }
    }
    //</editor-fold>
}
