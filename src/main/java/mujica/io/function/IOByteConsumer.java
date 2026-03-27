package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;

import java.io.IOException;

@CodeHistory(date = "2026/3/25")
@FunctionalInterface
public interface IOByteConsumer {

    void accept(byte value) throws IOException;
}
