package local.tools.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import local.tools.logs.Logger;
import local.tools.utils.StreamUtils;
import local.tools.utils.StringUtils;

import static java.lang.String.format;
import static local.tools.logs.Logger.log;
import static local.tools.logs.Logger.error;

public final class File {

    private static final String END_LINE = System.getProperty("line.separator");
    private static final String FILE_ENCODING = "utf8";//utf8 //cp1252 //System.getProperty("file.encoding");
    public  static final Charset CHARSET = Charset.forName(FILE_ENCODING);

    //<editor-fold defaultstate="collapsed" desc="Exceptions">
    public static final class OverwriteException extends Exception {
        public OverwriteException(String message) {
            super(message);
        }
    }
    //</editor-fold>

    public static final class LineIterator implements Iterator<String>, Iterable<String>, AutoCloseable {

        private BufferedReader reader = null;
        private boolean hasNext = true;
        private String line = "";

        public LineIterator(String filepath) throws Exception {
            java.io.File file = new java.io.File(filepath);
            checkFileValidForRead(file);

            setInputStream(new FileInputStream(file));
        }

        private void setInputStream(InputStream is) throws Exception {
            if(reader != null)
                reader.close();

            try {
                reader = new BufferedReader(new InputStreamReader(is, FILE_ENCODING));
                line = reader.readLine();
                hasNext = (line != null);
            } catch (Exception e) {
                dispose();
                throw e;
            }
        }

        public void dispose() {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ignored) {
            }
        }

