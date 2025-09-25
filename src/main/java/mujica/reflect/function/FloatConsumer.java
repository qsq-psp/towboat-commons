package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;

@CodeHistory(date = "2025/3/3")
@Stable(date = "2025/8/7")
@FunctionalInterface
public interface FloatConsumer {

    void accept(float value);
}
