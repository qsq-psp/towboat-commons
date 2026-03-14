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
    public long sort(@NotNull List<T> list, int startIndex, int endIndex) {
        int startWriteIndex = startIndex;
        int endWriteIndex = endIndex;
        { // end of recursion
            int length = endWriteIndex - startWriteIndex;
            if (length <= 2) {
                if (length == 2) {
                    endWriteIndex--;
                    if (comparator.compare(list.get(startWriteIndex), list.get(endWriteIndex)) > 0) {
                        list.set(endWriteIndex, list.set(startWriteIndex, list.get(endWriteIndex)));
                    }
                    return 1L;
                } else {
                    return 0L;
                }
            }
        }
        final T pivot = pivotSelector.select(list, startIndex, endIndex);
        int startReadIndex = startWriteIndex;
        int endReadIndex = endWriteIndex;
        long operationCount = 0L;
        LABEL:
        while (true) {
            while (true) { // skip from start to end
                T value = list.get(startReadIndex);
                int sign = comparator.compare(pivot, value);
                if (sign >= 0) {
                    if (sign > 0) {
                        list.set(startWriteIndex++, value);
                    }
                    if (++startReadIndex >= endReadIndex) {
                        break LABEL;
                    }
                } else {
                    break;
                }
            }
            while (true) { // skip from end to start
                T value = list.get(--endReadIndex);
                int sign = comparator.compare(value, pivot);
                if (sign >= 0) {
                    if (sign > 0) {
                        list.set(--endWriteIndex, value);
                    }
                    if (startReadIndex >= endReadIndex) {
                        break LABEL;
                    }
                } else {
                    ++endReadIndex;
                    break;
                }
            }
            { // possible swap
                T value = list.get(startReadIndex++);
                list.set(startWriteIndex++, list.get(--endReadIndex));
                list.set(--endWriteIndex, value);
            }
            operationCount++;
            if (startReadIndex >= endReadIndex) {
                break;
            }
        }
        operationCount += sort(list, startIndex, startWriteIndex);
        operationCount += sort(list, endWriteIndex, endIndex);
        while (startWriteIndex < endWriteIndex) {
            list.set(startWriteIndex++, pivot);
        }
        return operationCount;
    }
}
