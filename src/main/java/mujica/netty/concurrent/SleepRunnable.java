package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@CodeHistory(date = "2026/9/24", project = "nettyon")
@CodeHistory(date = "2026/10/4")
@FunctionalInterface
public interface SleepRunnable {

    /**
     * sleep in current thread, not the executor thread
     * @param executor the helper, some sleep providers use it
     */
    void run(ScheduledExecutorService executor, long duration, @NotNull TimeUnit unit) throws InterruptedException;
}
