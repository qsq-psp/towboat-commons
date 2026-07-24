package mujica.ds.sort;

import mujica.ds.any.list.MonotonicityDirection;
import mujica.ds.any.list.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

@CodeHistory(date = "2026/2/11")
public class ListHeapSort<T> extends SortingAlgorithm<List<T>> {

    @NotNull
    final Comparator<T> comparator;

    public ListHeapSort(@NotNull Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull List<T> target, int startIndex, int endIndex) {
        return 0L;
    }

    @Override
    public long sort(@NotNull List<T> list) {
        return sort(list, 0, list.size());
    }
}
