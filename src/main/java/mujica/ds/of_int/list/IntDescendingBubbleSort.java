package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/16", name = "BubbleSort")
@CodeHistory(date = "2025/10/25")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntDescendingBubbleSort extends SortingAlgorithm<int[]> {

    public static final IntDescendingBubbleSort INSTANCE = new IntDescendingBubbleSort();

    public IntDescendingBubbleSort() {
        super();
    }

    @Override
    @NotNull
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.DESCENDING;
    }

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < endIndex) {
            for (int index = startIndex + 1; index < endIndex; index++) {
                if (array[index - 1] < array[index]) {
                    int temp = array[index - 1];
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
    public long sortPart(@NotNull int[] array, int startIndex, int midIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < midIndex) {
            for (int index = endIndex - 1; index > startIndex; index--) {
                if (array[index - 1] < array[index]) {
                    int temp = array[index - 1];
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
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        long operationCount = 0L;
        int writeIndex = startIndex;
        int divIndex = startIndex;
        while (divIndex < endIndex) {
            for (int index = endIndex - 1; index > divIndex; index--) {
                if (array[index - 1] < array[index]) {
                    int temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operationCount++;
                }
            }
            if (writeIndex == startIndex || array[writeIndex - 1] != array[divIndex]) {
                array[writeIndex++] = array[divIndex];
            }
            divIndex++;
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
