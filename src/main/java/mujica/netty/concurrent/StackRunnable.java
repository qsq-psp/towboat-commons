package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

@CodeHistory(date = "2026/9/20")
@FunctionalInterface
public interface StackRunnable {

    void run(@NotNull BooleanSupplier yield);
}
