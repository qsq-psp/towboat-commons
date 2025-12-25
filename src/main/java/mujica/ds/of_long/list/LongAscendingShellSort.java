package mujica.ds.of_long.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/25", name = "ShellSort")
@CodeHistory(date = "2025/11/7")
@ReferencePage(title = "希尔排序", href = "https://oi-wiki.org/basic/shell-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class LongAscendingShellSort implements SortingAlgorithm<long[]> {

    public static final LongAscendingShellSort INSTANCE = new LongAscendingShellSort();

    @Override
    public boolean stable() {
        return false;
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_ASCENDING;
    }

    @Override
    public long apply(@NotNull long[] array, int startIndex, int endIndex) {
        return apply(array, startIndex, endIndex, 1);
    }

    private long apply(@NotNull long[] array, int fromIndex, int toIndex, int stride) {
        if (fromIndex + stride >= toIndex) {
            return 0L;
        }
        long operation = apply(array, fromIndex, toIndex, stride * 3 + 1);
        for (int i = fromIndex + stride; i < toIndex; i++) {
            int j = i;
            long value = array[j];
            while (true) {
                int k = j - stride;
                if (k >= fromIndex && array[k] > value) {
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
