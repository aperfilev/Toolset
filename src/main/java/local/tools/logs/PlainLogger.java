package local.tools.logs;

import java.io.PrintStream;

/**
 * Created Jul 24, 2018
 * @author alex9323
 */
class PlainLogger implements ILogger {
    
    private static final PlainLogger instance = new PlainLogger();
    private PrintStream stream = System.out;

    private PlainLogger() {}    
    
    public static PlainLogger getInstance() {
        return instance;
    }

    /**
     * Defines output PrintStream
     * @param stream 
     */
    @Override
    public void setOutputStream(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void flush() {
        this.stream.flush();
    }

    /**
     * Log message without line-ending 
     * @param msg - message
     */
    @Override
    public void print(Object msg) {
        this.stream.print(msg);
        this.stream.flush();
    }
    
    /**
     * Log message with line-ending 
     * @param msg - message
     */
    @Override
    public void println(Object msg) {
        this.stream.println(String.valueOf(msg));
        this.stream.flush();
    }
}