        @Override
        public void close() throws Exception {
            dispose();
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public String next() {
            String result;
            if(hasNext) {
                result = line;
                try {
                    line = reader.readLine();
                    hasNext = (line != null);
                } catch(IOException e) {
                    hasNext = false;
                    dispose();
                    //hide this exception
                }
                return result;
            } else {
                dispose();
                return null;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported operation.");
        }

        @Override
        protected void finalize() throws Throwable {
            dispose();
            super.finalize();
        }

        @Override
        public Iterator<String> iterator() {
            return this;
        }
    }

    public static final class LineAppender implements AutoCloseable {
        private BufferedWriter writer = null;
        private long counter = 0;

        private final String filepath;
        private boolean overwrite = false;
        
        public LineAppender(String filepath) throws Exception {
            this(filepath, false);
        }

        public LineAppender(String filepath, boolean overwrite) throws Exception {
            this.filepath = filepath;
            this.overwrite = overwrite;
            init();
        }

        public final void init() throws Exception {
            java.io.File destFile = new java.io.File(filepath);
            checkFileValidForWrite(destFile);

            try  {
                setOutputStream(new FileOutputStream(destFile, ! this.overwrite));
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                close();
                throw e;
            }
        }

        private void setOutputStream(OutputStream os) throws FileNotFoundException, UnsupportedEncodingException {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os, FILE_ENCODING);
            writer = new BufferedWriter(outputStreamWriter);
        }

        public final void flush() {
            try {
                if (writer != null) {
                    writer.flush();
                }
            } catch(IOException e) {
                error("Unable to flush content.");
            }
        }

        @Override
        public final void close() {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                    writer = null;
                }
            } catch (IOException e) {
                log("Unable to dispose FileAppender. '%s'", e.getMessage());
            }
        }

        public void append(String line) {
            try {
                writer.write(line);
                writer.write(END_LINE);
                ++counter;
            } catch(IOException e) {
                Logger.error("File Append Error.");
            }
        }

        public void reload() {
            try {
                close();
                init();
            } catch(Exception e) {
                Logger.error("Unable to reload LineAppender.");
            }
        }

        public long getCounter() {
            return counter;
        }

        @Override
        protected void finalize() throws Throwable {
            close();
            super.finalize();
        }
    }

    /* Alias for LineIterator */
    public static LineIterator byLines(String filename) throws Exception {
        return new LineIterator(filename);
    }

    public static LineIterator getLineIterator(String filepath) throws Exception {
        return new LineIterator(filepath);
    }

    public static LineAppender getLineAppender(String filepath) throws Exception {
        return new LineAppender(filepath);
    }

    //<editor-fold defaultstate="collapsed" desc="Zip Features">
    public static LineIterator getZipLineIterator(String filepath) throws Exception {
        LineIterator iterator = new LineIterator(filepath);
        ZipInputStream zis = new ZipInputStream(new FileInputStream(filepath));
        zis.getNextEntry();
        iterator.setInputStream(zis);
        return iterator;
    }

    public static LineAppender getZipLineAppender(String filepath) throws Exception {
        LineAppender appender = new LineAppender(filepath);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(filepath));
        ZipEntry ze = new ZipEntry(FilePath.parse(filepath).getName());
        zos.putNextEntry(ze);
        appender.setOutputStream(zos);
        return appender;
    }
    //</editor-fold>

    public static long length(String filepath) {
        java.io.File f = new java.io.File(filepath);
        return f.length();
    }
    
    public static boolean exist(String file) {
        java.io.File f = new java.io.File(file);
        return f.isFile() && f.exists();
    }

    public static void clean(String file) {
        try {
            File.writeAllText("", file, true);
        } catch(Exception e) {
            Logger.error("Unable to clean file '%s' due to '%s'", file, e.getMessage());
        }
    }
    
    public static void nioDelete(String filepath) throws IOException {
        java.io.File file = new java.io.File(filepath);
        java.nio.file.Files.delete(file.toPath());
    }

    public static boolean delete(String filepath) {
        java.io.File target = new java.io.File(filepath);
        boolean success = target.delete();
        if ( ! success) {
            Logger.error("Unable to delete filepath: '%s'.", filepath);
        }
        return success;
    }

    public static boolean rename(String from_file, String dest_file) {
        java.io.File fromFile = new java.io.File(from_file);
        java.io.File destFile = new java.io.File(dest_file);

        return fromFile.renameTo(destFile);
    }

    //<editor-fold desc="Copy & Move">
    public interface ConflictResolver {
        public String resolveFilename(String desired_filename);
    }

    public static String copy(String from_file, String dest_file, ConflictResolver method) throws IOException {
        java.io.File fromFile = new java.io.File(from_file);
        java.io.File destFile = new java.io.File(dest_file);

        if (destFile.isDirectory()) {
            destFile = new java.io.File(destFile, fromFile.getName());
            dest_file = destFile.getCanonicalPath();
        }

        dest_file = method.resolveFilename(dest_file);
        copy(fromFile, new java.io.File(dest_file));
        return dest_file;
    }
    
    private static void copy(java.io.File fromFile, java.io.File destFile) {
        try (InputStream in = new FileInputStream(fromFile);
            OutputStream out = new FileOutputStream(destFile)) 
        {            
            StreamUtils.copyBytes(in, out);
        } catch (FileNotFoundException ex) {
            Logger.printf("File not found. %s", ex);
            destFile.delete();
        } catch (Exception ex) {
            Logger.printf("Unable to copy file. %s", ex);
            destFile.delete();
        }
    }

    public static void copy(String from_file, String dest_file, boolean overwrite) throws Exception {
        java.io.File fromFile = new java.io.File(from_file);
        java.io.File destFile = new java.io.File(dest_file);

        //<editor-fold defaultstate="collapsed" desc="Verification">
        if (destFile.isDirectory())
            destFile = new java.io.File(destFile, fromFile.getName());

        //validations
        checkFileValidForRead(fromFile);

        if (destFile.exists() && !destFile.canWrite())
            throw new Exception("No write rights to destination file.");

        if (destFile.exists() && !overwrite) {
            throw new OverwriteException("Destination file allready exist.");
        }

        if (PathUtils.areSameGlobaly(from_file, dest_file)) {
            Logger.debug("Source and destination filepaths are same.");
            return;
        }
        //</editor-fold>

        copy(fromFile, destFile);
    }
    //</editor-fold>

    public static String resolveFilename(String desired_filename) {
        while (File.exist(desired_filename)) {
            desired_filename = alterFilename(desired_filename);
        }
        return desired_filename;
    }

    private static final Pattern indexSuffixPattern = Pattern.compile("\\[([0-9]+)\\]$");
    public static String alterFilename(String desired_filename) {
        FilePath filepath = FilePath.parse(desired_filename);

        int index = 0;
        Matcher m = indexSuffixPattern.matcher(filepath.getName());
        if (m.find()) {
            String strIndex = m.group(1);
            if (StringUtils.isNotEmpty(strIndex)) {
                index = Integer.valueOf(strIndex);
                filepath.setName(m.replaceFirst(format("[%d]", index + 1)));
            }
        } else {
            filepath.setName(filepath.getName() + format("[%d]", index));
        }
        return filepath.toString();
    }

    public static String removeFilenameIndex(String filename) {
        FilePath filepath = FilePath.parse(filename);
        Matcher m = indexSuffixPattern.matcher(filepath.getName());
        if (m.find()) {
            filepath.setName(m.replaceFirst(""));
        }
        return filepath.toString();
    }
    
    public static boolean areSameSize(String file0, String file1) {
        java.io.File file0File = new java.io.File(file0);
        java.io.File file1File = new java.io.File(file1);
        return (file0File.length() == file1File.length());
    }

    //<editor-fold defaultstate="collapsed" desc="Read & Write Accessors">
    //<editor-fold desc="Read">
    public static byte[] readBytes(String filepath, int length) throws Exception {
        return readBytes(new java.io.File(filepath), length);
    }

    public static byte[] readBytes(java.io.File file, int length) throws Exception {
        checkFileValidForRead(file);
        try (InputStream input = new FileInputStream(file)) {
            // Create the byte array to hold the data
            long content_length = Math.min(file.length(), length);

            byte[] bytes = StreamUtils.readBytes(input, content_length);
            // Ensure all the bytes have been read in
            if (bytes.length < content_length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            return bytes;
        } catch (Exception e) {
            throw new Exception("Unable to read bytes.", e);
        }
    }

    public static byte[] readAllBytes(java.io.File file) throws Exception {
        checkFileValidForRead(file);
        try (InputStream input = new FileInputStream(file)) {
            // Create the byte array to hold the data
            byte[] bytes = StreamUtils.readBytes(input, file.length());
            // Ensure all the bytes have been read in
            if (bytes.length < file.length()) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            return bytes;
        } catch (Exception e) {
            throw new Exception("Unable to read bytes", e);
        }
    }

    public static byte[] readAllBytes(String path) throws Exception {
        java.io.File fromFile = new java.io.File(path);
        return readAllBytes(fromFile);
    }

    public static List<String> readAllLines(String path) throws Exception {
        return readAllLines(path, FILE_ENCODING);
    }

    public static List<String> readAllLines(String path, String encoding) throws Exception {
        java.io.File fromFile = new java.io.File(path);
        return readAllLines(fromFile, encoding);
    }

    public static List<String> readAllLines(java.io.File path, String encoding) throws Exception {
        checkFileValidForRead(path);

        List<String> lines = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(path);
                InputStreamReader isReader = new InputStreamReader(fis, encoding);
                BufferedReader reader = new BufferedReader(isReader))
        {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            Logger.print("File Not Found.");
            throw e;
        } catch (IOException e) {
            Logger.print("Read-Write Error.");
            throw e;
        }
        return lines;
    }

    public static String readAllText(String path) throws Exception {
        return readAllText(path, FILE_ENCODING);
    }

    public static String readAllText(String path, String encoding) throws Exception {
        byte[] data = readAllBytes(path);
        return new String(data, Charset.forName(encoding));
    }
    //</editor-fold>
    //<editor-fold desc="Write">
    public static void writeAllBytes(byte[] bytes, String path, boolean overwrite) throws Exception {
        java.io.File destFile = new java.io.File(path);

        //<editor-fold defaultstate="collapsed" desc="Validations">
        if (destFile.isDirectory())
            throw new Exception("Destination is directory.");
        
        if (destFile.exists() && !overwrite)
            throw new Exception("Destination file exist.");
        
        if (destFile.exists() && !destFile.canWrite())
            throw new Exception("No write rights to destination file.");
        //</editor-fold>

        try (FileOutputStream out = new FileOutputStream(destFile)) {
            out.write(bytes);
            out.flush();
        } catch (IOException ex) {
            Logger.print("Unable to write file. " + ex);
        }
    }

    public static void writeAllLines(Collection<String> lines, String path, boolean overwrite) throws Exception {
        writeAllLines(lines, path, FILE_ENCODING, overwrite);
    }

    public static void writeAllLines(Collection<String> lines, String path, String encoding, boolean overwrite) throws Exception {
        if (StringUtils.isNullOrEmpty(path)) throw new IllegalArgumentException("Destination file is null.");

        java.io.File destFile = new java.io.File(path);

        //<editor-fold defaultstate="collapsed" desc="Validations">
        if (destFile.isDirectory())
            throw new IllegalArgumentException("Destination is directory.");
        
        if (destFile.exists() && !destFile.canWrite())
            throw new IllegalArgumentException("No write rights to dest file.");
        
        if (destFile.exists() && !overwrite)
            throw new IllegalArgumentException("Destination file exist.");
        //</editor-fold>

        try (FileOutputStream fos = new FileOutputStream(destFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
                BufferedWriter writer = new BufferedWriter(osw))
        {
            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                writer.write(line);
                if (iterator.hasNext()) {
                    writer.write(END_LINE);
                }
            }
            writer.flush();
        } catch (IOException e) {
            Logger.print("File Write Error.");
            throw e;
        }
    }

    public static void writeAllText(String text, String path, boolean overwrite) throws Exception {
        writeAllText(text, path, FILE_ENCODING, overwrite);
    }

    public static void writeAllText(String text, String path, String encoding, boolean overwrite) throws Exception {
        List<String> lines = new ArrayList<>();
        lines.add(text);
        writeAllLines(lines, path, encoding, overwrite);
    }

    public static void appendAllText(String text, String path) throws Exception {
        List<String> lines = new ArrayList<>();
        lines.add(text);
        appendAllLines(lines, path);
    }

    public static void appendAllLines(String[] lines, String path) throws Exception {
        appendAllLines(Arrays.asList(lines), path, FILE_ENCODING);
    }

    public static void appendAllLines(Collection<String> lines, String path) throws Exception {
        appendAllLines(lines, path, FILE_ENCODING);
    }

    public static void appendAllLines(Collection<String> lines, String path, String encoding) throws Exception {
        java.io.File destFile = new java.io.File(path);

        //<editor-fold defaultstate="collapsed" desc="Validations">
        if (destFile.isDirectory())
            throw new IllegalArgumentException("Destination is directory.");
        
        if (destFile.exists() && !destFile.canWrite())
            throw new IllegalArgumentException("No write rights to dest file.");
        
        //if file doesnt exists, then create it
        if(!destFile.exists())
            destFile.createNewFile();
        //</editor-fold>

        try (FileOutputStream fileOutputStream = new FileOutputStream(destFile, true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, encoding);
                BufferedWriter bufferWritter = new BufferedWriter(outputStreamWriter)) 
        {
            for (String line : lines) {
                bufferWritter.write(line);
                bufferWritter.write(END_LINE);
            }
            bufferWritter.flush();
        } catch(IOException e) {
            Logger.print("File Append Error.");
            throw e;
        }
    }
    //</editor-fold>
    //</editor-fold>

    public static class OverwriteConflictResolver implements ConflictResolver {
        @Override
        public String resolveFilename(String desired_filename) {
            return desired_filename;
        }
    }

    public static class RenameConflictResolver implements ConflictResolver {
        @Override
        public String resolveFilename(String desired_filename) {
            return File.resolveFilename(desired_filename);
        }
    }

    private static void checkFileValidForRead(java.io.File file) throws Exception {
        //validations
        if (!file.exists())
            throw new FileNotFoundException("File not found: " + file);

        if (!file.isFile())
            throw new Exception("Not file specified: " + file);

        if (!file.canRead())
            throw new Exception("No read rights to source file: " + file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            throw new Exception("File too large: " + file);
        }
    }

    private static void checkFileValidForWrite(java.io.File file) throws IOException {
        if (file.isDirectory())
            throw new IOException("Destination is directory.");

        if (file.exists() && ! file.canWrite())
            throw new IOException("No write rights to dest file.");

        //if file doesnt exists, then create it
        if ( ! file.exists())
            file.createNewFile();
    }
}