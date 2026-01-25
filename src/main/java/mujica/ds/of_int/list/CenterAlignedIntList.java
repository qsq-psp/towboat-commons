package mujica.ds.of_int.list;

import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

/**
 * Created on 2026/1/5.
 */
public class CenterAlignedIntList extends CopyOnResizeIntList {

    @Index(of = "array")
    int startIndex;

    public CenterAlignedIntList(@Nullable ResizePolicy policy) {
        super(policy);
    }

    CenterAlignedIntList(@NotNull ResizePolicy policy, @NotNull int[] array) {
        super(policy, array);
    }

    @NotNull
    @Override
    public CenterAlignedIntList duplicate() {
        return new CenterAlignedIntList(policy, Arrays.copyOfRange(array, startIndex, endIndex));
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (!(0 <= startIndex && startIndex <= endIndex && endIndex <= array.length)) {
            consumer.accept(new IndexOutOfBoundsException("startIndex = " + startIndex + ", endIndex = " + endIndex + ", capacity = " + array.length));
        }
    }

    @Override
    public boolean isEmpty() {
        return startIndex == endIndex;
    }

    @Override
    public boolean isFull() {
        final int capacity = array.length;
        return startIndex == 0 && endIndex == capacity && policy.nextCapacity(capacity) == capacity;
    }

    @Override
    public int intLength() {
        return endIndex - startIndex;
    }

    @NotNull
    @Override
    public int[] toIntArray() {
        return Arrays.copyOfRange(array, startIndex, endIndex);
    }

    @Override
    public void getRange(int srcOffset, @NotNull int[] dst, int dstOffset, int length) {
        srcOffset = Math.addExact(startIndex, srcOffset);
        if (Math.addExact(srcOffset, length) > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(array, srcOffset, dst, dstOffset, length);
    }

    @Override
    public int getInt(int i) {
        return array[startIndex + i];
    }

    @Override
    public int removeFirst() {
        if (startIndex == endIndex) {
            throw new NoSuchElementException();
        }
        modCount++;
        return array[startIndex++];
    }

    @Override
    public int removeLast() {
        if (startIndex == endIndex) {
            throw new NoSuchElementException();
        }
        modCount++;
        return array[--endIndex];
    }

    @Override
    public int setInt(int i, int t) {
        i = Math.addExact(startIndex, i);
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
        i = Math.addExact(startIndex, i);
        j = Math.addExact(startIndex, j);
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
        for (int i = startIndex; i < endIndex; i++) {
            array[i] = operator.applyAsInt(array[i]);
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            modCount = ++expectedModCount;
        }
    }

    @Override
    public void clear() {
        endIndex = startIndex;
        modCount++;
    }

    @Override
    public void reverse() {
        int i = startIndex;
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
}
