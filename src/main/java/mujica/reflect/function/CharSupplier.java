package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/3/25")
@FunctionalInterface
public interface CharSupplier {

    char getAsChar();
}
