package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;


@CodeHistory(date = "2018/10/30", project = "TubeM")
@CodeHistory(date = "2025/4/10")
@FunctionalInterface
public interface BooleanConsumer {

    void accept(boolean value);
}
