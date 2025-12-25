package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/10")
public class IntDescendingHeapSort implements SortingAlgorithm<int[]> {

    public static final IntDescendingHeapSort INSTANCE = new IntDescendingHeapSort();

    @Override
    public boolean stable() {
        return false;
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_DESCENDING;
    }

    @Override
    public long apply(@NotNull int[] array, int startIndex, int endIndex) {
        long operation = 0L;
        for (int start = startIndex + 1; start < endIndex; start++) {
            int current = start;
            while (current > startIndex) {
                int parent = startIndex + ((current - startIndex - 1) >> 1);
                if (array[parent] <= array[current]) {
                    break;
                }
                int temp = array[current];
                array[current] = array[parent];
                array[parent] = temp;
                current = parent;
                operation++;
            }
        }
        for (int end = endIndex - 1; end > startIndex; end--) {
            int smallest = array[startIndex];
            int value = array[end];
            int index = startIndex;
            while (true) {
                int left = ((index - startIndex) << 1) + startIndex + 1;
                int right = left + 1;
                if (left < end) {
                    int smaller;
                    if (right < end && array[right] < array[left]) {
                        smaller = right;
                    } else {
                        smaller = left;
                    }
                    if (array[smaller] < value) {
                        array[index] = array[smaller];
                        index = smaller;
                        operation++;
                        continue;
                    }
                }
                break;
            }
            array[index] = value;
            array[end] = smallest;
        }
        return operation;
    }
}
