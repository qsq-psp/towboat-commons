package mujica.ds.of_long.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/16", name = "BubbleSort")
@CodeHistory(date = "2025/10/28")
public class LongAscendingBubbleSort extends SortingAlgorithm<long[]> {

    public static final LongAscendingBubbleSort INSTANCE = new LongAscendingBubbleSort();

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
    public long sort(@NotNull long[] array, int startIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < endIndex) {
            for (int index = startIndex + 1; index < endIndex; index++) {
                if (array[index - 1] > array[index]) {
                    long temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operationCount++;
                }
            }
            endIndex--;
        }
        return operationCount;
    }

    @Override
    public long sortPart(@NotNull long[] array, int startIndex, int midIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < midIndex) {
            for (int index = endIndex - 1; index > startIndex; index--) {
                if (array[index - 1] > array[index]) {
                    long temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operationCount++;
                }
            }
            startIndex++;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull long[] array, int startIndex, @NotNull IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        long operationCount = 0L;
        int writeIndex = startIndex;
        while (startIndex < endIndex) {
            for (int index = endIndex - 1; index > startIndex; index--) {
                if (array[index - 1] > array[index]) {
                    long temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operationCount++;
                }
            }
            if (writeIndex == startIndex || array[writeIndex - 1] != array[startIndex]) {
                array[writeIndex++] = array[startIndex];
            }
            startIndex++;
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
