package mujica.ds.of_long.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/18", name = "SelectionSort")
@CodeHistory(date = "2025/11/4")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class LongAscendingSelectionSort implements SortingAlgorithm<long[]> {

    @Override
    public boolean stable() {
        return false; // array implementation of selection sort is not stable
    }

    @Override
    public boolean order() {
        return ASCENDING;
    }

    @Override
    public long apply(@NotNull long[] array, int fromIndex, int toIndex) {
        long operation = 0L;
        while (fromIndex < toIndex) {
            int selectedIndex = fromIndex;
            long selectedValue = array[selectedIndex];
            operation++;
            for (int index = fromIndex + 1; index < toIndex; index++) {
                long value = array[index];
                if (value > selectedValue) {
                    selectedIndex = index;
                    selectedValue = value;
                    operation++;
                }
            }
            toIndex--;
            if (selectedIndex != toIndex) {
                array[selectedIndex] = array[toIndex];
                array[toIndex] = selectedValue;
            }
        }
        return operation;
    }
}
