package mujica.ds.sort;

import mujica.ds.i32.I32;
import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/15", name = "QuickSort")
@CodeHistory(date = "2025/12/22", name = "IntAscendingQuickSort")
@CodeHistory(date = "2026/7/14")
@Name(value = "32位有符号整数递增快速排序", language = "zh")
public class S32AscendingQuickSort implements Sort<int[]> {

    @NotNull
    final SelectPivot<I32, int[]> selectPivot;

    @NotNull
    final I32 pivotSlot = new I32();

    public S32AscendingQuickSort(@NotNull SelectPivot<I32, int[]> selectPivot) {
        super();
        this.selectPivot = selectPivot;
    }

    @Override
    public boolean isDescending() {
        return false;
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
        selectPivot.selectPivot(array, startIndex, endIndex, pivotSlot);
        final int pivot = pivotSlot.getI32();
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
        if (startWriteIndex == endIndex) {
            throw new RuntimeException("too large pivot");
        }
        operationCount += sort(array, startIndex, startWriteIndex);
        if (startIndex == endWriteIndex) {
            throw new RuntimeException("too small pivot");
        }
        operationCount += sort(array, endWriteIndex, endIndex);
        while (startWriteIndex < endWriteIndex) {
            array[startWriteIndex++] = pivot;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
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
        endSlot.setI32(writeIndex);
        return operationCount;
    }
}
