package local.tools.serial.time;

import local.tools.logs.Logger;
import local.tools.time.StopWatch;
import org.junit.jupiter.api.Test;

public class StopWatchTest {

    @Test
    void testStopWatch() {
        StopWatch sw1 = new StopWatch();
        StopWatch sw2 = new StopWatch();

        Logger.print("Wait for 1 seconds.");
        sw1.start();
        // Some time cosuming calculations
        for (int i = 0; i < 10; ++i) {
            sw2.start();
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                Logger.error("Me having trouble sleeping");
            }
            sw2.stop();
        }
        sw1.stop();

        Logger.print("Outer time interval : " + sw1.peek());
        Logger.print("Inner time interval : " + sw2.peek());
    }
}
