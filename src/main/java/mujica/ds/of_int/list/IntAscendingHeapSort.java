package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/9")
@ReferencePage(title = "堆排序", href = "https://oi-wiki.org/basic/heap-sort/")
public class IntAscendingHeapSort extends SortingAlgorithm<int[]> {

    public static final IntAscendingHeapSort INSTANCE = new IntAscendingHeapSort();

    public IntAscendingHeapSort() {
        super();
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        long operationCount = 0L;
        for (int divIndex = startIndex + 1; divIndex < endIndex; divIndex++) {
            int index = divIndex;
            while (index != startIndex) {
                int parentIndex = startIndex + ((index - startIndex - 1) >> 1);
                if (array[parentIndex] >= array[index]) {
                    break;
                }
                int temp = array[index];
                array[index] = array[parentIndex];
                array[parentIndex] = temp;
                index = parentIndex;
                operationCount++;
            }
        }
        for (int divIndex = endIndex - 1; divIndex > startIndex; divIndex--) {
            int largestValue = array[startIndex];
            int value = array[divIndex];
            int index = startIndex;
            while (true) {
                int leftIndex = startIndex + 1 + ((index - startIndex) << 1);
                int rightIndex = leftIndex + 1;
                if (leftIndex < divIndex) {
                    int largerIndex;
                    if (rightIndex < divIndex && array[rightIndex] > array[leftIndex]) {
                        largerIndex = rightIndex;
                    } else {
                        largerIndex = leftIndex;
                    }
                    if (array[largerIndex] > value) {
                        array[index] = array[largerIndex];
                        index = largerIndex;
                        operationCount++;
                        continue;
                    }
                }
                break;
            }
            array[index] = value;
            array[divIndex] = largestValue;
        }
        return operationCount;
    }

    @Override
    public long sortPart(@NotNull int[] array, int startIndex, int midIndex, int endIndex) {
        long operationCount = 0L;
        final int rootIndex = endIndex - 1;
        for (int divIndex = rootIndex; divIndex > startIndex; divIndex--) {
            int index = divIndex - 1;
            while (index != rootIndex) {
                int parentIndex = endIndex - ((endIndex - index) >> 1);
                if (array[parentIndex] <= array[index]) {
                    break;
                }
                int temp = array[index];
                array[index] = array[parentIndex];
                array[parentIndex] = temp;
                index = parentIndex;
                operationCount++;
            }
        }
        for (int divIndex = startIndex; divIndex < midIndex; divIndex++) {
            int smallestValue = array[rootIndex];
            int value = array[divIndex];
            int index = rootIndex;
            while (true) {
                int leftIndex = rootIndex - 1 - ((rootIndex - index) << 1);
                int rightIndex = leftIndex - 1;
                if (leftIndex > divIndex) {
                    int smallerIndex;
                    if (rightIndex > divIndex && array[rightIndex] < array[leftIndex]) {
                        smallerIndex = rightIndex;
                    } else {
                        smallerIndex = leftIndex;
                    }
                    if (array[smallerIndex] < value) {
                        array[index] = array[smallerIndex];
                        index = smallerIndex;
                        operationCount++;
                        continue;
                    }
                }
                break;
            }
            array[index] = value;
            array[divIndex] = smallestValue;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        long operationCount = 0L;
        final int rootIndex = endIndex - 1;
        for (int divIndex = rootIndex; divIndex > startIndex; divIndex--) {
            int index = divIndex - 1;
            while (index != rootIndex) {
                int parentIndex = endIndex - ((endIndex - index) >> 1);
                if (array[parentIndex] <= array[index]) {
                    break;
                }
                int temp = array[index];
                array[index] = array[parentIndex];
                array[parentIndex] = temp;
                index = parentIndex;
                operationCount++;
            }
        }
        int writeIndex = startIndex;
        for (int divIndex = startIndex; divIndex < endIndex; divIndex++) {
            int smallestValue = array[rootIndex];
            int value = array[divIndex];
            int index = rootIndex;
            while (true) {
                int leftIndex = rootIndex - 1 - ((rootIndex - index) << 1);
                int rightIndex = leftIndex - 1;
                if (leftIndex > divIndex) {
                    int smallerIndex;
                    if (rightIndex > divIndex && array[rightIndex] < array[leftIndex]) {
                        smallerIndex = rightIndex;
                    } else {
                        smallerIndex = leftIndex;
                    }
                    if (array[smallerIndex] < value) {
                        array[index] = array[smallerIndex];
                        index = smallerIndex;
                        operationCount++;
                        continue;
                    }
                }
                break;
            }
            array[index] = value;
            if (writeIndex == startIndex || array[writeIndex - 1] != smallestValue) {
                array[writeIndex++] = smallestValue;
            }
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
