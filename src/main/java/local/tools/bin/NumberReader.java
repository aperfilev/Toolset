package local.tools.bin;

import java.io.IOException;
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

public interface NumberReader {
    
    public byte   readByte() throws IOException;
    public short  readShort() throws IOException;
    public int    readInt() throws IOException;
    public long   readLong() throws IOException;
    
    public float  readFloat() throws IOException;
    public double readDouble() throws IOException;

    public Int8   readInt8() throws IOException;
    public Int16  readInt16() throws IOException;
    public Int32  readInt32() throws IOException;
    public Int64  readInt64() throws IOException;
    
    public UInt8  readUInt8() throws IOException;
    public UInt16 readUInt16() throws IOException;
    public UInt32 readUInt32() throws IOException;
    public UInt64 readUInt64() throws IOException;
    
    public Float32 readFloat32() throws IOException;
    public Float64 readFloat64() throws IOException;
}
