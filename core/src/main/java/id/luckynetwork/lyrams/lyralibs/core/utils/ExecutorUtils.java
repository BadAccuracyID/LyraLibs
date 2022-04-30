package id.luckynetwork.lyrams.lyralibs.core.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class ExecutorUtils {

    @Getter
    private final int poolSize = Runtime.getRuntime().availableProcessors() + 2;

    public ExecutorService getFixedExecutorService(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize,
                15L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                factory);

        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public ExecutorService getUnlimitedExecutorService(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                15L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                factory);

        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public ExecutorService getSingleThreadedExecutor(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 1,
                15L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                factory);

        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public ExecutorService getWorkStealingExecutor(String name) {
        LuckyForkJoinWorkerThreadFactory factory = new LuckyForkJoinWorkerThreadFactory();
        ForkJoinPool pool = new ForkJoinPool(poolSize, factory, null, true);
        // forces the pool to create a work thread so that we can rename it
        pool.execute(() -> {
        });

        factory.rename(name);
        return pool;
    }

    /**
     * The custom ForkJoinWorker thread factory
     */
    static class LuckyForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {

        @Setter
        private LuckyForkJoinWorkerThread workerThread = null;

        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            this.setWorkerThread(new LuckyForkJoinWorkerThread(pool));
            return this.workerThread;
        }

        public void rename(String name) {
            Objects.requireNonNull(this.workerThread, "The worker thread is null!");

            int activeThreadCount = this.workerThread.getPool().getActiveThreadCount();
            int poolSize = this.workerThread.getPool().getPoolSize();

            String finalName = String.format(name + "-", (poolSize + 1)) + (activeThreadCount + 1);
            this.workerThread.rename(finalName);
        }

        /**
         * Default ForkJoinWorkerThreadFactory implementation; creates a
         * new ForkJoinWorkerThread.
         */
        static class LuckyForkJoinWorkerThread extends ForkJoinWorkerThread {

            LuckyForkJoinWorkerThread(ForkJoinPool pool) {
                super(pool);
            }

            public void rename(String name) {
                super.setName(name);
            }

        }
    }

    /**
     * The custom thread factory
     */
    static class LuckyThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        LuckyThreadFactory(String name) {
            SecurityManager manager = System.getSecurityManager();
            group = (manager != null) ? manager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = String.format(name + "-", poolNumber.getAndIncrement());
        }

        public Thread newThread(@NotNull Runnable runnable) {
            String name = namePrefix + threadNumber.getAndIncrement();
            Thread thread = new Thread(group, runnable, name, 0);
            if (thread.isDaemon())
                thread.setDaemon(false);
            if (thread.getPriority() != Thread.NORM_PRIORITY)
                thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }
}
