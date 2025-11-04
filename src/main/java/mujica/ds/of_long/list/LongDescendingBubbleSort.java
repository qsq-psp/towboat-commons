package mujica.ds.of_long.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/16", name = "BubbleSort")
@CodeHistory(date = "2025/10/28")
public class LongDescendingBubbleSort implements SortingAlgorithm<long[]> {

    public static final LongDescendingBubbleSort INSTANCE = new LongDescendingBubbleSort();

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public boolean order() {
        return DESCENDING;
    }

    @Override
    public long apply(@NotNull long[] array, int fromIndex, int toIndex) {
        long operation = 0L;
        while (fromIndex < toIndex) {
            for (int index = fromIndex + 1; index < toIndex; index++) {
                if (array[index - 1] < array[index]) {
                    long temp = array[index - 1];
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
