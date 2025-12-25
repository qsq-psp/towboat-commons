package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/16", name = "BubbleSort")
@CodeHistory(date = "2025/10/25")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntDescendingBubbleSort implements SortingAlgorithm<int[]> {

    public static final IntDescendingBubbleSort INSTANCE = new IntDescendingBubbleSort();

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_DESCENDING;
    }

    @Override
    public long apply(@NotNull int[] array, int startIndex, int endIndex) {
        long operation = 0L;
        while (startIndex < endIndex) {
            for (int index = startIndex + 1; index < endIndex; index++) {
                if (array[index - 1] < array[index]) {
                    int temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operation++;
                }
            }
            endIndex--;
        }
        return operation;
    }
}
