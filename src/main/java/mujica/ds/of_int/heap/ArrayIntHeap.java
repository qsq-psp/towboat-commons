package mujica.ds.of_int.heap;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2024/1/15", project = "Ultramarine", name = "IntHeap")
@CodeHistory(date = "2025/3/29")
public abstract class ArrayIntHeap extends AbstractOrderedIntQueue {

    private static final long serialVersionUID = 0x1e017ba1849e7e12L;

    @NotNull
    int[] array;

    protected ArrayIntHeap(@NotNull int[] array) {
        super();
        this.array = array;
        this.endIndex = startIndex();
    }

    protected ArrayIntHeap(int initialCapacity) {
        this(new int[initialCapacity]);
    }

    protected ArrayIntHeap() {
        this(INITIAL_CAPACITY);
    }

    @NotNull
    @Override
    public abstract ArrayIntHeap duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    protected abstract int startIndex();

    protected void ensureCapacity(int minCapacity) {
        final int capacity = array.length;
        if (minCapacity <= capacity) {
            return;
        }
        array = Arrays.copyOf(array, Math.max(minCapacity, capacity << 1));
    }

    @Override
    public int intLength() {
        return endIndex - startIndex();
    }

    @Override
    public boolean isEmpty() {
        return startIndex() == endIndex;
    }

    @Override
    public abstract void offer(int t);

    public abstract void removeRoot();

    @Override
    public int remove() throws NoSuchElementException {
        final int index = startIndex();
        if (index < endIndex) {
            int removed = array[index];
            removeRoot();
            return removed;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int poll(int fallback) {
        final int index = startIndex();
        if (index < endIndex) {
            int removed = array[index];
            removeRoot();
            return removed;
        } else {
            return fallback;
        }
    }

    @Override
    public int element() throws NoSuchElementException {
        final int index = startIndex();
        if (index < endIndex) {
            return array[index];
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int peek(int fallback) {
        final int index = startIndex();
        if (index < endIndex) {
            return array[index];
        } else {
            return fallback;
        }
    }

    @Override
    public void clear() {
        endIndex = startIndex();
    }

    @NotNull
    @Override
    public int[] toArray() {
        return Arrays.copyOfRange(array, startIndex(), endIndex);
    }

    @Override
    public void forEach(@NotNull IntConsumer action) {
        final int expectedModCount = modCount;
        final int endIndex = this.endIndex;
        for (int i = startIndex(); i < endIndex; i++) {
            action.accept(array[i]);
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class SafeIterator implements PrimitiveIterator.OfInt {

        private final int expectedModCount = modCount;

        private int index = startIndex();

        @Override
        public boolean hasNext() {
            return index < endIndex;
        }

        @Override
        public int nextInt() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (index >= endIndex) {
                throw new NoSuchElementException();
            }
            return array[index++];
        }
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new SafeIterator();
    }
}
