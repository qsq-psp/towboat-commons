package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/9")
public class IntAscendingHeapSort implements SortingAlgorithm<int[]> {

    public static final IntAscendingHeapSort INSTANCE = new IntAscendingHeapSort();

    @Override
    public boolean stable() {
        return false;
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_ASCENDING;
    }

    @Override
    public long apply(@NotNull int[] array, int fromIndex, int toIndex) {
        long operation = 0L;
        for (int start = fromIndex + 1; start < toIndex; start++) {
            int current = start;
            while (current > fromIndex) {
                int parent = fromIndex + ((current - fromIndex - 1) >> 1);
                if (array[parent] >= array[current]) {
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
            int largest = array[fromIndex];
            int value = array[end];
            int index = fromIndex;
            while (true) {
                int left = ((index - fromIndex) << 1) + fromIndex + 1;
                int right = left + 1;
                if (left < end) {
                    int larger;
                    if (right < end && array[right] > array[left]) {
                        larger = right;
                    } else {
                        larger = left;
                    }
                    if (array[larger] > value) {
                        array[index] = array[larger];
                        index = larger;
                        operation++;
                        continue;
                    }
                }
                break;
            }
            array[index] = value;
            array[end] = largest;
        }
        return operation;
    }
}
