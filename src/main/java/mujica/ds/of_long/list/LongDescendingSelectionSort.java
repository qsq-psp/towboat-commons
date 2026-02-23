package mujica.ds.of_long.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/18", name = "SelectionSort")
@CodeHistory(date = "2025/11/4")
@ReferencePage(title = "选择排序", href = "https://oi-wiki.org/basic/selection-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class LongDescendingSelectionSort extends SortingAlgorithm<long[]> {

    public static final LongDescendingSelectionSort INSTANCE = new LongDescendingSelectionSort();

    @Override
    @NotNull
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.DESCENDING;
    }

    @Override
    public boolean stable() {
        return false; // array implementation of selection sort is not stable
    }

    @Override
    public long sort(@NotNull long[] array, int startIndex, int endIndex) {
        long operation = 0L;
        while (startIndex < endIndex) {
            int selectedIndex = startIndex;
            long selectedValue = array[selectedIndex]; // select min
            operation++;
            for (int index = startIndex + 1; index < endIndex; index++) {
                long value = array[index];
                if (value < selectedValue) {
                    selectedIndex = index;
                    selectedValue = value;
                    operation++;
                }
            }
            endIndex--;
            if (selectedIndex != endIndex) {
                array[selectedIndex] = array[endIndex];
                array[endIndex] = selectedValue;
            }
        }
        return operation;
    }
}
