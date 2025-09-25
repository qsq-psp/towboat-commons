package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;

@CodeHistory(date = "2025/4/8")
@Stable(date = "2025/7/27")
@FunctionalInterface
public interface ByteConsumer {

    void accept(byte value);
}
