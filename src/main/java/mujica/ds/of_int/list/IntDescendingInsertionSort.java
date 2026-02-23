package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/21", name = "InsertionSort")
@CodeHistory(date = "2025/10/30")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntDescendingInsertionSort extends SortingAlgorithm<int[]> {

    public static final IntDescendingInsertionSort INSTANCE = new IntDescendingInsertionSort();

    public IntDescendingInsertionSort() {
        super();
    }

    @NotNull
    @Override
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
        int readIndex = startIndex;
        while (readIndex < endIndex) {
            int key = array[readIndex];
            int writeIndex = readIndex - 1;
            while (writeIndex >= startIndex && array[writeIndex] < key) {
                array[writeIndex + 1] = array[writeIndex];
                writeIndex--;
                operationCount++;
            }
            array[writeIndex + 1] = key;
            readIndex++;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        long operationCount = 0L;
        int writeIndex = startIndex;
        LABEL:
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            int key = array[readIndex];
            for (int index = writeIndex - 1; index >= startIndex; index--) {
                if (array[index] >= key) {
                    if (array[index] > key) {
                        index++;
                        int length = writeIndex - index;
                        System.arraycopy(array, index, array, index + 1, length);
                        array[index] = key;
                        writeIndex++;
                        operationCount += length;
                    }
                    continue LABEL;
                }
            }
            System.arraycopy(array, startIndex, array, startIndex + 1, writeIndex - startIndex);
            array[startIndex] = key;
            writeIndex++;
            operationCount += writeIndex - startIndex;
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
