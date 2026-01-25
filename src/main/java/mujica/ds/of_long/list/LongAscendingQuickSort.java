package mujica.ds.of_long.list;

import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/31.
 */
@ReferencePage(title = "快速排序", href = "https://oi-wiki.org/basic/quick-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class LongAscendingQuickSort extends LongQuickSort {

    public LongAscendingQuickSort(@NotNull LongPivotSelector pivotSelector) {
        super(pivotSelector);
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_ASCENDING;
    }

    @Override
    public long apply(@NotNull long[] array, int startIndex, int endIndex) {
        int startWrite = startIndex;
        int endWrite = endIndex - 1;
        if (startWrite >= endWrite) {
            return 0L;
        }
        long operationCount = 0L;
        final long pivot = pivotSelector.select(array, startIndex, endIndex);
        int startRead = startWrite;
        int endRead = endWrite;
        BISECT:
        while (true) {
            while (true) {
                long value = array[startRead];
                if (value <= pivot) {
                    if (value < pivot) {
                        array[startWrite++] = value;
                    }
                    if (++startRead > endRead) {
                        break BISECT;
                    }
                } else {
                    break;
                }
            }
            while (true) {
                long value = array[endRead];
                if (value >= pivot) {
                    if (value > pivot) {
                        array[endWrite--] = value;
                    }
                    if (startRead > --endRead) {
                        break BISECT;
                    }
                } else {
                    break;
                }
            }
            {
                long value = array[startRead++];
                array[startWrite++] = array[endRead--];
                array[endWrite--] = value;
            }
            operationCount++;
            if (startRead > endRead) {
                break;
            }
        }
        operationCount += apply(array, startIndex, startWrite);
        operationCount += apply(array, endWrite + 1, endIndex);
        while (startWrite <= endWrite) {
            array[startWrite++] = pivot;
        }
        return operationCount;
    }
}
