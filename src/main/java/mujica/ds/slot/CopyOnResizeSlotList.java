package mujica.ds.slot;

import mujica.ds.i32.I32;
import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.function.Consumer;
import java.util.function.Predicate;

@CodeHistory(date = "2026/7/9")
public class CopyOnResizeSlotList<S, A> extends SlotList<S, A> {

    @NotNull
    final SlotListAllocator<S, A> allocator;

    @NotNull
    A array;

    @Name(value = "size", language = "en")
    @Index(of = "array", inclusive = false)
    int endIndex;

    @Name(value = "modCount", language = "en")
    int version;

    public CopyOnResizeSlotList(@NotNull SlotListAllocator<S, A> allocator) {
        super();
        this.allocator = allocator;
        this.array = allocator.newArray(allocator.getCapacityPolicy().initialCapacity());
    }

    CopyOnResizeSlotList(@NotNull SlotListAllocator<S, A> allocator, @NotNull A array, int endIndex) {
        super();
        this.allocator = allocator;
        this.array = array;
        this.endIndex = endIndex;
    }

    @NotNull
    @Override
    public CopyOnResizeSlotList<S, A> duplicate() {
        final int newCapacity = Math.max(allocator.getCapacityPolicy().initialCapacity(), endIndex);
        final A newArray = allocator.newArray(newCapacity);
        allocator.copy(array, 0, newArray, 0, endIndex);
        return new CopyOnResizeSlotList<>(allocator, newArray, endIndex);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int capacity = allocator.length(array);
        if (!(0 <= endIndex && endIndex <= capacity)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    @NotNull
    public SlotListAllocator<S, A> getAllocator() {
        return allocator;
    }

    @Override
    public int size() {
        return endIndex;
    }

    @Override
    public boolean isEmpty() {
        return endIndex == 0;
    }

    @Override
    public boolean isFull() {
        final int capacity = allocator.length(array);
        return endIndex == capacity && allocator.getCapacityPolicy().nextCapacity(capacity) == capacity;
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.load(array, index, out);
    }

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.store(array, index, in);
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S slot) throws IndexOutOfBoundsException {
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.exchange(array, index, slot);
    }

    @Override
    public void removeItemAt(int index, @Nullable S slot) throws IndexOutOfBoundsException {
        final int endLength = endIndex - index - 1;
        if (endLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (slot != null) {
            allocator.load(array, index, slot);
        }
        if (endLength > 0) {
            allocator.copySelf(array, index + 1, index, endLength);
        }
        allocator.releaseReference(array, --endIndex);
        version++;
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        // ensure one end room
        final int capacity = allocator.length(array);
        if (endIndex == capacity) {
            int newCapacity = allocator.getCapacityPolicy().nextLargerCapacity(capacity);
            A newArray = allocator.newArray(newCapacity);
            allocator.copy(array, 0, newArray, 0, endIndex);
            array = newArray;
        }
        final int endLength = endIndex - gapIndex;
        if (endLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (endLength > 0) {
            allocator.copySelf(array, gapIndex, gapIndex + 1, endLength);
        }
        allocator.store(array, gapIndex, in);
        version++;
    }

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        final int expectedVersion = version;
        for (int index = 0; index < endIndex; index++) {
            allocator.load(array, index, tempSlot);
            consumer.accept(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        final int expectedVersion = version;
        for (int index = 0; index < endIndex; index++) {
            allocator.load(array, index, tempSlot);
            consumer.accept(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            allocator.store(array, index, tempSlot);
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        final int expectedVersion = version;
        for (int index = 0; index < endIndex; index++) {
            allocator.load(array, index, tempSlot);
            boolean predicateResult = predicate.test(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            if (predicateResult) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        final int expectedVersion = version;
        for (int index = 0; index < endIndex; index++) {
            allocator.load(array, index, tempSlot);
            boolean predicateResult = predicate.test(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            if (!predicateResult) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        for (int index = endIndex - 1; index >= 0; index--) {
            allocator.releaseReference(array, index);
        }
        endIndex = 0;
        version++;
    }

    @Override
    public void reverse() {
        int lowIndex = 0;
        int highIndex = endIndex - 1;
        while (lowIndex < highIndex) {
            allocator.exchange(array, lowIndex, highIndex);
            lowIndex++;
            highIndex--;
        }
        version++;
    }

    @Override
    public long sort(@NotNull Sort<A> sort) {
        final long operationCount = sort.sort(array, 0, endIndex);
        version++;
        return operationCount;
    }

    @Override
    public long sortPart(@NotNull Sort<A> sort, int newEndIndex) {
        if (!(0 <= newEndIndex && newEndIndex <= endIndex)) {
            throw new IndexOutOfBoundsException();
        }
        final long operationCount = sort.sortPart(array, 0, newEndIndex, endIndex);
        endIndex = newEndIndex;
        version++;
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull Sort<A> sort) {
        final I32 endSlot = new I32(endIndex);
        final long operationCount = sort.sortUnique(array, 0, endSlot);
        endIndex = endSlot.getI32();
        version++;
        return operationCount;
    }

    @Override
    public void getRange(int srcIndex, @NotNull A dst, int dstIndex, int length) {
        if (srcIndex < 0 || length < 0 || Math.addExact(srcIndex, length) > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.copy(array, srcIndex, dst, dstIndex, length);
    }

    @Override
    public void getAll(@NotNull A dst, int dstIndex) {
        allocator.copy(array, 0, dst, dstIndex, endIndex);
    }
}
