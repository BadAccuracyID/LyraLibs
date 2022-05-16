package id.luckynetwork.lyrams.lyralibs.velocity.tasks;

import id.luckynetwork.lyrams.lyralibs.core.interfaces.Callable;
import id.luckynetwork.lyrams.lyralibs.core.interfaces.Returnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class LyraTasks {

    private static LyraTasks instance;

    private final ExecutorService executor;

    public LyraTasks(ExecutorService executor) {
        instance = this;
        this.executor = executor;
    }

    public static ExecutorService getExecutor() {
        return instance.executor;
    }

    /**
     * "Submit a task to the executor and return a Future object."
     *
     * The above function is a static method of the LyraTasks class. It returns a Future object. The Future object is a
     * Java class that represents the result of an asynchronous computation. The Future object is returned immediately, and
     * the task is executed asynchronously
     *
     * @param callable The task to be executed.
     * @return A Future object
     */
    public static Future<?> submit(Callable callable) {
        return LyraTasks.getExecutor()
                .submit(() -> {
                            try {
                                callable.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
    }

    /**
     * Submit a task to the executor to be executed after a delay.
     *
     * @param callable The task to be executed.
     * @param timeUnit The time unit to use for the delay.
     * @param delay The amount of time to wait before executing the task.
     * @return A Future object.
     */
    public static Future<?> submitLater(Callable callable, TimeUnit timeUnit, long delay) {
        return LyraTasks.getExecutor()
                .submit(() -> {
                            try {
                                timeUnit.sleep(delay);
    /**
     * Runs the given callable asynchronously and returns a CompletableFuture that completes when the callable completes.
     *
     * @param callable The callable to run.
     * @return A CompletableFuture that will run the callable asynchronously.
     */
                                callable.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
    }

    /**
     * Runs the given callable asynchronously and returns a CompletableFuture that completes when the callable completes.
     *
     * @param callable The callable to run.
     * @return A CompletableFuture that will run the callable asynchronously.
     */
    public CompletableFuture<Void> runAsync(Callable callable) {
        return CompletableFuture.runAsync(
                () -> {
                    try {
                        callable.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * Run the given callable asynchronously after the given delay.
     *
     * @param callable The callable to run.
     * @param timeUnit The time unit to use for the delay.
     * @param delay The amount of time to delay the execution of the task.
     * @return A CompletableFuture that will run the callable after the delay.
     */
    public static CompletableFuture<Void> runAsyncLater(Callable callable, TimeUnit timeUnit, long delay) {
        return CompletableFuture.runAsync(
                () -> {
                    try {
                        timeUnit.sleep(delay);
                        callable.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    /**
     * It takes a Returnable and returns a CompletableFuture that will return the value of the Returnable when it's done.
     *
     * @param returnable The function that will be executed asynchronously.
     * @return A CompletableFuture object.
     */
    public static CompletableFuture<?> supplyAsync(Returnable<?> returnable) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return returnable.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );
    }

    /**
     * It returns a CompletableFuture that will return the result of the given Returnable after the given delay.
     *
     * @param returnable The returnable object that will be executed.
     * @param timeUnit The time unit to use for the delay.
     * @param delay The amount of time to wait before executing the task.
     * @return A CompletableFuture object.
     */
    public static CompletableFuture<?> supplyAsyncLater(Returnable<?> returnable, TimeUnit timeUnit, long delay) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        timeUnit.sleep(delay);
                        return returnable.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );
    }

    /**
     * Shutdown the executor and cancel all running tasks.
     */
    public void shutdown() {
        executor.shutdownNow();
    }
}
