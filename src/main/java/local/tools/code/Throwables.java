package local.tools.code;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Throwables {
    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
