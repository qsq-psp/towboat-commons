package mujica.ds.of_boolean.list;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/4/15")
public interface BooleanSequence {

    int booleanLength();

    boolean getBoolean(int index);
}
