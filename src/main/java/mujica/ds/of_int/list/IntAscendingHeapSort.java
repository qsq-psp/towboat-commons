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
    public long apply(@NotNull int[] array, int startIndex, int endIndex) {
        long operation = 0L;
        for (int start = startIndex + 1; start < endIndex; start++) {
            int current = start;
            while (current > startIndex) {
                int parent = startIndex + ((current - startIndex - 1) >> 1);
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
        for (int end = endIndex - 1; end > startIndex; end--) {
            int largest = array[startIndex];
            int value = array[end];
            int index = startIndex;
            while (true) {
                int left = ((index - startIndex) << 1) + startIndex + 1;
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
