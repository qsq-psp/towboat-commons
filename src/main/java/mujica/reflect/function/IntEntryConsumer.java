package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2022/5/1", project = "Ultramarine", name = "BiIntConsumer")
@CodeHistory(date = "2025/3/27")
@FunctionalInterface
public interface IntEntryConsumer {

    void accept(int key, int value);
}
