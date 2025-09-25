package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/5/9")
@FunctionalInterface
public interface StateDuplicator<T> {

    void duplicate(@NotNull T original, @NotNull T copy);
}
