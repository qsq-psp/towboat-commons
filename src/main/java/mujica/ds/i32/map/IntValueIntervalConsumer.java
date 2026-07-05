package mujica.ds.i32.map;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/1/18")
@FunctionalInterface
public interface IntValueIntervalConsumer {

    void accept(int left, int right, int value);
}
