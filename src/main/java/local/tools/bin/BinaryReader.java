package local.tools.bin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import local.tools.code.Verify;
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

public final class BinaryReader extends InputStream implements AutoCloseable {

    /* End Of Line symbol */
    private final static char EOL = '\0';
    private final static char END = (char) -1;
    private final static char NL = '\n';
    private final static char CR = '\r';

    private final InputStream source;
    private final EndianType endianType;

    private NumberReader numberReader;
    private long position = 0;

    //TODO
    // - [x] - Make reading primitive types by byte arrays - int - byte[4], long - byte[8]
    // - [ ] - Make handling -1 byte while reading e.g. Int32 [1, 2, 3, -1] - should throw IOException ??

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public BinaryReader(InputStream source, EndianType endianType) {
        this.source = source;
        this.endianType = endianType;

        init();
    }

    public BinaryReader(String filename, EndianType endianType) throws FileNotFoundException {
        this(new FileInputStream(filename), endianType);
    }

    public BinaryReader(InputStream source) {
        this(source, EndianType.LittleEndian);
    }

    public BinaryReader(String filename) throws FileNotFoundException {
        this(filename, EndianType.LittleEndian);
    }

    private void init() {
        switch (endianType) {
            case BigEndian:
                numberReader = new BigEndianNumberReader(source);
                break;
            default:
            case LittleEndian:
                numberReader = new LittleEndianNumberReader(source);
                break;
        }
    }
    //</editor-fold>

    public long getPosition() {
        return position;
    }

    @Override
    public long skip(long count) throws IOException {
        this.position += count;
        return source.skip(count);
    }

    public void seek(long position) throws IOException {
        Verify.checkTrue(position < this.position, "Requested seek back which is not supported.");

        long count = position - this.position;
        skip(count);
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public int available() throws IOException {
        return source.available();
    }

    //<editor-fold defaultstate="collapsed" desc="InputStream Proxy Methods">
    @Override
    public int read() throws IOException {
        this.position += 1;
        return source.read();
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        int r = source.read(buffer);
        this.position += r;
        return r;
    }

    @Override
    public int read(byte[] buffer, int index, int count) throws IOException {
        int r = source.read(buffer, index, count);
        this.position += r;
        return r;
    }

    //TODO: Method need to throw IO if less than count bytes read
    public byte[] readBytes(int count) throws IOException {
        byte[] bytes = new byte[count];
        int r = read(bytes);
        if (r < 0) return new byte[0];
        return (r == count) ? bytes : Arrays.copyOf(bytes, r);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Read Java Types">
    public boolean readBoolean() throws IOException {
        return (read() != 0);
    }

    public byte readByte() throws IOException {
        this.position += Byte.BYTES;
        return numberReader.readByte();
    }

    public char readChar() throws IOException {
        this.position += Short.BYTES;
        return (char) numberReader.readShort();
    }

    public short readShort() throws IOException {
        this.position += Short.BYTES;
        return (short) numberReader.readShort();
    }

    public int readInt() throws IOException {
        this.position += Integer.BYTES;
        return numberReader.readInt();
    }

    public long readLong() throws IOException {
        this.position += Long.BYTES;
        return numberReader.readLong();
    }

    public float readFloat() throws IOException {
        this.position += Float.BYTES;
        return Float.intBitsToFloat(numberReader.readInt());
    }

    public double readDouble() throws IOException {
        this.position += Double.BYTES;
        return Double.longBitsToDouble(numberReader.readLong());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Read Primitives">
    public Int8 readInt8() throws IOException {
        this.position += Byte.BYTES;
        return numberReader.readInt8();
    }

    public Int16 readInt16() throws IOException {
        this.position += Short.BYTES;
        return numberReader.readInt16();
    }

    public Int32 readInt32() throws IOException {
        this.position += Integer.BYTES;
        return numberReader.readInt32();
    }

    public Int64 readInt64() throws IOException {
        this.position += Long.BYTES;
        return numberReader.readInt64();
    }

    public UInt8 readUInt8() throws IOException {
        this.position += Byte.BYTES;
        return numberReader.readUInt8();
    }

    public UInt16 readUInt16() throws IOException {
        this.position += Short.BYTES;
        return numberReader.readUInt16();
    }

    public UInt32 readUInt32() throws IOException {
        this.position += Integer.BYTES;
        return numberReader.readUInt32();
    }

    public UInt64 readUInt64() throws IOException {
        this.position += Long.BYTES;
        return numberReader.readUInt64();
    }

    public Float32 readFloat32() throws IOException {
        this.position += Float.BYTES;
        return numberReader.readFloat32();
    }

    public Float64 readFloat64() throws IOException {
        this.position += Double.BYTES;
        return numberReader.readFloat64();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Read Strings">    
    public String readString() throws IOException {
        List<Byte> buffer = new ArrayList<>();
        while (true) {
            byte b = (byte) read();
            if ((char) b == END || (char) b == EOL) break;
            buffer.add(b);
        }
        byte[] data = new byte[buffer.size()];
        for (int i = 0, len = buffer.size(); i < len; ++i) {
            data[i] = buffer.get(i);
        }
        return new String(data);
    }

    public String readString(int length) throws IOException {
        byte[] data = readBytes(length);
        return new String(data);
    }

    public char[] readChars(int count) throws IOException {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            char c = (char) read();
            if (c == END || c == EOL) break;
            buffer.append(c);
        }
        return buffer.toString().toCharArray();
    }
    //</editor-fold>
}
