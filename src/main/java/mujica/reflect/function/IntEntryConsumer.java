package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;

/**
 * Created in Ultramarine on 2022/5/1, named BiIntConsumer.
 * Recreated on 2025/3/27.
 */
@CodeHistory(date = "2022/5/1", project = "Ultramarine", name = "BiIntConsumer")
@CodeHistory(date = "2025/3/27")
@FunctionalInterface
public interface IntEntryConsumer {

    void accept(int key, int value);
}
