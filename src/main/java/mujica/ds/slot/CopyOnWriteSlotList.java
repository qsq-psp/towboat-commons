package mujica.ds.slot;

import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@CodeHistory(date = "2026/7/12")
public class CopyOnWriteSlotList<S, A> extends RandomAccessBasedSlotCollection<S, A> {

    @NotNull
    final SlotArrayAllocator<S, A> allocator;

    @NotNull
    volatile A array;

    public CopyOnWriteSlotList(@NotNull SlotArrayAllocator<S, A> allocator) {
        super();
        this.allocator = allocator;
        this.array = allocator.newArray(0);
    }

    CopyOnWriteSlotList(@NotNull SlotArrayAllocator<S, A> allocator, @NotNull A array) {
        super();
        this.allocator = allocator;
        this.array = array;
    }

    @NotNull
    @Override
    public CopyOnWriteSlotList<S, A> duplicate() {
        return new CopyOnWriteSlotList<>(allocator, array);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (allocator.length(array) < 0) {
            consumer.accept(new RuntimeException("negative array length"));
        }
    }

    @NotNull
    @Override
    protected SlotArrayAllocator<S, A> getAllocator() {
        return allocator;
    }

    @Override
    public int size() {
        return allocator.length(array);
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        allocator.load(array, index, out);
    }

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        final A oldArray = array;
        final A newArray = allocator.cloneArray(oldArray);
        allocator.store(newArray, index, in);
        array = newArray;
        allocator.releaseArray(oldArray);
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S slot) throws IndexOutOfBoundsException {
        final A oldArray = array;
        final A newArray = allocator.cloneArray(oldArray);
        allocator.exchange(newArray, index, slot);
        array = newArray;
        allocator.releaseArray(oldArray);
    }

    @Override
    public void removeItemAt(int index, @Nullable S slot) throws IndexOutOfBoundsException {
        final A oldArray = array;
        final int oldLength = allocator.length(oldArray);
        if (index < 0 || index >= oldLength) {
            throw new IndexOutOfBoundsException();
        }
        final A newArray = allocator.newArray(oldLength - 1);
        allocator.copy(oldArray, 0, newArray, 0, index);
        allocator.copy(oldArray, index + 1, newArray, index, oldLength - index - 1);
        array = newArray;
        allocator.releaseArray(oldArray);
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        final A oldArray = array;
        final int oldLength = allocator.length(oldArray);
        if (gapIndex < 0 || gapIndex > oldLength) {
            throw new IndexOutOfBoundsException();
        }
        final A newArray = allocator.newArray(oldLength + 1);
        if (gapIndex > 0) {
            allocator.copy(oldArray, 0, newArray, 0, gapIndex);
        }
        if (gapIndex < oldLength) {
            allocator.copy(oldArray, gapIndex, newArray, gapIndex + 1, oldLength - gapIndex);
        }
        array = newArray;
        allocator.releaseArray(oldArray);
    }

    @Override
    public void insertLast(@NotNull A thatArray, int startIndex, int endIndex) {
        final int transferLength = Math.subtractExact(endIndex, startIndex);
        if (transferLength <= 0) {
            if (transferLength == 0) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        final A oldArray = array;
        final int oldLength = allocator.length(oldArray);
        final A newArray = allocator.newArray(Math.addExact(oldLength, transferLength));
        allocator.copy(oldArray, 0, newArray, 0, oldLength);
        allocator.copy(thatArray, startIndex, newArray, oldLength, transferLength);
        array = newArray;
        allocator.releaseArray(oldArray);
    }

    @Override
    public void insertLastSelf(int startIndex, int endIndex) {
        insertLast(array, startIndex, endIndex);
    }

    @Override
    public void appendTo(@NotNull SlotCollection<S, A> that) {
        that.insertLast(array);
    }

    @Override
    public void removeRange(int startIndex, int endIndex) throws IndexOutOfBoundsException {
        final A oldArray = array;
        final int oldLength = allocator.length(oldArray);
        if (!(0 <= startIndex && startIndex <= endIndex && endIndex <= oldLength)) {
            throw new IndexOutOfBoundsException();
        }
        if (startIndex == endIndex) {
            return;
        }
        final A newArray = allocator.newArray(oldLength + startIndex - endIndex);
        if (0 < startIndex) {
            allocator.copy(oldArray, 0, newArray, 0, startIndex);
        }
        if (endIndex < oldLength) {
            allocator.copy(oldArray, endIndex, newArray, startIndex, oldLength - endIndex);
        }
        array = newArray;
        allocator.releaseArray(oldArray);
    }

    @Override
    public void truncate(int newLength) throws IndexOutOfBoundsException {
        final A oldArray = array;
        final int oldLength = allocator.length(oldArray);
        if (!(0 <= newLength && newLength <= oldLength)) {
            throw new IndexOutOfBoundsException();
        }
        if (newLength == oldLength) {
            return;
        }
        final A newArray = allocator.newArray(newLength);
        if (newLength > 0) {
            allocator.copy(oldArray, 0, newArray, 0, newLength);
        }
        array = newArray;
        allocator.releaseArray(oldArray);
    }

    @Override
    public void clear() {
        final A oldArray = array;
        array = allocator.newArray(0);
        allocator.releaseArray(oldArray);
    }

    @Override
    public void reverse() {
        final A oldArray = array;
        final int arrayLength = allocator.length(oldArray);
        if (arrayLength < 2) {
            return;
        }
        final S tempSlot = allocator.newSlot();
        final A newArray = allocator.newArray(arrayLength);
        {
            int newIndex = arrayLength;
            for (int oldIndex = 0; oldIndex < arrayLength; oldIndex++) {
                allocator.load(oldArray, oldIndex, tempSlot);
                allocator.store(oldArray, --newIndex, tempSlot);
            }
        }
        array = newArray;
        allocator.releaseSlot(tempSlot);
        allocator.releaseArray(oldArray);
    }

    @Override
    public long sort(@NotNull Sort<A> sort) {
        final A oldArray = array;
        final A newArray = allocator.cloneArray(oldArray);
        final long operationCount = sort.sort(newArray);
        array = newArray;
        allocator.releaseArray(oldArray);
        return operationCount;
    }

    @Override
    public void getRange(int srcIndex, @NotNull A dstArray, int dstIndex, int length) {
        allocator.copy(array, srcIndex, dstArray, dstIndex, length);
    }

    @NotNull
    @Override
    public A allToArray() {
        return allocator.cloneArray(array);
    }

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        final A array = this.array;
        final int arrayLength = allocator.length(array);
        for (int index = 0; index < arrayLength; index++) {
            allocator.load(array, index, tempSlot);
            consumer.accept(tempSlot);
        }
    }
}
