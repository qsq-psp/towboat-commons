package mujica.ds.of_int.list;

import mujica.reflect.function.IntEntryPredicate;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * Created on 2025/6/25.
 */
@CodeHistory(date = "2025/6/25")
public class CopyOnResizeIntList extends AbstractIntList {

    @NotNull
    final ResizePolicy policy;

    @NotNull
    int[] array;

    int length;

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
        this.length = array.length;
    }

    @NotNull
    @Override
    public CopyOnResizeIntList duplicate() {
        return new CopyOnResizeIntList(policy, Arrays.copyOf(array, length));
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (length < 0 || length > array.length) {
            consumer.accept(new IndexOutOfBoundsException("length = " + length + ", capacity = " + array.length));
        }
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public boolean isFull() {
        final int capacity = array.length;
        return length == capacity && policy.nextCapacity(capacity) == capacity;
    }

    @Override
    public int intLength() {
        return length;
    }

    @NotNull
    @Override
    public int[] toArray() {
        return Arrays.copyOf(array, length);
    }

    @Override
    public void getRange(int srcOffset, @NotNull int[] dst, int dstOffset, int length) {
        if (srcOffset + length > this.length) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(array, srcOffset, dst, dstOffset, length);
    }

    @Override
    public int getInt(int i) {
        if (i >= length) {
            throw new IndexOutOfBoundsException();
        }
        return array[i];
    }

    boolean ensureOneSlot() {
        if (length == array.length) {
            int capacity = policy.nextCapacity(length);
            if (length == capacity) {
                return false;
            }
            array = Arrays.copyOf(array, capacity);
        }
        return true;
    }

    @Override
    public boolean offerAt(int i, int t) {
        if (i < 0 || length < i) {
            throw new IndexOutOfBoundsException();
        }
        if (ensureOneSlot()) {
            System.arraycopy(array, i, array, i + 1, length - i);
            array[i] = t;
            length++;
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean offerLast(int t) {
        if (ensureOneSlot()) {
            array[length++] = t;
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int removeAt(int i) {
        if (i >= length) {
            throw new IndexOutOfBoundsException();
        }
        final int removed = array[i];
        System.arraycopy(array, i + 1, array, i, length - i - 1);
        length--;
        modCount++;
        return removed;
    }

    @Override
    public int removeLast() {
        if (length == 0) {
            throw new NoSuchElementException();
        }
        modCount++;
        return array[--length];
    }

    @Override
    public void removeRange(int fi, int ti) {
        if (fi < 0 || length < ti || fi > ti) {
            throw new IndexOutOfBoundsException();
        }
        if (fi == ti) {
            return;
        }
        System.arraycopy(array, ti, array, fi, length - ti);
        length -= ti - fi;
        modCount++;
    }

    @Override
    public void removeIf(@NotNull IntPredicate filter) {
        if (length == 0) {
            return;
        }
        final byte[] retain = new byte[length];
        final int expectedModCount = modCount;
        for (int i = 0; i < length; i++) {
            if (!filter.test(array[i])) {
                retain[i] = 1;
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
        int n = 0;
        for (int i = 0; i < length; i++) {
            if (retain[i] != 0) {
                array[n++] = array[i];
            }
        }
        length = n;
        modCount++;
    }

    @Override
    public void removeIf(@NotNull IntEntryPredicate filter) {
        if (length == 0) {
            return;
        }
        final byte[] retain = new byte[length];
        final int expectedModCount = modCount;
        for (int i = 0; i < length; i++) {
            if (!filter.test(i, array[i])) {
                retain[i] = 1;
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
        int n = 0;
        for (int i = 0; i < length; i++) {
            if (retain[i] != 0) {
                array[n++] = array[i];
            }
        }
        length = n;
        modCount++;
    }

    void unsafeRemoveIf(@NotNull IntPredicate filter) {
        int n = 0;
        for (int i = 0; i < length; i++) {
            if (filter.test(array[i])) {
                continue;
            }
            array[n++] = array[i];
        }
        length = n;
    }

    void unsafeRemoveIf(@NotNull IntEntryPredicate filter) {
        int n = 0;
        for (int i = 0; i < length; i++) {
            if (filter.test(i, array[i])) {
                continue;
            }
            array[n++] = array[i];
        }
        length = n;
    }

    @Override
    public int setInt(int i, int t) {
        if (i >= length) {
            throw new IndexOutOfBoundsException();
        }
        final int s = array[i];
        array[i] = t;
        modCount++;
        return s;
    }

    @Override
    public void swap(int i, int j) {
        if (Math.max(i, j) >= length) {
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
        for (int i = 0; i < length; i++) {
            array[i] = operator.applyAsInt(array[i]);
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            modCount = ++expectedModCount;
        }
    }

    @Override
    public void clear() {
        length = 0;
        modCount++;
    }

    @Override
    public void reverse() {
        int i = 0;
        int j = length - 1;
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
        final int n = array.length;
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
        // todo
    }

    @Override
    public void sort(boolean descending) {
        Arrays.sort(array, 0, length);
        if (descending) {
            reverse(); // modCount included
        } else {
            modCount++;
        }
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + array.length + ", modCount = " + modCount + ">";
    }
}
