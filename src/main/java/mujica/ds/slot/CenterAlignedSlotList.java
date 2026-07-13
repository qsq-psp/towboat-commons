package mujica.ds.slot;

import mujica.ds.i32.I32;
import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

@CodeHistory(date = "2026/1/5", name = "CenterAlignedIntList")
@CodeHistory(date = "2026/7/10")
@DirectSubclass({CyclicSlotList.class})
public class CenterAlignedSlotList<S, A> extends CopyOnResizeSlotList<S, A> {

    @Index(of = "array")
    int startIndex;

    public CenterAlignedSlotList(@NotNull SlotListAllocator<S, A> allocator) {
        super(allocator);
    }

    CenterAlignedSlotList(@NotNull SlotListAllocator<S, A> allocator, @NotNull A array, int startIndex, int endIndex) {
        super(allocator, array, endIndex);
        this.startIndex = startIndex;
    }

    @NotNull
    @Override
    public CenterAlignedSlotList<S, A> duplicate() {
        final int listSize = endIndex - startIndex;
        final int newCapacity = Math.max(allocator.getCapacityPolicy().initialCapacity(), listSize);
        final A newArray = allocator.newArray(newCapacity);
        final int newStartIndex = (newCapacity - listSize) / 2;
        allocator.copy(array, startIndex, newArray, newStartIndex, listSize);
        return new CenterAlignedSlotList<>(allocator, newArray, newStartIndex, newStartIndex + listSize);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int capacity = allocator.length(array);
        if (!(0 <= startIndex && startIndex <= endIndex && endIndex <= capacity)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        return endIndex - startIndex;
    }

    @Override
    public boolean isEmpty() {
        return startIndex == endIndex;
    }

    @Override
    public boolean isFull() {
        if (startIndex != 0) {
            return false;
        }
        final int capacity = allocator.length(array);
        return endIndex == capacity && allocator.getCapacityPolicy().nextCapacity(capacity) == capacity;
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.load(array, index, out);
    }

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.store(array, index, in);
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S slot) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.exchange(array, index, slot);
    }

    @Override
    public void removeItemAt(int index, @Nullable S slot) throws IndexOutOfBoundsException {
        final int startLength = index;
        if (startLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        final int endLength = endIndex - index - 1;
        if (endLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (slot != null) {
            allocator.load(array, index, slot);
        }
        if (startLength < endLength) {
            if (startLength > 0) {
                allocator.copySelf(array, startIndex, startIndex + 1, startLength);
            }
            allocator.releaseReference(array, startIndex++);
        } else {
            if (endLength > 0) {
                allocator.copySelf(array, index + 1, index, endLength);
            }
            allocator.releaseReference(array, --endIndex);
        }
        version++;
    }

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(array, index, tempSlot);
            consumer.accept(tempSlot);
        }
    }

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(array, index, tempSlot);
            consumer.accept(tempSlot);
            allocator.store(array, index, tempSlot);
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(array, index, tempSlot);
            if (predicate.test(tempSlot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(array, index, tempSlot);
            if (!predicate.test(tempSlot)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        for (int index = startIndex; index < endIndex; index++) {
            allocator.releaseReference(array, index);
        }
        final int halfCapacity = allocator.length(array) >>> 1;
        startIndex = halfCapacity;
        endIndex = halfCapacity;
        version++;
    }

    @Override
    public long sort(@NotNull Sort<A> sort) {
        return sort.sort(array, startIndex, endIndex);
    }

    @Override
    public long sortPart(@NotNull Sort<A> sort, int newEndIndex) {
        if (newEndIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        newEndIndex += startIndex;
        if (newEndIndex > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        final long operationCount = sort.sortPart(array, startIndex, newEndIndex, endIndex);
        endIndex = newEndIndex;
        version++;
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull Sort<A> sort) {
        final I32 endSlot = new I32(endIndex);
        final long operationCount = sort.sortUnique(array, startIndex, endSlot);
        endIndex = endSlot.getI32();
        version++;
        return operationCount;
    }

    @Override
    public void getRange(int srcIndex, @NotNull A dst, int dstIndex, int length) {
        if (srcIndex < 0 || length < 0) {
            throw new IndexOutOfBoundsException();
        }
        srcIndex = Math.addExact(srcIndex, length);
        if (Math.addExact(srcIndex, length) > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.copy(array, srcIndex, dst, dstIndex, length);
    }

    @Override
    public void getAll(@NotNull A dst, int dstIndex) {
        allocator.copy(array, startIndex, dst, dstIndex, endIndex - startIndex);
    }
}
