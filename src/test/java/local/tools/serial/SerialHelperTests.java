package local.tools.serial;

import local.tools.logs.Logger;
import local.tools.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

public class SerialHelperTests {

    @Test
    public void test_base64_ser_deser() throws Exception {
        File oFile = new File(".");
        SerialHelper<File> serialHelper = new SerialHelper<>();
        String data = serialHelper.SerializeInBase64(oFile);
        //File.writeAllText(oData, "file_base64.txt", true);

        //String iData = File.readAllText("file_base64.txt");
        File iFile = serialHelper.DeserializeFromBase64(data);
        Logger.print(StringUtils.join(Arrays.asList(iFile.list()), "\n"));
    }

    @Test
    public void test_ser_deser() throws Exception {
        File oFile = new File(".");
        SerialHelper<File> serialHelper = new SerialHelper<>();
        byte[] data = serialHelper.serialize(oFile);
        //File.writeAllBytes(oData, "file.txt", true);

        //byte[] iData = File.readAllBytes("file.txt");
        File iFile = serialHelper.deserialize(data);
        Logger.print(StringUtils.join(Arrays.asList(iFile.list()), "\n"));
    }
}
