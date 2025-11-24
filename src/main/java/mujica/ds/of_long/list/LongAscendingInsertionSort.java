package mujica.ds.of_long.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/21", name = "InsertionSort")
@CodeHistory(date = "2025/11/1")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class LongAscendingInsertionSort implements SortingAlgorithm<long[]> {

    public static final LongAscendingInsertionSort INSTANCE = new LongAscendingInsertionSort();

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_ASCENDING;
    }

    @Override
    public long apply(@NotNull long[] array, int fromIndex, int toIndex) {
        long operation = 0L;
        int readIndex = fromIndex;
        while (readIndex < toIndex) {
            long key = array[readIndex];
            int writeIndex = readIndex - 1;
            while (writeIndex >= fromIndex && array[writeIndex] > key) {
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
