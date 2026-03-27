package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/5/4")
@FunctionalInterface
public interface ByteSupplier {

    byte getAsByte();
}
