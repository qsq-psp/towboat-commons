package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;


@CodeHistory(date = "2025/4/15")
@FunctionalInterface
public interface CharConsumer {

    void accept(char value);
}
