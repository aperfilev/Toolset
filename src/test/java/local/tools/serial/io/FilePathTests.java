package local.tools.serial.io;

import local.tools.io.FilePath;
import org.junit.jupiter.api.Test;

import static local.tools.io.FilePath.parse;
import static local.tools.logs.Logger.print;

public class FilePathTests {

    @Test
    public void testSplitPath() {
        String[] filepaths = {"C:\\Program Files (x86)\\Main\\bin\\file.exe", "C:\\Program Files (x86)\\Main\\bin\\file."};

        for (String filepath : filepaths) {
            FilePath item = parse(filepath);
            print(item.filepath());
            print(item.getPath());
            print(item.filename());
            print(item.getName());
            print(item.getExt());
        }
    }

    @Test
    public void testGetFileFromURL() {
        String url_path = "http://jaist.host.net/project/jacob-project/jacob-project/1.17/jacob-1.17.zip?token=jdbfjhg6ejg6jehgj&id=45lh34";
        FilePath item = FilePath.parseURL(url_path);
        print(item.getPath());
        print(item.getName());
        print(item.getExt());
    }
}
