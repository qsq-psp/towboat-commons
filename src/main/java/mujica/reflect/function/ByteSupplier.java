package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;

@CodeHistory(date = "2025/5/4")
@Stable(date = "2025/7/28")
@FunctionalInterface
public interface ByteSupplier {

    byte getAsByte();
}
