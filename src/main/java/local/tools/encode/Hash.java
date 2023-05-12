package local.tools.encode;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public final class Hash {

    private static final int BUFFER_SIZE = 4068;
    
    public enum HashType {
        SHA_1   ("SHA-1"),
        MD5     ("MD5"),
        CRC32   ("CRC32");
        
        public final String value;

        private HashType(String value) {
            this.value = value;
        }
    }
    
    private static byte[] createChecksum(InputStream data, HashType type) throws NoSuchAlgorithmException, IOException {
        MessageDigest complete;
        byte[] buffer = new byte[BUFFER_SIZE];
        complete = MessageDigest.getInstance(type.value);
        int numRead;
        do {
            numRead = data.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        return complete.digest();
    }

    public static String calcSHA_1(String filename) throws Exception {
        return calcSum(filename, HashType.SHA_1);
    }
    
    public static String calcSHA_1(byte[] data) throws Exception {
        try (InputStream is = new ByteArrayInputStream(data)) {
            return calcSum(is, HashType.SHA_1);
        }
    }

    public static String calcMD5(String filename) throws Exception {
        return calcSum(filename, HashType.MD5);
    }
    
    public static String calcMD5(byte[] data) throws Exception {
        try (InputStream is = new ByteArrayInputStream(data)) {
            return calcSum(is, HashType.MD5);
        }
    }

    public static String calcSum(InputStream data, HashType type) throws Exception {
        byte[] b = createChecksum(data, type);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String calcSum(String filename, HashType type) throws Exception {
        try (InputStream is = new FileInputStream(filename)) {
            return calcSum(is, type);
        }
    }
    
    public static String calcCRC32(byte[] data) throws Exception {
        try (InputStream is = new ByteArrayInputStream(data)) {
            return calcCRC32(is);
        }
    }

    public static String calcCRC32(String filename) throws Exception {
        try (InputStream is = new FileInputStream(filename)) {
            return calcCRC32(is);
        }
    }
    
    private static String calcCRC32(InputStream input) throws Exception {
        Checksum checksum = new CRC32();
        try {
            try (BufferedInputStream is = new BufferedInputStream(input)) {
                byte[] bytes = new byte[BUFFER_SIZE];
                int len;
                while ((len = is.read(bytes)) >= 0) {
                    checksum.update(bytes, 0, len);
                }
            }
        } catch (IOException e) {
            throw new Exception("Unable to calc crc32.", e);
        }
        return Long.toHexString(checksum.getValue());
    }
}
