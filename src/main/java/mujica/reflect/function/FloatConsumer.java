package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;


@CodeHistory(date = "2018/9/20", project = "TubeM")
@CodeHistory(date = "2025/3/3")
@FunctionalInterface
public interface FloatConsumer {

    void accept(float value);
}
