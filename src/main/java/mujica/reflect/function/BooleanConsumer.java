package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;

@CodeHistory(date = "2025/4/10")
@Stable(date = "2025/7/27")
@FunctionalInterface
public interface BooleanConsumer {

    void accept(boolean value);
}
