package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/1/10.
 */
@CodeHistory(date = "2026/1/10")
public interface PartialSortingAlgorithm<T> extends SortingAlgorithm<T> {

    /**
     * after sorting, elements from startIndex (inclusive) to endIndex (exclusive) is sorted;
     * other elements remain unchanged
     */
    @Override
    long apply(@NotNull T target, @Index(of = "target") int startIndex, @Index(of = "target", inclusive = false) int endIndex);

    /**
     * after sorting, elements from startIndex (inclusive) to midIndex (exclusive) is sorted;
     * elements from midIndex (inclusive) to endIndex (exclusive) is undefined, there may be duplicate or dropped elements;
     * other elements remain unchanged
     */
    long apply(@NotNull T target, @Index(of = "target") int startIndex, @Index(of = "target", inclusive = false) int midIndex, @Index(of = "target", inclusive = false) int endIndex);
}
