package local.tools.serial.bjson;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import local.tools.bin.BinaryWriter;
import local.tools.bin.EndianType;
import local.tools.serial.primitives.UInt16;
import local.tools.serial.primitives.UInt32;
import local.tools.serial.primitives.UInt64;
import local.tools.serial.primitives.UInt8;

public class BJSONWriter implements AutoCloseable {
    
    private final BinaryWriter writer;

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public BJSONWriter(OutputStream source, boolean littleEndian) {
        this.writer = new BinaryWriter(source, littleEndian ? EndianType.LittleEndian : EndianType.BigEndian);
    }
    
    public BJSONWriter(OutputStream target) {
        this(target, true);
    }
    
    public BJSONWriter(String filename, boolean littleEndian) throws FileNotFoundException {
        this(new BufferedOutputStream(new FileOutputStream(filename)), littleEndian);
    }
    
    public BJSONWriter(String filename) throws FileNotFoundException {
        this(filename, true);
    }
    //</editor-fold>
    
    public static BJSONType detectType(Object o) {
        if (o == null) return BJSONType.NULL;
        String className = o.getClass().getSimpleName();
        switch (className) {
            case "BJSONNullNode":   return BJSONType.NULL;
            case "BJSONArray":      return BJSONType.BJSONArray;
            case "BJSONObject":     return BJSONType.BJSONObject;
            case "String":          return BJSONType.String;
            case "Boolean":         return BJSONType.Boolean;
            case "Float":           return BJSONType.Float32;
            case "Double":          return BJSONType.Float64;
            case "Byte":            return BJSONType.Int8;
            case "Short":           return BJSONType.Int16;
            case "Integer":         return BJSONType.Int32;
            case "Long":            return BJSONType.Int64;
            case "UInt8":           return BJSONType.UInt8;
            case "UInt16":          return BJSONType.UInt16;
            case "UInt32":          return BJSONType.UInt32;
            case "UInt64":          return BJSONType.UInt64;
            default: {
                throw new RuntimeException("Unsupported Type");
            }
        }
    }
    
    public void writeBJSONNode(BJSONNode node) throws IOException {
        writeType(node.getType());
        writeValue(node, node.getType());
    }
    
    public void writeBJSONObject(BJSONObject object) throws IOException {
        int size = object.getSize();
        writeSize(size);
        for (Map.Entry<String, Object> entry : object.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            BJSONType type = detectType(value);
            writeKey(key);
            writeType(type);
            writeValue(value, type);
        }
    }
    
    public void writeBJSONArray(BJSONArray array) throws IOException {
        int size = array.getSize();
        writeSize(size);
        for (int i=0; i<size; ++i) {
            Object value = array.get(i);
            BJSONType type = detectType(value);
            writeType(type);
            writeValue(value, type);
        }
    }
    
    public void writeKey(String key) throws IOException {
        writeString(key);
    }
    
    public void writeValue(Object value, BJSONType type) throws IOException {
        switch (type) {
            case NULL: {
                //do nothing
                break;
            }
            case BJSONArray: {
                writeBJSONArray((BJSONArray) value);
                break;
            }
            case BJSONObject: {
                writeBJSONObject((BJSONObject) value);
                break;
            }
            case String: {
                writeString((String) value);
                break;
            }
            case Boolean:
                writer.writeBoolean((Boolean) value);
                break;
            case Int8:
                writer.writeByte((Byte) value);
                break;
            case Int16:
                writer.writeShort((Short) value);
                break;
            case Int32:
                writer.writeInt((Integer) value);
                break;
            case Int64:
                writer.writeLong((Long) value);
                break;
            case UInt8:
                writer.writeUInt8((UInt8) value);
                break;                
            case UInt16:
                writer.writeUInt16((UInt16) value);
                break;
            case UInt32:
                writer.writeUInt32((UInt32) value);
                break;
            case UInt64:
                writer.writeUInt64((UInt64) value);
                break;
            case Float32:
                writer.writeFloat((Float) value);
                break;
            case Float64:
                writer.writeDouble((Double) value);
                break;
            default:
                throw new BJSONException(String.format("This type: '%s' is not supported.", value.getClass().getName()));
        }
    }
    
    public void writeType(BJSONType type) throws IOException {
        writer.writeByte(type.getValue());
    }
    
    public void writeSize(int size) throws IOException {
        writer.writeInt(size);
    }
    
    public void writeString(String str) throws IOException {
//        byte[] data = str.getBytes();
//        int length = data.length;
//        writer.writeInt(length);
//        writer.write(data);
        writer.writeBytes(str);
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
