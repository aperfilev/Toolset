package local.tools.time;

public final class StopWatch {
    private volatile boolean m_started = false;
    
    private long time_interval = 0L;
    private long start_point = 0L;

    public static StopWatch Start() {
        StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }
    
    /**
     * Put starting point of time interval
     */
    public void start() {
        if ( ! m_started) {
            m_started = true;
            start_point = System.currentTimeMillis();
        }
    }

    /**
     * Put ending point of time interval
     */
    public void stop() {
        if (m_started) {
            long end_point = System.currentTimeMillis();
            time_interval += (end_point - start_point);
            m_started = false;
        }
    }

    /**
     * Resets time interval
     */
    public void reset() {
        time_interval = 0L;
    }

    /**
     * Returns time between start call and stop call
     * @return
     */
    public long peek() {
        long end_point = (start_point != 0L ? System.currentTimeMillis() : 0L);
        return (time_interval + (m_started ? (end_point - start_point) : 0L));
    }

    @Override
    public String toString() {
        return String.format("%s ms.", peek());
    }
}
