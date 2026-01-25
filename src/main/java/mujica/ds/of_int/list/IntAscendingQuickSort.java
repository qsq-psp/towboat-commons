package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/22")
@ReferencePage(title = "快速排序", href = "https://oi-wiki.org/basic/quick-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntAscendingQuickSort extends IntQuickSort {

    public IntAscendingQuickSort(@NotNull IntPivotSelector pivotSelector) {
        super(pivotSelector);
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_ASCENDING;
    }

    @Override
    public long apply(@NotNull int[] array, int start, int end) {
        int startWrite = start;
        int endWrite = end;
        {
            int length = endWrite - startWrite;
            if (length <= 2) {
                if (length == 2) {
                    endWrite--;
                    if (array[startWrite] > array[endWrite]) {
                        int temp = array[startWrite];
                        array[startWrite] = array[endWrite];
                        array[endWrite] = temp;
                    }
                    return 1L;
                } else {
                    return 0L;
                }
            }
        }
        final int pivot = pivotSelector.select(array, start, end);
        int startRead = startWrite;
        int endRead = endWrite;
        long operationCount = 0L;
        BISECT:
        while (true) {
            while (true) {
                int value = array[startRead];
                if (value <= pivot) {
                    if (value < pivot) {
                        array[startWrite++] = value;
                    }
                    if (++startRead >= endRead) {
                        break BISECT;
                    }
                } else {
                    break;
                }
            }
            while (true) {
                int value = array[--endRead];
                if (value >= pivot) {
                    if (value > pivot) {
                        array[--endWrite] = value;
                    }
                    if (startRead >= endRead) {
                        break BISECT;
                    }
                } else {
                    ++endRead;
                    break;
                }
            }
            {
                int value = array[startRead++];
                array[startWrite++] = array[--endRead];
                array[--endWrite] = value;
            }
            operationCount++;
            if (startRead >= endRead) {
                break;
            }
        }
        operationCount += apply(array, start, startWrite);
        operationCount += apply(array, endWrite, end);
        while (startWrite < endWrite) {
            array[startWrite++] = pivot;
        }
        return operationCount;
    }
}
