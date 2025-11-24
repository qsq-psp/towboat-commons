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
    public long apply(@NotNull int[] array, int fromIndex, int toIndex) {
        long operation = 0L;
        for (int start = fromIndex + 1; start < toIndex; start++) {
            int current = start;
            while (current > fromIndex) {
                int parent = fromIndex + ((current - fromIndex - 1) >> 1);
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
        for (int end = toIndex - 1; end > fromIndex; end--) {
            int smallest = array[fromIndex];
            int value = array[end];
            int index = fromIndex;
            while (true) {
                int left = ((index - fromIndex) << 1) + fromIndex + 1;
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
