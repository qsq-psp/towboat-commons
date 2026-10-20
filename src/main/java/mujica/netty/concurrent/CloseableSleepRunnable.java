package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@CodeHistory(date = "2026/10/7")
public interface CloseableSleepRunnable extends Closeable, SleepRunnable {

    @Override
    void run(ScheduledExecutorService executor, long duration, @NotNull TimeUnit unit) throws InterruptedException;

    @Override
    void close() throws IOException;
}
