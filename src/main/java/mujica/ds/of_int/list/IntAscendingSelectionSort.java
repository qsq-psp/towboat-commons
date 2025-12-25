package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/18", name = "SelectionSort")
@CodeHistory(date = "2025/11/2")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntAscendingSelectionSort implements SortingAlgorithm<int[]> {

    public static final IntAscendingSelectionSort INSTANCE = new IntAscendingSelectionSort();

    @Override
    public boolean stable() {
        return false; // array implementation of selection sort is not stable
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_ASCENDING;
    }

    @Override
    public long apply(@NotNull int[] array, int startIndex, int endIndex) {
        long operation = 0L;
        while (startIndex < endIndex) {
            int selectedIndex = startIndex;
            int selectedValue = array[selectedIndex]; // select max
            operation++;
            for (int index = startIndex + 1; index < endIndex; index++) {
                int value = array[index];
                if (value > selectedValue) {
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
