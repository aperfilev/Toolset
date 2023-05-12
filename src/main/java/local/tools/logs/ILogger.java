package local.tools.logs;

import java.io.PrintStream;

public interface ILogger {
    
    /**
     * Defines output PrintStream
     * @param stream - output stream
     */
    public void setOutputStream(PrintStream stream);
    
    /**
     * Flashes buffers into stream.
     */
    public void flush();
    
    /**
     * Log message without line-ending 
     * @param msg - message
     */
    public void print(Object msg);
    
    /**
     * Log message with line-ending 
     * @param msg - message
     */
    public void println(Object msg);
}
