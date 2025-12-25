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
    public long apply(@NotNull int[] array, int startIndex, int endIndex) {
        int lowIndex = startIndex;
        int highIndex = endIndex - 1;
        if (lowIndex >= highIndex) {
            return 0L;
        }
        long operationCount = 0L;
        final int pivot = pivotSelector.select(array, startIndex, endIndex);
        BISECT:
        while (true) {
            while (true) {
                if (array[lowIndex] <= pivot) {
                    if (++lowIndex >= highIndex) {
                        break BISECT;
                    }
                } else {
                    break;
                }
            }
            while (true) {
                if (array[highIndex] >= pivot) {
                    if (--highIndex <= lowIndex) {
                        break BISECT;
                    }
                } else {
                    break;
                }
            }
            int value = array[lowIndex];
            array[lowIndex] = array[highIndex];
            array[highIndex] = value;
            lowIndex++;
            highIndex--;
            operationCount++;
            if (lowIndex >= highIndex) {
                break;
            }
        }
        assert lowIndex - highIndex == 0 || lowIndex - highIndex == 1;
        if (lowIndex == highIndex && array[lowIndex] <= pivot) {
            lowIndex++;
        }
        if (lowIndex == startIndex || lowIndex == endIndex) {
            for (highIndex = startIndex; highIndex < endIndex; highIndex++) {
                if (array[highIndex] == pivot) {
                    break;
                }
            }
            assert highIndex < endIndex;
            if (lowIndex == startIndex) {
                array[highIndex] = array[lowIndex];
                array[lowIndex] = pivot;
                lowIndex++;
            } else {
                lowIndex--;
                array[highIndex] = array[lowIndex];
                array[lowIndex] = pivot;
            }
        }
        assert startIndex < lowIndex;
        assert lowIndex < endIndex;
        operationCount += apply(array, startIndex, lowIndex);
        operationCount += apply(array, lowIndex, endIndex);
        return operationCount;
    }
}
