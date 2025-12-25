package mujica.ds.of_long.list;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2023/4/2", project = "Ultramarine", name = "PivotSelector")
@CodeHistory(date = "2025/3/15", name = "PivotSelector")
@CodeHistory(date = "2025/12/4")
@FunctionalInterface
public interface LongPivotSelector {

    long select(@NotNull long[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex);
}
