package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/4/8")
@FunctionalInterface
public interface ByteConsumer {

    void accept(byte value);
}
