package mujica.ds.of_boolean.list;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.ds.of_int.list.TwiceResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2026/3/13")
public class CopyOnResizeBooleanList extends AbstractBooleanList {

    private static final long serialVersionUID = 0xE04C062731DD528CL;

    @NotNull
    final ResizePolicy policy;

    @NotNull
    boolean[] array;

    @Index(of = "array", inclusive = false)
    int endIndex;

    int modCount;

    public CopyOnResizeBooleanList(@Nullable ResizePolicy policy) {
        super();
        if (policy == null) {
            policy = TwiceResizePolicy.INSTANCE;
        }
        this.policy = policy;
        this.array = new boolean[policy.initialCapacity()];
    }

    CopyOnResizeBooleanList(@NotNull ResizePolicy policy, @NotNull boolean[] array) {
        super();
        this.policy = policy;
        this.array = array;
        this.endIndex = array.length;
    }

    @NotNull
    @Override
    public CopyOnResizeBooleanList duplicate() {
        return new CopyOnResizeBooleanList(policy, Arrays.copyOf(array, endIndex));
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
    public int booleanLength() {
        return endIndex;
    }

    @NotNull
    @Override
    public boolean[] toBooleanArray() {
        return Arrays.copyOf(array, endIndex);
    }

    @Override
    public void getRange(int srcOffset, @NotNull boolean[] dst, int dstOffset, int length) {
        if (Math.addExact(srcOffset, length) > this.endIndex) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(array, srcOffset, dst, dstOffset, length);
    }

    @Override
    public boolean getBoolean(int i) {
        if (i > endIndex) {
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
    public boolean offerAt(int i, boolean t) {
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
    public boolean offerLast(boolean t) {
        if (ensureOneSlot()) {
            array[endIndex++] = t;
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeAt(int i) {
        if (i >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        final boolean removed = array[i];
        System.arraycopy(array, i + 1, array, i, endIndex - i - 1);
        endIndex--;
        modCount++;
        return removed;
    }

    @Override
    public boolean removeLast() {
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
}
