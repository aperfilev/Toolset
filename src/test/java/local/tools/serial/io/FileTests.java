package local.tools.serial.io;

import local.tools.io.File;
import local.tools.logs.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static local.tools.logs.Logger.print;

public class FileTests {

    @BeforeEach
    void setUp() {
        try {
            File.writeAllText("1,2,3,4,5,6,7,8,9", "test.txt", true);
            File.writeAllBytes(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, "test.jpg", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            File.delete("test.txt");
            File.delete("test.jpg");
        } catch (Exception ignore) {
        }
    }

    @Test
    public void test_SameSize() {
        String file0 = "test.jpg";
        String file1 = "./test.jpg";
        String file2 = "test.txt";
        Logger.print("SameSize: " + File.areSameSize(file0, file1));
        Logger.print("SameSize: " + File.areSameSize(file1, file2));
    }

    @Test
    public void test_ReadBytes() throws Exception {
        java.io.File file = new java.io.File("test.jpg");
        byte[] data = File.readBytes(file, 32);
        Logger.debug(new String(data, "cp1251"));
    }

    @Test
    public void test_appendAllLines() throws Exception {
        List<String> lines = new ArrayList<>();
        lines.add("Line 1");
        lines.add("Line 2");
        lines.add("Line 3");
        File.writeAllLines(lines, "appendFileTest.txt", true);
        lines.clear();
        lines.add("Line 4");
        lines.add("Line 5");
        lines.add("Line 6");
        File.appendAllLines(lines, "appendFileTest.txt");
        File.appendAllText("Line 7\nLine 8\nLine 9", "appendFileTest.txt");
    }

    @Test
    public void test_ResolveFilename() {
        String filepath1 = "./test[0].txt";
        String filepath2 = "./test[2].txt";
        String resolved_name1 = File.resolveFilename(filepath1);
        String resolved_name2 = File.resolveFilename(filepath2);
        print(filepath1 + " -> " + resolved_name1);
        print(filepath2 + " -> " + resolved_name2);
    }

    @Test
    public void test_lineIterator() throws Exception {
        Iterator<String> lines = File.getLineIterator("test.txt");
        while (lines.hasNext()) {
            Logger.println(lines.next());
        }
    }

    @Test
    public void test_AlterFilename() {
        String filepath1 = "./test[0].txt";
        filepath1 = File.alterFilename(filepath1);
        Logger.print(filepath1);
        filepath1 = File.alterFilename(filepath1);
        Logger.print(filepath1);
        filepath1 = File.alterFilename(filepath1);
        Logger.print(filepath1);
    }

    @Test
    public void test_ZipWriter() throws Exception {
        String filepath = "test.txt";
        File.LineAppender appender = File.getZipLineAppender(filepath + ".zip");
        for (String line : File.getLineIterator(filepath)) {
            appender.append(line);
        }
        appender.close();
    }

    @Test
    public void test_ZipReader() throws Exception {
        String filepath = "test.txt.zip";
        for (String line : File.getZipLineIterator(filepath)) {
            Logger.debug(line);
        }
    }
}
