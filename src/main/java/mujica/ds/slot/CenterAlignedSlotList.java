package mujica.ds.slot;

import mujica.ds.i32.I32;
import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.function.Consumer;
import java.util.function.Predicate;

@CodeHistory(date = "2026/1/5", name = "CenterAlignedIntList")
@CodeHistory(date = "2026/7/10")
@DirectSubclass({CyclicSlotList.class})
public class CenterAlignedSlotList<S, A> extends CopyOnResizeSlotList<S, A> {

    @Index(of = "array")
    int startIndex;

    public CenterAlignedSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator) {
        super(allocator);
        final int halfCapacity = allocator.length(array) >>> 1;
        startIndex = halfCapacity;
        endIndex = halfCapacity;
    }

    CenterAlignedSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator, @SuppressWarnings("unused") Void doNotInitializeIndexes) {
        super(allocator);
    }

    CenterAlignedSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator, @NotNull A array, int startIndex, int endIndex) {
        super(allocator, array, endIndex);
        this.startIndex = startIndex;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new CenterAlignedSlotList<>(allocator);
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
    public int getSize() {
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
        return endIndex == capacity && allocator.getCapacityPolicy().tryLargerCapacity(capacity) == capacity;
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
    public void exchangeItemAt(int index, @NotNull S inOut) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.exchange(array, index, inOut);
    }

    @Override
    public void removeItemAt(int index, @Nullable S out) throws IndexOutOfBoundsException {
        final int startLength = index;
        if (startLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        final int endLength = endIndex - index - 1;
        if (endLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (out != null) {
            allocator.load(array, index, out);
        }
        if (startLength < endLength) {
            if (startLength > 0) {
                allocator.copyWithin(array, startIndex, startIndex + 1, startLength);
            }
            allocator.releaseReference(array, startIndex++);
        } else {
            if (endLength > 0) {
                allocator.copyWithin(array, index + 1, index, endLength);
            }
            allocator.releaseReference(array, --endIndex);
            if (startLength == 0) {
                // now startLength == 0 && endLength == 0
                int halfCapacity = allocator.length(array) >>> 1;
                startIndex = halfCapacity;
                endIndex = halfCapacity;
            }
        }
        version++;
    }

    private void enlargeArray(int startRemaining, int endRemaining) {
        final int listSize = endIndex - startIndex;
        final int minimumCapacity = startRemaining + listSize + endRemaining;
        final int newCapacity = allocator.getCapacityPolicy().notSmallerCapacity(minimumCapacity);
        final int newStartIndex = ((newCapacity - minimumCapacity) >>> 1) + startRemaining;
        final A newArray = allocator.newArray(newCapacity);
        allocator.copy(array, startIndex, newArray, newStartIndex, listSize);
        allocator.releaseArray(array);
        array = newArray;
        startIndex = newStartIndex;
        endIndex = newStartIndex + listSize;
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        final int startLength = gapIndex;
        if (startLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        gapIndex += startIndex;
        final int endLength = endIndex - gapIndex;
        if (endLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (startLength < endLength) {
            if (startIndex == 0) {
                enlargeArray(1, 0);
                assert startIndex > 0;
            }
            gapIndex = startIndex + startLength - 1;
            if (startLength > 0) {
                allocator.copyWithin(array, startIndex, startIndex - 1, startLength);
            }
            startIndex--;
        } else {
            int capacity = allocator.length(array);
            if (endIndex == capacity) {
                enlargeArray(0, 1);
                assert endIndex < allocator.length(array);
            }
            gapIndex = endIndex - endLength;
            if (endLength > 0) {
                allocator.copyWithin(array, gapIndex, gapIndex + 1, endLength);
            }
            endIndex++;
        }
        allocator.store(array, gapIndex, in);
        version++;
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        if (startIndex == 0) {
            enlargeArray(1, 0);
            assert startIndex > 0;
        }
        allocator.store(array, --startIndex, in);
        version++;
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        final int capacity = allocator.length(array);
        if (endIndex == capacity) {
            enlargeArray(0, 1);
            assert endIndex < allocator.length(array);
        }
        allocator.store(array, endIndex++, in);
        version++;
    }

    @Override
    public void insertLast(@NotNull A thatArray, int thatStartIndex, int thatEndIndex) {
        final int transferLength = Math.subtractExact(thatEndIndex, thatStartIndex);
        if (transferLength <= 0) {
            if (transferLength == 0) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        final int capacity = allocator.length(array);
        if (Math.addExact(endIndex, transferLength) > capacity) {
            enlargeArray(0, transferLength);
        }
        allocator.copy(thatArray, thatStartIndex, array, endIndex, transferLength);
        endIndex += transferLength;
        version++;
    }

    @Override
    public void appendTo(@NotNull SlotCollection<S, A> that) {
        that.insertLast(array, startIndex, endIndex);
    }

    @Override
    public void removeRange(int intervalStartIndex, int intervalEndIndex) throws IndexOutOfBoundsException {
        if (intervalStartIndex >= intervalEndIndex) {
            if (intervalStartIndex == intervalEndIndex) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        final int startLength = intervalStartIndex;
        if (startLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        intervalStartIndex += startIndex;
        intervalEndIndex += endIndex;
        final int endLength = endIndex - intervalEndIndex;
        if (endLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (startLength < endLength) {
            int newStartIndex = startIndex + intervalEndIndex - intervalStartIndex;
            if (startLength > 0) {
                allocator.copyWithin(array, startIndex, newStartIndex, startLength);
            }
            allocator.releaseReference(array, startIndex, newStartIndex);
            startIndex = newStartIndex;
        } else {
            int newEndIndex = endIndex + intervalStartIndex - intervalEndIndex;
            if (endLength > 0) {
                allocator.copyWithin(array, intervalEndIndex, intervalStartIndex, endLength);
            }
            allocator.releaseReference(array, newEndIndex, endIndex);
            endIndex = newEndIndex;
        }
        version++;
    }

    @Override
    public void setSize(int newEndIndex) throws IndexOutOfBoundsException {
        if (newEndIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        newEndIndex += startIndex;
        if (newEndIndex >= endIndex) {
            if (newEndIndex == endIndex) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        allocator.releaseReference(array, newEndIndex, endIndex);
        endIndex = newEndIndex;
        version++;
    }

    @Override
    public void clear() {
        allocator.releaseReference(array, startIndex, endIndex);
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
        srcIndex = Math.addExact(startIndex, srcIndex);
        if (Math.addExact(srcIndex, length) > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.copy(array, srcIndex, dst, dstIndex, length);
    }

    @Override
    public void getAll(@NotNull A dst, int dstIndex) {
        allocator.copy(array, startIndex, dst, dstIndex, endIndex - startIndex);
    }

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        final int expectedVersion = version;
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(array, index, consumedSlot);
            consumer.accept(consumedSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        final int expectedVersion = version;
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(array, index, consumedSlot);
            consumer.accept(consumedSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            allocator.store(array, index, consumedSlot);
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T consumedSlot) {
        final int expectedVersion = version;
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(array, index, consumedSlot);
            boolean predicateResult = predicate.test(consumedSlot);
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
    public <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T consumedSlot) {
        final int expectedVersion = version;
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(array, index, consumedSlot);
            boolean predicateResult = predicate.test(consumedSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            if (!predicateResult) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "size = " + getSize() + ", start = " + startIndex + ", end = " + endIndex + ", capacity = " + allocator.length(array) + ", version = " + version;
    }
}
