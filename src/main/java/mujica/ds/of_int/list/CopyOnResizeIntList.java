package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.PublicIntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/6/25")
public class CopyOnResizeIntList extends AbstractIntList {

    @NotNull
    final ResizePolicy policy;

    @NotNull
    int[] array;

    @Index(of = "array", inclusive = false)
    int endIndex;

    int modCount;

    public CopyOnResizeIntList(@Nullable ResizePolicy policy) {
        super();
        if (policy == null) {
            policy = TwiceResizePolicy.INSTANCE;
        }
        this.policy = policy;
        this.array = new int[policy.initialCapacity()];
    }

    CopyOnResizeIntList(@NotNull ResizePolicy policy, @NotNull int[] array) {
        super();
        this.policy = policy;
        this.array = array;
        this.endIndex = array.length;
    }

    @NotNull
    @Override
    public CopyOnResizeIntList duplicate() {
        return new CopyOnResizeIntList(policy, Arrays.copyOf(array, endIndex));
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (endIndex < 0 || endIndex > array.length) {
            consumer.accept(new IndexOutOfBoundsException("length = " + endIndex + ", capacity = " + array.length));
        }
    }

    @Override
    public boolean isEmpty() {
        return endIndex == 0;
    }

    @Override
    public boolean isFull() {
        final int capacity = array.length;
        return endIndex == capacity && policy.nextCapacity(capacity) == capacity;
    }

    @Override
    public int intLength() {
        return endIndex;
    }

    @NotNull
    @Override
    public int[] toIntArray() {
        return Arrays.copyOf(array, endIndex);
    }

    @Override
    public void getRange(int srcOffset, @NotNull int[] dst, int dstOffset, int length) {
        if (Math.addExact(srcOffset, length) > this.endIndex) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(array, srcOffset, dst, dstOffset, length);
    }

