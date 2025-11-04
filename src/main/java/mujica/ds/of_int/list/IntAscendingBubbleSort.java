package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/16", name = "BubbleSort")
@CodeHistory(date = "2025/10/25")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntAscendingBubbleSort implements SortingAlgorithm<int[]> {

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public boolean order() {
        return ASCENDING;
    }

    @Override
    public long apply(@NotNull int[] array, int fromIndex, int toIndex) {
        long operation = 0L;
        while (fromIndex < toIndex) {
            for (int index = fromIndex + 1; index < toIndex; index++) {
                if (array[index - 1] > array[index]) {
                    int temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operation++;
                }
            }
            toIndex--;
        }
        return operation;
    }
}
