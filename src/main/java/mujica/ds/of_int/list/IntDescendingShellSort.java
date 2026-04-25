package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/25", name = "ShellSort")
@CodeHistory(date = "2025/11/6")
@ReferencePage(title = "希尔排序", href = "https://oi-wiki.org/basic/shell-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntDescendingShellSort extends SortingAlgorithm<int[]> {

    public static final IntDescendingShellSort INSTANCE = new IntDescendingShellSort();

    public IntDescendingShellSort() {
        super();
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.DESCENDING;
    }

    @Override
    public boolean stable() {
        return false;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        return apply(array, startIndex, endIndex, 1);
    }

    private long apply(@NotNull int[] array, int startIndex, int endIndex, int stride) {
        if (startIndex + stride >= endIndex) {
            return 0L;
        }
        long operation = apply(array, startIndex, endIndex, stride * 3 + 1);
        for (int i = startIndex + stride; i < endIndex; i++) {
            int j = i;
            int value = array[j];
            while (true) {
                int k = j - stride;
                if (k >= startIndex && array[k] < value) {
                    array[j] = array[j - stride];
                    j -= stride;
                    operation++;
                } else {
                    break;
                }
            }
            array[j] = value; // insertion
        }
        return operation;
    }
}
