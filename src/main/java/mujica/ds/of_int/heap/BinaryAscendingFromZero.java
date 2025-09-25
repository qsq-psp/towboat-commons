package mujica.ds.of_int.heap;

import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

@CodeHistory(date = "2024/1/15", project = "Ultramarine", name = "IntHeap.MinS0")
@CodeHistory(date = "2025/3/30")
public class BinaryAscendingFromZero extends ArrayIntHeap {

    private static final long serialVersionUID = 0x9f025c6bd2e0bf73L;

    @NotNull
    public static int[] buildHeapUp(@NotNull int[] array, int size) {
        for (int start = 1; start < size; start++) {
            int current = start;
            while (current > 0) {
                int parent = (current - 1) >> 1;
                if (array[parent] <= array[current]) {
                    break;
                }
                int temp = array[current];
                array[current] = array[parent];
                array[parent] = temp;
                current = parent;
            }
        }
        return array; // debug
    }

    @NotNull
    public static int[] buildHeapUp(@NotNull int[] array) {
        return buildHeapUp(array, array.length);
    }

    @NotNull
    public static int[] buildHeapDown(@NotNull int[] array, int size) {
        for (int start = size >> 1; start >= 0; start--) {
            int parent = start;
            while (true) {
                int left = (parent << 1) + 1;
                if (left >= size) {
                    break;
                }
                {
                    int right = left + 1;
                    if (right < size && array[right] < array[left]) {
                        left = right;
                    }
                }
                if (array[parent] <= array[left]) {
                    break;
                }
                int temp = array[parent];
                array[parent] = array[left];
                array[left] = temp;
                parent = left;
            }
        }
        return array; // debug
    }

    @NotNull
    public static int[] buildHeapDown(@NotNull int[] array) {
        return buildHeapDown(array, array.length);
    }

    public BinaryAscendingFromZero() {
        super();
    }

    public BinaryAscendingFromZero(int initialCapacity) {
        super(initialCapacity);
    }

    BinaryAscendingFromZero(@NotNull int[] array) {
        super(array);
    }

    @NotNull
    @Override
    public BinaryAscendingFromZero duplicate() {
        final BinaryAscendingFromZero that = new BinaryAscendingFromZero(Arrays.copyOf(array, endIndex));
        that.endIndex = this.endIndex;
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        int size = endIndex;
        if (size < 0) {
            consumer.accept(new IndexOutOfBoundsException("size = " + size));
            return;
        }
        final int capacity = array.length;
        if (size > capacity) {
            consumer.accept(new IndexOutOfBoundsException("size = " + size + ", capacity = " + capacity));
            size = capacity;
        }
        for (int i = 1; i < size; i++) { // child
            int j = (i - 1) >> 1; // parent
            if (array[i] < array[j]) {
                consumer.accept(new InvariantException("array[" + i + "] = " + array[i] + " (as child) smaller than array[" + j + "] = " + array[j] + " (as parent)"));
            }
        }
    }

    @Override
    protected int startIndex() {
        return 0;
    }

    @Override
    public void offer(int t) {
        int index = endIndex;
        final int newEndIndex = index + 1;
        ensureCapacity(newEndIndex);
        int parent = (index - 1) >> 1;
        while (parent >= 0 && t < array[parent]) {
            array[index] = array[parent];
            index = parent;
            parent = (parent - 1) >> 1;
        }
        array[index] = t;
        endIndex = newEndIndex;
        modCount++;
    }

    @Override
    public void removeRoot() {
        final int newEndIndex = endIndex - 1;
        int value = array[newEndIndex];
        int index = 0;
        while (true) {
            int left = (index << 1) + 1;
            int right = left + 1;
            if (left < newEndIndex) {
                int smaller;
                if (right < newEndIndex && array[right] < array[left]) {
                    smaller = right;
                } else {
                    smaller = left;
                }
                if (array[smaller] < value) {
                    array[index] = array[smaller];
                    index = smaller;
                    continue;
                }
            }
            break;
        }
        array[index] = value;
        endIndex = newEndIndex;
        modCount++;
    }
}
