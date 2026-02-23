package mujica.ds.generic.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

@CodeHistory(date = "2026/2/15")
@ReferencePage(title = "插入排序", href = "https://oi-wiki.org/basic/insertion-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class ListInsertionSort<T> extends SortingAlgorithm<List<T>> {

    @NotNull
    final Comparator<T> comparator;

    public ListInsertionSort(@NotNull Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public long sort(@NotNull List<T> list, int startIndex, int endIndex) {
        long operationCount = 0L;
        int readIndex = startIndex;
        while (readIndex < endIndex) {
            T key = list.remove(readIndex);
            int lowIndex = startIndex;
            int highIndex = readIndex;
            while (lowIndex < highIndex) { // binary search
                int midIndex = (lowIndex + highIndex) >>> 1;
                T mid = list.get(midIndex);
                if (comparator.compare(mid, key) > 0) {
                    highIndex = midIndex;
                } else {
                    lowIndex = midIndex + 1;
                }
            }
            list.add(highIndex, key); // insert key before highIndex
            readIndex++;
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull List<T> list) {
        return sort(list, 0, list.size());
    }
}
