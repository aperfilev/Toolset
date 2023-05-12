package local.tools.serial.bjson;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import local.tools.bin.BinaryReader;
import local.tools.bin.EndianType;

public class BJSONReader implements AutoCloseable {
    
    private final BinaryReader reader;
    
    public BJSONReader(InputStream source, boolean littleEndian) {
        this.reader = new BinaryReader(source, littleEndian ? EndianType.LittleEndian : EndianType.BigEndian);
    }
    
    public BJSONReader(InputStream source) {
        this(source, true);
    }
    
    public BJSONReader(String filename, boolean littleEndian) throws FileNotFoundException {
        this(new BufferedInputStream(new FileInputStream(filename)), littleEndian);
    }
    
    public BJSONReader(String filename) throws FileNotFoundException {
        this(filename, true);
    }
    
    public Object readBJSONNode() throws IOException {
        BJSONType type = BJSONType.fromValue(reader.read());
        return readValue(type);
    }
    
    public BJSONNode readNullValue() throws IOException {
        return BJSONNullNode.INSTANCE;
    }
    
    public BJSONNode readBJSONObject() throws IOException {
        BJSONObject bjsonObject = new BJSONObject();
        int size = reader.readInt();
        for (int i=0; i<size; ++i) {
            String key = reader.readString();
            BJSONType type = BJSONType.fromValue(reader.read());
            if (type == null) throw new BJSONException("Unknown element type detected.");
            Object value = readValue(type);
            bjsonObject.put(key, value);
        }
        return bjsonObject;
    }
    
    public BJSONNode readBJSONArray() throws IOException {
        BJSONArray bjsonArray = new BJSONArray();
        int size = reader.readInt();
        for (int i=0; i<size; ++i) {
            BJSONType type = BJSONType.fromValue(reader.read());
            if (type == null) throw new BJSONException("Unknown element type detected.");
            Object value = readValue(type);
            bjsonArray.add(value);
        }
        return bjsonArray;
    }
    
    public Object readValue(BJSONType type) throws IOException {
        switch (type) {
            case NULL: {
                return readNullValue();
            }
            case BJSONArray: {
                return readBJSONArray();
            }
            case BJSONObject: {
                return readBJSONObject();
            }
            case String: {
                return reader.readString();
            }
            case Boolean:
                return reader.readBoolean();
            case Int8:
                return reader.readByte();
            case Int16:
                return reader.readShort();
            case Int32:
                return reader.readInt();
            case Int64:
                return reader.readLong();
            case Float32:
                return reader.readFloat();
            case Float64:
                return reader.readDouble();
            case UInt8:
                return reader.readUInt8();
            case UInt16:
                return reader.readUInt16();
            case UInt32:
                return reader.readUInt32();
            case UInt64:
                return reader.readUInt64();
            default:
                throw new BJSONException("This type is not supported.");
        }
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}
