package id.luckynetwork.lyrams.lyralibs.core.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class ExecutorUtils {

    @Getter
    private final int poolSize = Runtime.getRuntime().availableProcessors() + 2;

    /**
     * > It creates a fixed size thread pool with a custom thread factory that sets the thread name and a custom uncaught
     * exception handler that logs the exception
     *
     * @param name The name of the thread pool.
     * @return A fixed thread pool executor service.
     */
    public ExecutorService getFixedExecutorService(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize,
                15L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                factory);

        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * > It creates a fixed size thread pool with a custom thread factory that sets the thread name and a custom uncaught
     * exception handler that logs the exception
     *
     * @param name The name of the thread pool.
     * @param poolSize The size of the thread pool.
     * @return A fixed thread pool executor service.
     */
    public ExecutorService getFixedExecutorService(String name, int poolSize) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize, poolSize,
                15L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                factory);

        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * > It creates a new `ScheduledExecutorService` with a fixed pool size, and a `ThreadFactory` that creates threads
     * with a name format of `name` and a `LuckyThreadFactory` that creates threads with a name format of `name`
     *
     * @param name The name of the thread pool.
     * @return A ScheduledExecutorService
     */
    public ScheduledExecutorService getFixedScheduledExecutorService(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(poolSize, factory);

        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * > It creates a new `ScheduledExecutorService` with a fixed pool size, and a `ThreadFactory` that creates threads
     * with a name format of `name` and a `LuckyThreadFactory` that creates threads with a name format of `name`
     *
     * @param name The name of the thread pool.
     * @param poolSize The size of the thread pool.
     * @return A ScheduledExecutorService
     */
    public ScheduledExecutorService getFixedScheduledExecutorService(String name, int poolSize) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(poolSize, factory);

        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * Create a thread pool executor with an unlimited number of threads, and a thread factory that creates threads with
     * the given name.
     *
     * @param name The name of the thread pool.
     * @return An ExecutorService
     */
    public ExecutorService getUnlimitedExecutorService(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                15L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                factory);

        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * Create a scheduled executor service with an unlimited number of threads, and allow the threads to time out.
     *
     * @param name The name of the thread pool.
     * @return A ScheduledExecutorService
     */
    public ScheduledExecutorService getUnlimitedScheduledExecutorService(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(Integer.MAX_VALUE, factory);

        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * Create a single threaded executor with a thread factory that creates threads with the given name and a custom
     * uncaught exception handler.
     *
     * @param name The name of the thread pool.
     * @return A single threaded executor service.
     */
    public ExecutorService getSingleThreadedExecutor(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 1,
                15L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                factory);

        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * Create a single threaded scheduled executor with a custom thread factory that sets the thread name and uses
     * LuckyThreadFactory to set the thread priority.
     *
     * @param name The name of the thread.
     * @return A single threaded scheduled executor service.
     */
    public ScheduledExecutorService getSingleThreadedScheduledExecutor(String name) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(name).setThreadFactory(new LuckyThreadFactory(name)).build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, factory);

        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * It creates a ForkJoinPool with a custom thread factory that renames the threads
     *
     * @param name The name of the thread pool.
     * @return A work stealing executor service.
     */
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
    private static class LuckyForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {

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
    private static class LuckyThreadFactory implements ThreadFactory {
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
