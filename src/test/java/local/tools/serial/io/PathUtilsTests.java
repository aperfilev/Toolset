package local.tools.serial.io;

import local.tools.io.FilePath;
import local.tools.io.PathUtils;
import org.junit.jupiter.api.Test;

import static local.tools.logs.Logger.debug;
import static local.tools.logs.Logger.print;

public class PathUtilsTests {

    @Test
    public void test_EncodeUrlTail() {
        String url = "http://www.host.com/ethrjy/aseres/serser.php?q='твдвл оцуоц цр'&ejekj";
        print(url);
        String old = url.substring(0, url.indexOf("?"));
        url = PathUtils.encodeURLTail(url);
        String mod = url.substring(0, url.indexOf("?"));
        print(url);
        assert old.equals(mod);
        print("OK.");
    }

    @Test
    public void testSame() {
        String path0 = "./test.jpg";
        String path1 = "test.jpg";
        debug(path0);
        debug(path1);
        assert PathUtils.areSameGlobaly(path0, path1);
        print("OK.");
    }

    @Test
    public void testSplitPath() {
        String[] filepaths = {"C:\\Program Files (x86)\\Main\\bin\\file.exe", "C:\\Program Files (x86)\\Main\\bin\\file."};

        for (String filepath : filepaths) {
            FilePath item = new FilePath(filepath);
            print(item.filepath());
            print(item.getPath());
            print(item.filename());
            print(item.getName());
            print(item.getExt());
        }
    }

    @Test
    public void testGetFileFromURL() {
        String url_path = "http://www.host.net/project/project/jacob-project/1.17/jacob-1.17.zip?token=jdbfjhg6ejg6jehgj&id=45lh34";

        FilePath item = FilePath.parseURL(url_path);
        print(item.getPath());
        print(item.getName());
        print(item.getExt());
    }

    @Test
    public void test_CombinePath() {
        String host_url = "http://host.ru/";
        String path_url = "/sdgsdfg/sg/dfgd/fdfs?dfsf&dsf";
        String result_url = PathUtils.combinePath(host_url, path_url);
        assert result_url.equals("http://host.ru/sdgsdfg/sg/dfgd/fdfs?dfsf&dsf");
        print("OK.");
    }
}
