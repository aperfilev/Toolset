package local.tools.logs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import local.tools.code.Throwables;
import local.tools.utils.MultiOutputStream;

//<editor-fold defaultstate="collapsed" desc="All Logger Methods Export">
import static local.tools.logs.Logger.debug;
import static local.tools.logs.Logger.error;
import static local.tools.logs.Logger.info;
import static local.tools.logs.Logger.log;
import static local.tools.logs.Logger.warning;
//</editor-fold>

/**
 * Created Jul 24, 2018
 * @author alex9323
 */
public final class Logger {
    
    public static void main(String[] args) {
//        Logger.print("Hello\n");
//        Logger.printf("Hello '%s'\n", OS.USER_NAME);
//        Logger.println("Hello");
//        Logger.printfln("Hello '%s'", OS.USER_NAME);
//        Logger.debug("This is debug message");
//        Logger.debug("This is debug message for '%s'", OS.USER_NAME);
//        Logger.debug(new Exception("Test exception"));
//        Logger.warning("This is warning message");
//        Logger.warning("This is warning message for '%s'", OS.USER_NAME);
//        Logger.error("This is error message");
//        Logger.error("This is error message for '%s'", OS.USER_NAME);
//        Logger.error(new Exception("Test exception"));
//        Logger.log("INFO", "This is log message");
//        Logger.log("INFO", "This is log message for '%s'", OS.USER_NAME);
//        Logger.printfln("Hello world\t%d\t%d", 2500, 25100);
//        Logger.printfln("Hellooo\t%d\t%d", 250, 360100);
    }
    
    private Logger() {
    }
    
    private static ILogger instance;
    private static boolean isDebugMode = true;
    
    private static final SimpleDateFormat filename_date_format  = new SimpleDateFormat("yyyy-MM-dd hhmmaa");
    private static final SimpleDateFormat date_format           = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
    
    static {
        if (java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp")) {
            setDebugMode(true);
        }
        setLogger(PlainLogger.getInstance());
    }
    
    private static FileOutputStream targetOutputStream;
    
    /**
     * Initializes Logger class
     * @param logger
     */
    public static void setLogger(ILogger logger) {
        instance = logger;
        
        try {
            targetOutputStream = new FileOutputStream(String.format("log_%s.txt", getFilestamp()));
            setOutputStream(new PrintStream(
                                new MultiOutputStream(System.out, targetOutputStream)));
        } catch (FileNotFoundException e) {
            error(getBasicInfo(e));
        }
    }

    public static void setDebugMode(boolean enabled) {
        Logger.isDebugMode = enabled;
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
    
    public static void setOutputStream(PrintStream stream) {
        instance.setOutputStream(stream);
    }
    
    public static void flush() {
        instance.flush();
    }
    
    /**
     * Log message with line-ending.
     * @param msg
     */
    public static void print(Object msg) {
        instance.println(msg);
    }
    
    /**
     * Log message with formatting, without line-ending 
     * @param format
     * @param args
     */
    public static void printf(String format, Object... args) {
        instance.print(String.format(format, args));
    }
    
    /**
     * Log message with line-ending 
     * @param msg
     */
    public static void println(Object msg) {
        instance.println(msg);
    }
    
    /**
     * Log message with formatting, with line-ending 
     * @param format
     * @param args
     */
    public static void printfln(String format, Object... args) {
        instance.println(String.format(format, args));
    }
                
    /**
     * Method with custom level.
     * @param level
     * @param msg 
     */
    public static void log(String level, Object msg) {
        println(createLabel(level) + String.valueOf(msg));
    }
    
    public static void log(String level, String format, Object... args) {
        println(createLabel(level) + String.format(format, args));
    }
    
    /**
     * Method with custom level.
     * @param msg
     */
    public static void info(Object msg) {
        println(createLabel("INFO") + String.valueOf(msg));
    }
    
    public static void info(String format, Object... args) {
        println(createLabel("INFO") + String.format(format, args));
    }
    
    /**
     * Method with DEBUG label
     * Works only in debug mode
     * @param msg 
     */
    public static void debug(Object msg) {
        if (isDebugMode) {
            if (msg instanceof Throwable)
                msg = getBasicInfo((Throwable) msg) + " - " + Throwables.getStackTraceAsString((Throwable) msg);
        
            println(createLabel("DEBUG") + String.valueOf(msg));
        }
    }

    public static void debug(String format, Object... args) {
        if (isDebugMode) println(createLabel("DEBUG") + String.format(format, args));
    }
    
    /**
     * Method with WARNING label
     * @param msg 
     */
    public static void warning(Object msg) {
        println(createLabel("WARNING") + String.valueOf(msg));
    }
    
    public static void warning(String format, Object... args) {
        println(createLabel("WARNING") + String.format(format, args));
    }
    
    /**
     * Method with ERROR label
     * @param msg 
     */
    public static void error(Object msg) {
        if (msg instanceof Throwable)
            msg = getBasicInfo((Throwable) msg);
        
        println(createLabel("ERROR") + String.valueOf(msg));
    }
    
    public static void error(String format, Object... args) {
        println(createLabel("ERROR") + String.format(format, args));
    }
    
    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    private static final String getBasicInfo(Throwable e) {
        return e.getClass().getSimpleName() + ": " + e.getMessage();
    }
    
    private static final String getFilestamp() {
        Date now = new Date(System.currentTimeMillis());
        synchronized (filename_date_format) {
            return filename_date_format.format(now);
        }
    }

    private static final String getTimestamp() {
        Date now = new Date(System.currentTimeMillis());
        synchronized (date_format) {
            return date_format.format(now);
        }
    }
    
    private static final String getCodeReference(int stackdepth) {
        StackTraceElement st = Thread.currentThread().getStackTrace()[stackdepth + 2];
        return String.format("%s.%s(%s:%d)", st.getClassName(), st.getMethodName(), st.getFileName(), st.getLineNumber());
    }
    
    private static final String createLabel(final String level) {
        return String.format("[%s][%s][%s] ", getTimestamp(), getCodeReference(2), level);
    }
    //</editor-fold>
}
