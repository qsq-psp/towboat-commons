package mujica.ds.of_long.list;

import mujica.ds.SortingAlgorithm;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/27.
 */
public abstract class LongQuickSort implements SortingAlgorithm<long[]> {

    @NotNull
    protected final LongPivotSelector pivotSelector;

    protected LongQuickSort(@NotNull LongPivotSelector pivotSelector) {
        super();
        this.pivotSelector = pivotSelector;
    }

    @Override
    public boolean stable() {
        return false;
    }
}
