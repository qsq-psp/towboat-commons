package mujica.ds.bit;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/4/15", name = "BooleanSequence")
@CodeHistory(date = "2026/7/6")
public interface ReadOnlyBitArray {

    int booleanLength();

    boolean getBoolean(int index);
}
