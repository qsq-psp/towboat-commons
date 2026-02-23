package mujica.ds.generic.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

@CodeHistory(date = "2026/2/23")
@ReferencePage(title = "快速排序", href = "https://oi-wiki.org/basic/quick-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class ListQuickSort<T> extends SortingAlgorithm<List<T>> {

    @NotNull
    final Comparator<T> comparator;

    @NotNull
    final ListPivotSelector<T> pivotSelector;

    public ListQuickSort(@NotNull Comparator<T> comparator, @NotNull ListPivotSelector<T> pivotSelector) {
        super();
        this.comparator = comparator;
        this.pivotSelector = pivotSelector;
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull List<T> target, int startIndex, int endIndex) {
        return 0; // todo
    }
}
