package mujica.ds.of_byte.list;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/2/6.
 */
@CodeHistory(date = "2026/2/6")
public interface BytePivotSelector {

    byte select(@NotNull byte[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex);
}
