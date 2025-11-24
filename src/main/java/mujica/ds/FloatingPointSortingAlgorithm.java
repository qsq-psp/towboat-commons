package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/10/26")
public interface FloatingPointSortingAlgorithm<T> extends SortingAlgorithm<T> {

    @Override
    boolean stable();

    @Override
    int orderingComposition();

    @NotNull
    NotANumberSortingPolicy nanPolicy();

    @Override
    long apply(@NotNull T target, int fromIndex, int toIndex);
}
