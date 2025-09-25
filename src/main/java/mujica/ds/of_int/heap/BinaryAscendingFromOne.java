package mujica.ds.of_int.heap;

import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

@CodeHistory(date = "2024/1/15", project = "Ultramarine", name = "IntHeap.MinS1")
@CodeHistory(date = "2025/3/30")
public class BinaryAscendingFromOne extends ArrayIntHeap {

    private static final long serialVersionUID = 0x612a605dee6c56fbL;

    /**
     * @param endIndex not inclusive; before add
     */
    public static void unsafeAdd(@NotNull int[] heap, int endIndex, int value) {
        while (endIndex > 1) {
            int parent = endIndex >> 1;
            if (value < heap[parent]) {
                heap[endIndex] = heap[parent];
                endIndex = parent;
            } else {
                break;
            }
        }
        heap[endIndex] = value;
    }

    /**
     * Check both array length and endIndex
     * @param endIndex not inclusive; before add
     */
    @NotNull
    public static int[] resizeAdd(@NotNull int[] heap, int endIndex, int value) {
        if (endIndex < 1) {
            throw new IllegalArgumentException();
        }
        int capacity = heap.length;
        if (endIndex >= capacity) {
            if (endIndex > capacity) {
                throw new IllegalArgumentException();
            }
            heap = Arrays.copyOf(heap, Math.max(INITIAL_CAPACITY, Math.max(endIndex, capacity << 1)));
        }
        unsafeAdd(heap, endIndex, value);
        return heap;
    }

    /**
     * Check both array length and endIndex, return false for bad array length but throw for bad endIndex
     * @param endIndex not inclusive; before add
     */
    public static boolean tryAdd(@NotNull int[] heap, int endIndex, int value) {
        if (endIndex < 1) {
            throw new IllegalArgumentException();
        }
        if (heap.length <= endIndex) {
            return false;
        }
        unsafeAdd(heap, endIndex, value);
        return true;
    }

    /**
     * The heap size is inside the array, at index 0
     */
    public static void unsafeAdd(@NotNull int[] heap, int value) {
        unsafeAdd(heap, heap[0]++, value);
    }

    /**
     * The heap size is inside the array, at index 0
     */
    @NotNull
    public static int[] resizeAdd(@NotNull int[] heap, int value) {
        return resizeAdd(heap, heap[0]++, value);
    }

    /**
     * The heap size is inside the array, at index 0
     */
    public static boolean tryAdd(@NotNull int[] heap, int value) {
        return tryAdd(heap, heap[0]++, value);
    }

    /**
     * @param endIndex not inclusive; after remove
     */
    public static void unsafeRemove(@NotNull int[] heap, int endIndex) {
        final int value = heap[endIndex];
        int index = 1;
        while (true) {
            int left = index << 1;
            int right = left + 1;
            if (left < endIndex) {
                int smaller;
                if (right < endIndex && heap[right] < heap[left]) {
                    smaller = right;
                } else {
                    smaller = left;
                }
                if (heap[smaller] < value) {
                    heap[index] = heap[smaller];
                    index = smaller;
                    continue;
                }
            }
            break;
        }
        heap[index] = value;
    }

    /**
     * @param endIndex not inclusive; after remove
     */
    public static int remove(@NotNull int[] heap, int endIndex) {
        if (endIndex < 1) {
            throw new IndexOutOfBoundsException();
        }
        final int value = heap[1];
        unsafeRemove(heap, endIndex);
        return value;
    }

    public static int remove(@NotNull int[] heap) {
        final int newEndIndex = heap[0] - 1;
        if (newEndIndex < 1) {
            throw new IndexOutOfBoundsException();
        }
        final int value = heap[1];
        unsafeRemove(heap, newEndIndex);
        heap[0] = newEndIndex;
        return value;
    }

    public BinaryAscendingFromOne() {
        super();
    }

    public BinaryAscendingFromOne(int initialCapacity) {
        super(initialCapacity);
    }

    BinaryAscendingFromOne(@NotNull int[] array) {
        super(array);
    }

    @NotNull
    @Override
    public BinaryAscendingFromOne duplicate() {
        final BinaryAscendingFromOne that = new BinaryAscendingFromOne(Arrays.copyOf(array, endIndex));
        that.endIndex = this.endIndex;
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        int size = endIndex;
        if (size < 1) {
            consumer.accept(new IndexOutOfBoundsException("size = " + size));
            return;
        }
        final int capacity = array.length;
        if (size > capacity) {
            consumer.accept(new IndexOutOfBoundsException("size = " + size + ", capacity = " + capacity));
            size = capacity;
        }
        for (int i = 2; i < size; i++) { // child
            int j = i >> 1; // parent
            if (array[i] < array[j]) {
                consumer.accept(new InvariantException("array[" + i + "] = " + array[i] + " (as child) smaller than array[" + j + "] = " + array[j] + " (as parent)"));
            }
        }
    }

    @Override
    protected int startIndex() {
        return 1;
    }

    @Override
    public void offer(int t) {
        array = resizeAdd(array, endIndex, t);
        endIndex++;
        modCount++;
    }

    @Override
    public void removeRoot() {
        final int newEndIndex = endIndex - 1;
        unsafeRemove(array, newEndIndex);
        endIndex = newEndIndex;
        modCount++;
    }
}
