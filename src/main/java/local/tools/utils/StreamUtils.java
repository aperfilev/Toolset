package local.tools.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Math.min;

public final class StreamUtils {

    public static final int BUFFER_SIZE = 4096;

    private StreamUtils() {
    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        return readBytes(inputStream, Integer.MAX_VALUE);
    }
    
    public static byte[] readBytes(InputStream inputStream, long maxContentLength) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            copyBytes(inputStream, byteStream, maxContentLength);
            return byteStream.toByteArray();
        }
    }

    public static long copyBytes(InputStream source, OutputStream target) throws IOException {
        return copyBytes(source, target, Long.MAX_VALUE);
    }
    
    public static long copyBytes(InputStream source, DataOutput target, long maxContentLength) throws IOException {
        maxContentLength = maxContentLength > 0 ? maxContentLength : Long.MAX_VALUE;
        long transfered = 0L;
        try {
            byte[] buffer = new byte[(int) min(BUFFER_SIZE, maxContentLength)];
            int len;
            while (transfered < maxContentLength) {
                if (Thread.currentThread().isInterrupted()) break;
                len = source.read(buffer);
                if (len == -1) break;
                
                target.write(buffer, 0, len);
                transfered += len;
            }
        } catch (IOException e) {
            throw e;
        }
        return transfered;
    }
    
    public static long copyBytes(InputStream source, OutputStream target, long maxContentLength) throws IOException {
        maxContentLength = maxContentLength > 0 ? maxContentLength : Long.MAX_VALUE;
        long transfered = 0L;
        try {
            byte[] buffer = new byte[(int) min(BUFFER_SIZE, maxContentLength)];
            int len;
            while (transfered < maxContentLength) {
                if (Thread.currentThread().isInterrupted()) break;
                len = source.read(buffer);
                if (len == -1) break;
                
                target.write(buffer, 0, len);
                transfered += len;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            target.flush();
        }
        return transfered;
    }
}
