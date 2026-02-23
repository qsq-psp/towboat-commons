package mujica.ds.generic.list;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created on 2026/2/23.
 */
@CodeHistory(date = "2026/2/23")
@FunctionalInterface
public interface ListPivotSelector<T> {

    T select(@NotNull List<T> list, @Index(of = "list") int startIndex, @Index(of = "list", inclusive = false) int endIndex);
}
