package mujica.ds.of_long;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/5/11")
public interface LongSequence {

    int longLength();

    long getLong(int index);
}
