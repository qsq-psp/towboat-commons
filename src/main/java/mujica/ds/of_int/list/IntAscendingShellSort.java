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
public class IntAscendingShellSort extends SortingAlgorithm<int[]> {

    public static final IntAscendingShellSort INSTANCE = new IntAscendingShellSort();

    public IntAscendingShellSort() {
        super();
    }

    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    private long sort(@NotNull int[] array, int startIndex, int endIndex, int step) {
        if (startIndex + step >= endIndex) {
            return 0L;
        }
        long operationCount = sort(array, startIndex, endIndex, step * 3 + 1);
        for (int index0 = startIndex + step; index0 < endIndex; index0++) {
            int index1 = index0;
            int value = array[index1];
            while (true) {
                int index2 = index1 - step;
                if (index2 >= startIndex && array[index2] > value) {
                    array[index1] = array[index1 - step];
                    index1 -= step;
                    operationCount++;
                } else {
                    break;
                }
            }
            array[index1] = value; // insertion
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        return sort(array, startIndex, endIndex, 1);
    }
}
