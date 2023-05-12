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

public interface NumberWriter {
    
    public void writeByte(int v) throws IOException;
    public void writeShort(int v) throws IOException;
    public void writeInt(int v) throws IOException;
    public void writeLong(long v) throws IOException;
    
    public void writeFloat(float v) throws IOException;
    public void writeDouble(double v) throws IOException;
    
    public void writeInt8(Int8 v) throws IOException;
    public void writeInt16(Int16 v) throws IOException;
    public void writeInt32(Int32 v) throws IOException;
    public void writeInt64(Int64 v) throws IOException;
    
    public void writeUInt8(UInt8 v) throws IOException;
    public void writeUInt16(UInt16 v) throws IOException;
    public void writeUInt32(UInt32 v) throws IOException;
    public void writeUInt64(UInt64 v) throws IOException;
    
    public void writeFloat32(Float32 v) throws IOException;
    public void writeFloat64(Float64 v) throws IOException;
}
