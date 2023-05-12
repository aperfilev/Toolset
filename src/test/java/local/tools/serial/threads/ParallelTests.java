package local.tools.serial.threads;

import local.tools.encode.Hash;
import local.tools.io.File;
import local.tools.logs.Logger;
import local.tools.threads.Parallel;
import local.tools.time.StopWatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelTests {

    @BeforeEach
    void setUp() {
        byte[] data = new byte[1000000]; // 1Mb in size
        Random random = new Random();
        random.nextBytes(data);
        try {
            File.writeAllBytes(data, "test.jpg", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        File.delete("test.jpg");
    }

    @Test
    public void test_ParallelBatchRun() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            String item = String.valueOf(i);
            items.add(item);
        }

        Parallel.For(items, 5, 100, new Parallel.Method<>() {
            @Override
            public void call(String item) throws Exception {
                Logger.printfln("Thread %s item: %s", Thread.currentThread().getName(), item);
            }
        });
        Logger.debug("Left items: %d", items.size());
    }

    @Test
    public void test_ParallelStart() throws Exception {
        {
            final StopWatch sw0 = new StopWatch();
            final StopWatch sw1 = new StopWatch();
            final StopWatch sw2 = new StopWatch();
            class Record {
                String short_md5;
                String total_md5;
            }
            final Record rec = new Record();
            final byte[] data = File.readAllBytes("test.jpg");
            sw0.start();
            Parallel.start(() -> {
                sw1.start();
                try {
                    final byte[] short_block = Arrays.copyOf(data, 1000);
                    rec.short_md5 = Hash.calcMD5(short_block);
                } catch (Exception e) {
                    Logger.error("Unable to calc block md5 '%s'", e.getMessage());
                } finally {
                    sw1.stop();
                }
            }, () -> {
                sw2.start();
                try {
                    rec.total_md5 = Hash.calcMD5(data);
                } catch (Exception e) {
                    Logger.error("Unable to calc md5 '%s'", e.getMessage());
                } finally {
                    sw2.stop();
                }
            });
            sw0.stop();
            Logger.debug("Short block: '%s'", rec.short_md5);
            Logger.debug("Total md5: '%s'", rec.total_md5);
            Logger.debug("Short block time: %s", sw1);
            Logger.debug("Total md5 time: %s", sw2);
            Logger.debug("Total time: %s", sw0);
        }
        {
            final StopWatch sw0 = new StopWatch();
            class Record {
                String short_md5;
                String total_md5;
            }
            final Record rec = new Record();
            final byte[] data = File.readAllBytes("test.jpg");
            sw0.start();
            final byte[] short_block = Arrays.copyOf(data, 1000);
            rec.short_md5 = Hash.calcMD5(short_block);
            rec.total_md5 = Hash.calcMD5(data);
            sw0.stop();
            Logger.debug("Short block: '%s'", rec.short_md5);
            Logger.debug("Total md5: '%s'", rec.total_md5);
            Logger.debug("Total time: %s", sw0);
        }
    }

    public class SquareCalculator {

        private ExecutorService executor = Executors.newSingleThreadExecutor();

        public Future<Integer> calculate(Integer input) {
            return executor.submit(() -> {
                Thread.sleep(1000);
                return input * input;
            });
        }
    }

    @Test
    public void test_FuturesExecution() throws InterruptedException, ExecutionException {
        Future<Integer> future = new SquareCalculator().calculate(10);

        while (!future.isDone()) {
            System.out.println("Calculating...");
            Thread.sleep(300);
        }

        Integer result = future.get();
        Logger.print(result);
    }
}
