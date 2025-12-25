package mujica.ds.of_long.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/21", name = "InsertionSort")
@CodeHistory(date = "2025/11/1")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class LongDescendingInsertionSort implements SortingAlgorithm<long[]> {

    public static final LongDescendingInsertionSort INSTANCE = new LongDescendingInsertionSort();

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_DESCENDING;
    }

    @Override
    public long apply(@NotNull long[] array, int startIndex, int endIndex) {
        long operation = 0L;
        int readIndex = startIndex;
        while (readIndex < endIndex) {
            long key = array[readIndex];
            int writeIndex = readIndex - 1;
            while (writeIndex >= startIndex && array[writeIndex] < key) {
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
