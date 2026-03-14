package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;

import java.io.IOException;

@CodeHistory(date = "2026/2/25")
@FunctionalInterface
public interface IOIntConsumer {

    void accept(int value) throws IOException;
}
