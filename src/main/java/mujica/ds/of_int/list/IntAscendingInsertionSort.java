package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/21", name = "InsertionSort")
@CodeHistory(date = "2025/10/30")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntAscendingInsertionSort implements SortingAlgorithm<int[]> {

    public static final IntAscendingInsertionSort INSTANCE = new IntAscendingInsertionSort();

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_ASCENDING;
    }

    @Override
    public long apply(@NotNull int[] array, int startIndex, int endIndex) {
        long operation = 0L;
        int readIndex = startIndex;
        while (readIndex < endIndex) {
            int key = array[readIndex];
            int writeIndex = readIndex - 1;
            while (writeIndex >= startIndex && array[writeIndex] > key) {
                array[writeIndex + 1] = array[writeIndex];
                writeIndex--;
                operation++;
            }
            array[writeIndex + 1] = key;
            readIndex++;
        }
        return operation;
    }
}
