package id.luckynetwork.lyrams.lyralibs.bukkit.tasks;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.core.interfaces.Callable;
import id.luckynetwork.lyrams.lyralibs.core.interfaces.Returnable;
import id.luckynetwork.lyrams.lyralibs.core.utils.DateUtils;
import org.bukkit.Bukkit;

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

    public static Future<?> submitLater(Callable callable, TimeUnit timeUnit, long delay) {
        return LyraTasks.getExecutor()
                .submit(() -> {
                            try {
                                timeUnit.sleep(delay);
                                callable.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
    }

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

    public static void runSync(Callable callable) {
        Bukkit.getScheduler().runTask(LyraLibsBukkit.getPlugin(), callable::call);
    }

    public static void runSyncLater(Callable callable, TimeUnit timeUnit, long delay) {
        Bukkit.getScheduler().runTaskLater(LyraLibsBukkit.getPlugin(), callable::call, DateUtils.toTicks(delay, timeUnit));
    }

    public static void runSyncLater(Callable callable, long delay) {
        Bukkit.getScheduler().runTaskLater(LyraLibsBukkit.getPlugin(), callable::call, delay);
    }

    public void shutdown() {
        executor.shutdownNow();
    }

}
