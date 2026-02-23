package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/15", name = "QuickSort")
@CodeHistory(date = "2025/12/22")
@ReferencePage(title = "快速排序", href = "https://oi-wiki.org/basic/quick-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntAscendingQuickSort extends SortingAlgorithm<int[]> {

    @NotNull
    final IntPivotSelector pivotSelector;

    public IntAscendingQuickSort(@NotNull IntPivotSelector pivotSelector) {
        super();
        this.pivotSelector = pivotSelector;
    }

    @Override
    @NotNull
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        int startWriteIndex = startIndex;
        int endWriteIndex = endIndex;
        { // end of recursion
            int length = endWriteIndex - startWriteIndex;
            if (length <= 2) {
                if (length == 2) {
                    endWriteIndex--;
                    if (array[startWriteIndex] > array[endWriteIndex]) {
                        int temp = array[startWriteIndex];
                        array[startWriteIndex] = array[endWriteIndex];
                        array[endWriteIndex] = temp;
                    }
                    return 1L;
                } else {
                    return 0L;
                }
            }
        }
        final int pivot = pivotSelector.select(array, startIndex, endIndex);
        int startReadIndex = startWriteIndex;
        int endReadIndex = endWriteIndex;
        long operationCount = 0L;
        LABEL:
        while (true) {
            while (true) { // skip from start to end
                int value = array[startReadIndex];
                if (value <= pivot) {
                    if (value < pivot) {
                        array[startWriteIndex++] = value;
                    }
                    if (++startReadIndex >= endReadIndex) {
                        break LABEL;
                    }
                } else {
                    break;
                }
            }
            while (true) { // skip from end to start
                int value = array[--endReadIndex];
                if (value >= pivot) {
                    if (value > pivot) {
                        array[--endWriteIndex] = value;
                    }
                    if (startReadIndex >= endReadIndex) {
                        break LABEL;
                    }
                } else {
                    ++endReadIndex;
                    break;
                }
            }
            { // swap
                int value = array[startReadIndex++];
                array[startWriteIndex++] = array[--endReadIndex];
                array[--endWriteIndex] = value;
            }
            operationCount++;
            if (startReadIndex >= endReadIndex) {
                break;
            }
        }
        operationCount += sort(array, startIndex, startWriteIndex);
        operationCount += sort(array, endWriteIndex, endIndex);
        while (startWriteIndex < endWriteIndex) {
            array[startWriteIndex++] = pivot;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        if (endIndex - startIndex <= 1) {
            return 0L;
        }
        long operationCount = sort(array, startIndex, endIndex);
        int previous = array[startIndex++];
        int writeIndex = startIndex;
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            int current = array[readIndex];
            if (previous == current) {
                continue;
            }
            array[writeIndex++] = current;
            previous = current;
            operationCount++;
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
