package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

@CodeHistory(date = "2026/9/27")
public interface ExtendedExecutor extends Executor {

    @Override
    void execute(@NotNull Runnable task);

    void execute(@NotNull HeapRunnable task);

    void execute(@NotNull StackRunnable task);
}
