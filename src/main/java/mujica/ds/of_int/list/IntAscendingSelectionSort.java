package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/18", name = "SelectionSort")
@CodeHistory(date = "2025/11/2")
@ReferencePage(title = "选择排序", href = "https://oi-wiki.org/basic/selection-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntAscendingSelectionSort extends SortingAlgorithm<int[]> {

    public static final IntAscendingSelectionSort INSTANCE = new IntAscendingSelectionSort();

    @Override
    @NotNull
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public boolean stable() { // can be removed; super method also returns false
        return false; // array implementation of selection sort is not stable
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < endIndex) {
            int selectedIndex = startIndex;
            int selectedValue = array[selectedIndex]; // select max
            for (int index = startIndex + 1; index < endIndex; index++) {
                int value = array[index];
                if (value > selectedValue) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            endIndex--;
            if (selectedIndex != endIndex) {
                array[selectedIndex] = array[endIndex];
                array[endIndex] = selectedValue;
            }
        }
        return operationCount;
    }

    @Override
    public long sortPart(@NotNull int[] array, int startIndex, int midIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < midIndex) {
            int selectedIndex = startIndex;
            int selectedValue = array[selectedIndex]; // select min
            for (int index = startIndex + 1; index < endIndex; index++) {
                int value = array[index];
                if (value < selectedValue) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            if (selectedIndex != startIndex) {
                array[selectedIndex] = array[startIndex];
                array[startIndex] = selectedValue;
            }
            startIndex++;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        long operationCount = 0L;
        int readIndex = startIndex;
        int writeIndex = startIndex;
        while (readIndex < endIndex) {
            int selectedIndex = readIndex;
            int selectedValue = array[selectedIndex]; // select min
            for (int index = readIndex + 1; index < endIndex; index++) {
                int value = array[index];
                if (value < selectedValue) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            array[selectedIndex] = array[readIndex];
            if (writeIndex == startIndex || array[writeIndex - 1] != selectedValue) {
                array[writeIndex++] = selectedValue;
            }
            readIndex++;
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
