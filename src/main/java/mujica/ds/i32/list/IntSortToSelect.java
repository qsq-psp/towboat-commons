package mujica.ds.i32.list;

import mujica.ds.any.list.SortingAlgorithm;
import mujica.ds.any.list.MonotonicityDirection;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/24.
 */
public class IntSortToSelect implements IntSelectAlgorithm {

    @NotNull
    final SortingAlgorithm<int[]> ascending;

    public IntSortToSelect(@NotNull SortingAlgorithm<int[]> ascending) {
        super();
        if (ascending.monotonicity() != MonotonicityDirection.ASCENDING) {
            throw new IllegalArgumentException();
        }
        this.ascending = ascending;
    }

    @Override
    public int select(@NotNull int[] array, int startIndex, int midIndex, int endIndex) {
        ascending.sortPart(array, startIndex, midIndex + 1, endIndex);
        return array[midIndex];
    }
}
