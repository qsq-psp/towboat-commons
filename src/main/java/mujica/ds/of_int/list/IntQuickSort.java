package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/15", name = "QuickSort")
@CodeHistory(date = "2025/12/22")
public abstract class IntQuickSort implements SortingAlgorithm<int[]> {

    @NotNull
    protected final IntPivotSelector pivotSelector;

    protected IntQuickSort(@NotNull IntPivotSelector pivotSelector) {
        super();
        this.pivotSelector = pivotSelector;
    }

    @Override
    public boolean stable() {
        return false;
    }
}