    @Override
    public int getInt(int i) {
        if (i >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        return array[i];
    }

    protected boolean ensureOneSlot() {
        if (array.length == endIndex) {
            int capacity = policy.nextCapacity(endIndex);
            if (capacity <= endIndex) {
                return false;
            }
            array = Arrays.copyOf(array, capacity);
        }
        return true;
    }

    protected boolean ensureSlots(int count) {
        count = Math.addExact(endIndex, count);
        int oldCapacity = array.length;
        while (oldCapacity < count) {
            int newCapacity = policy.nextCapacity(endIndex);
            if (newCapacity <= oldCapacity) {
                return false;
            }
            oldCapacity = newCapacity;
        }
        array = Arrays.copyOf(array, oldCapacity);
        return true;
    }

    @Override
    public boolean offerAt(int i, int t) {
        if (i < 0 || endIndex < i) {
            throw new IndexOutOfBoundsException();
        }
        if (ensureOneSlot()) {
            System.arraycopy(array, i, array, i + 1, endIndex - i);
            array[i] = t;
            endIndex++;
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean offerLast(int t) {
        if (ensureOneSlot()) {
            array[endIndex++] = t;
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int removeAt(int i) {
        if (i >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        final int removed = array[i];
        System.arraycopy(array, i + 1, array, i, endIndex - i - 1);
        endIndex--;
        modCount++;
        return removed;
    }

    @Override
    public int removeLast() {
        if (endIndex == 0) {
            throw new NoSuchElementException();
        }
        modCount++;
        return array[--endIndex];
    }

    @Override
    public void removeRange(int startIndex, int endIndex) {
        if (startIndex < 0 || this.endIndex < endIndex || startIndex > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        if (startIndex == endIndex) {
            return;
        }
        System.arraycopy(array, endIndex, array, startIndex, this.endIndex - endIndex);
        this.endIndex -= endIndex - startIndex;
        modCount++;
    }

    @Override
    public void removeIf(@NotNull IntPredicate filter) {
        if (endIndex == 0) {
            return;
        }
        final byte[] retain = new byte[endIndex];
        final int expectedModCount = modCount;
        for (int i = 0; i < endIndex; i++) {
            if (!filter.test(array[i])) {
                retain[i] = 1;
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
        int n = 0;
        for (int i = 0; i < endIndex; i++) {
            if (retain[i] != 0) {
                array[n++] = array[i];
            }
        }
        endIndex = n;
        modCount++;
    }

    @Override
    public void removeIf(@NotNull IntEntryPredicate filter) {
        if (endIndex == 0) {
            return;
        }
        final byte[] retain = new byte[endIndex];
        final int expectedModCount = modCount;
        for (int i = 0; i < endIndex; i++) {
            if (!filter.test(i, array[i])) {
                retain[i] = 1;
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
        int n = 0;
        for (int i = 0; i < endIndex; i++) {
            if (retain[i] != 0) {
                array[n++] = array[i];
            }
        }
        endIndex = n;
        modCount++;
    }

    void unsafeRemoveIf(@NotNull IntPredicate filter) {
        int n = 0;
        for (int i = 0; i < endIndex; i++) {
            if (filter.test(array[i])) {
                continue;
            }
            array[n++] = array[i];
        }
        endIndex = n;
    }

    void unsafeRemoveIf(@NotNull IntEntryPredicate filter) {
        int n = 0;
        for (int i = 0; i < endIndex; i++) {
            if (filter.test(i, array[i])) {
                continue;
            }
            array[n++] = array[i];
        }
        endIndex = n;
    }

    @Override
    public int setInt(int i, int t) {
        if (i >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        final int s = array[i];
        array[i] = t;
        modCount++;
        return s;
    }

    @Override
    public void swap(int i, int j) {
        if (Math.max(i, j) >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        final int t = array[i];
        array[i] = array[j];
        array[j] = t;
        modCount++;
    }

    @Override
    public void map(@NotNull IntUnaryOperator operator) {
        int expectedModCount = modCount;
        for (int i = 0; i < endIndex; i++) {
            array[i] = operator.applyAsInt(array[i]);
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            modCount = ++expectedModCount;
        }
    }

    @Override
    public void clear() {
        endIndex = 0;
        modCount++;
    }

    @Override
    public void reverse() {
        int i = 0;
        int j = endIndex - 1;
        while (i < j) {
            int t = array[i];
            array[i] = array[j];
            array[j] = t;
            i++;
            j++;
        }
        modCount++;
    }

    @Override
    public void rotate(int d) {
        final int n = endIndex;
        if (n <= 1) {
            return;
        }
        d %= n;
        if (d == 0) {
            return;
        }
        if (d < 0) {
            d += n;
        }
        if (ensureSlots(d)) {
            System.arraycopy(array, 0, array, d, n);
            System.arraycopy(array, n, array, 0, d);
            return;
        }
        if (d == 1) {
            int s = array[n - 1];
            for (int i = 0; i < n; i++) {
                int t = array[i];
                array[i] = s;
                s = t;
            }
        } else if (d == n - 1) {
            int s = array[0];
            for (int i = n - 1; i >= 0; i--) {
                int t = array[i];
                array[i] = s;
                s = t;
            }
        } else {
            int[] dArray = Arrays.copyOfRange(array, n - d, d);
            System.arraycopy(array, 0, array, d, n - d);
            System.arraycopy(dArray, 0, array, 0, d);
        }
    }

    @Override
    public void sort(@NotNull MonotonicityDirection direction) {
        Arrays.sort(array, 0, endIndex);
        if (direction == MonotonicityDirection.ASCENDING) {
            modCount++;
        } else if (direction == MonotonicityDirection.DESCENDING) {
            reverse(); // modCount included
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public long sort(@NotNull SortingAlgorithm<int[]> algorithm) {
        return algorithm.sort(array, 0, endIndex);
    }

    @Override
    public long sortPart(@NotNull SortingAlgorithm<int[]> algorithm, int m) {
        if (!(0 <= m && m <= endIndex)) {
            throw new IndexOutOfBoundsException();
        }
        if (m == 0) {
            clear();
            return 0L;
        }
        final long r = algorithm.sortPart(array, 0, m, endIndex);
        endIndex = m;
        modCount++;
        return r;
    }

    @Override
    public long sortUnique(@NotNull SortingAlgorithm<int[]> algorithm) {
        final PublicIntSlot slot = new PublicIntSlot(endIndex);
        final long r = algorithm.sortUnique(array, 0, slot);
        endIndex = slot.getInt();
        modCount++;
        return r;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + array.length + ", modCount = " + modCount + ">";
    }
}
