package mujica.ds.i64;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/5/11", name = "LongSequence")
@CodeHistory(date = "2026/7/6")
public interface ReadOnlyI64Array {

    int longLength();

    long getLong(int index);
}
