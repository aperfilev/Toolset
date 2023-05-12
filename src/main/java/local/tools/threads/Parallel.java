package local.tools.threads;

import local.tools.code.Verify;
import local.tools.logs.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Represents methods to ease scheduling parallel processing
 */
public final class Parallel {

    //<editor-fold defaultstate="collapsed" desc="Internal Works">
    public static abstract class Method<Type> {

        public abstract void call(Type item) throws Exception;

        public void onError(Type item) throws Exception {}

        public void onError(Exception e, Type item) throws Exception {
            onError(item);
        }

        public void onFinish(Type item) throws Exception {}
    }

    public static final class BreakException extends Exception {}

    private final static class StopHandle {
        private final List<? extends BaseWorkThread> workThreads;

        StopHandle(List<? extends BaseWorkThread> targets) {
            this.workThreads = targets;
        }

        public void terminateAll() {
            for (BaseWorkThread thread : workThreads) {
                thread.Stop();
            }
        }
    }

    private static abstract class BaseWorkThread extends Thread {
        private final StopHandle stopHandle;
        protected volatile boolean stop = false;

        protected BaseWorkThread(StopHandle handle) {
            this.stopHandle = handle;
        }

        public void stopAllThreads() {
            stopHandle.terminateAll();
        }

        public final void Stop() {
            this.stop = true;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Behaviors">
    private static final class SharedItemsWorker<Type> extends BaseWorkThread {
        private final Iterator<Type> iterator;
        private final Method method;
        private final int batchSize;
        private final List<Type> batchItems;

        private SharedItemsWorker(Method method, Iterator<Type> items, int batchSize, StopHandle t) {
            super(t);
            this.method = method;
            this.iterator = items;
            this.batchSize = batchSize;
            this.batchItems = new ArrayList<>(batchSize);
        }

        @Override
        public void run() {
            boolean hasNext = true;
            while (!stop && hasNext) {
                // take batch of items
                synchronized (iterator) {
                    for (int i = 0; iterator.hasNext() && i < batchSize; ++i) {
                        batchItems.add(iterator.next());
                    }
                    hasNext = iterator.hasNext();
                }

                // process batch items
                for (Type item : this.batchItems) {
                    try {
                        try {
                            method.call(item);
                        } catch (BreakException e) {
                            stopAllThreads();
                            break;
                        } catch (Exception e) {
                            method.onError(e, item);
                            continue;
                        }
                        method.onFinish(item);
                    } catch (Exception e) {
                        Logger.error(e);
                    }
                }
                batchItems.clear();
            }
        }
    }
    //</editor-fold>

    /**
     * Creates a maxThreads of threads which use synchronized removing items mechanism.
     * Better for large lists. Uses no additional memory.
     *
     * @param <Type> - element type of given Iterable collection.
     * @param items - Iterable collection of elements.
     * @param maxThreads - maximum number of threads to be used for parallel processing.
     * @param batchSize - batch size to be used for each portion processing for each thread.
     * @param method - Runnable method object with given processing functions.
     */
    public static <Type> void For(Iterable<Type> items, int maxThreads, int batchSize, final Method<Type> method) {
        Verify.checkArgument(maxThreads > 1, "Given zero or negative number of threads.");
        Verify.checkArgument(batchSize > 1, "Given zero or negative batchSize.");
        Verify.checkNotNull(items, "Given null Iterable.");

        //Init all threads
        List<BaseWorkThread> workers = new ArrayList<>(maxThreads);
        Iterator<Type> shared_iterator = items.iterator();
        StopHandle stopHandle = new StopHandle(workers);

        for (int i = 0; i < maxThreads; ++i) {
            if (Thread.currentThread().isInterrupted()) return;
            workers.add(new SharedItemsWorker<>(method, shared_iterator, batchSize, stopHandle));
        }

        //Start all threads
        for (Thread work : workers) {
            work.start();
        }

        // WaitForAll threads
        for (Thread work : workers) {
            try {
                if (!work.isInterrupted()) {
                    work.join();
                }
            } catch (InterruptedException e) {
                Logger.error(e.getMessage());
                break;
            }
        }
    }

    public static void start(Runnable... runs) {
        List<Thread> threads = new ArrayList<>(runs.length);
        //Init all threads
        for (Runnable run : runs) {
            threads.add(new Thread(run));
        }

        //Start all threads
        for (Thread thread : threads) {
            thread.start();
        }

        // WaitForAll threads
        for (Thread thread : threads) {
            while (thread.isAlive() && !thread.isInterrupted()) {
                try {
                    thread.join();
                } catch (InterruptedException ignore) {}
            }
        }
    }
    
    public static void fork(Runnable... runs) {
        List<Thread> threads = new ArrayList<>(runs.length);
        //Init all threads
        for (Runnable run : runs) {
            threads.add(new Thread(run));
        }

        //Start all threads
        for (Thread thread : threads) {
            thread.setDaemon(true);
            thread.start();
        }
    }
    
    public static void fork(Runnable method, Callable callback) {
        Thread thread = new Thread(() -> {
            method.run();
            try {
                callback.call();
            } catch (Exception ex) {
                Logger.error(ex.getMessage());
            }
        });
        thread.start();
    }
}
