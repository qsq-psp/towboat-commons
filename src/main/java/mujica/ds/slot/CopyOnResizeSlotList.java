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
public class CopyOnResizeSlotList<S, A> extends VersionSlotList<S, A> {

    @NotNull
    final SlotArrayAllocator.PolicySAA<S, A> allocator;

    @NotNull
    A array;

    @Name(value = "size", language = "en")
    @Index(of = "array", inclusive = false)
    int endIndex;

    public CopyOnResizeSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator) {
        super();
        this.allocator = allocator;
        this.array = allocator.newArray(allocator.getCapacityPolicy().initialCapacity());
    }

    CopyOnResizeSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator, @NotNull A array, int endIndex) {
        super();
        this.allocator = allocator;
        this.array = array;
        this.endIndex = endIndex;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new CopyOnResizeSlotList<>(allocator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        final int newCapacity = Math.max(allocator.getCapacityPolicy().min(), endIndex);
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
    public SlotArrayAllocator.PolicySAA<S, A> getAllocator() {
        return allocator;
    }

    @Override
    public int getSize() {
        return endIndex;
    }

    @Override
    public boolean isEmpty() {
        return endIndex == 0;
    }

    @Override
    public boolean isFull() {
        final int capacity = allocator.length(array);
        return endIndex == capacity && allocator.getCapacityPolicy().tryLargerCapacity(capacity) <= capacity;
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
    public void exchangeItemAt(int index, @NotNull S inOut) throws IndexOutOfBoundsException {
        if (index >= endIndex) {
            throw new IndexOutOfBoundsException();
        }
        allocator.exchange(array, index, inOut);
    }

    @Override
    public void removeItemAt(int index, @Nullable S out) throws IndexOutOfBoundsException {
        final int endLength = endIndex - index - 1;
        if (endLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (out != null) {
            allocator.load(array, index, out);
        }
        if (endLength > 0) {
            allocator.copyWithin(array, index + 1, index, endLength);
        }
        allocator.releaseReference(array, --endIndex);
        version++;
    }

    private void enlarge(int newCapacity) {
        final A newArray = allocator.newArray(newCapacity);
        allocator.copy(array, 0, newArray, 0, endIndex);
        allocator.releaseArray(array);
        array = newArray;
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        // ensure one end room
        final int capacity = allocator.length(array);
        if (endIndex == capacity) {
            enlarge(allocator.getCapacityPolicy().largerCapacity(capacity));
        }
        final int endLength = endIndex - gapIndex;
        if (endLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (endLength > 0) {
            allocator.copyWithin(array, gapIndex, gapIndex + 1, endLength);
        }
        allocator.store(array, gapIndex, in);
        endIndex++;
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
        final int newEndIndex = Math.addExact(endIndex, transferLength);
        final int capacity = allocator.length(array);
        if (newEndIndex > capacity) {
            enlarge(allocator.getCapacityPolicy().notSmallerCapacity(newEndIndex));
        }
        allocator.copy(thatArray, thatStartIndex, array, endIndex, transferLength);
        endIndex = newEndIndex;
        version++;
    }

    @Override
    public void insertLastSelf(int srcStartIndex, int srcEndIndex) {
        if (!(0 <= srcStartIndex && srcStartIndex <= srcEndIndex && srcEndIndex <= endIndex)) {
            throw new IndexOutOfBoundsException();
        }
        final int transferLength = srcEndIndex - srcStartIndex;
        if (transferLength == 0) {
            return;
        }
        final int newEndIndex = Math.addExact(endIndex, transferLength);
        final int capacity = allocator.length(array);
        if (newEndIndex > capacity) {
            enlarge(allocator.getCapacityPolicy().notSmallerCapacity(newEndIndex));
        }
        allocator.copyWithin(array, srcStartIndex, endIndex, transferLength);
        endIndex = newEndIndex;
        version++;
    }

    @Override
    public void appendTo(@NotNull SlotCollection<S, A> that) {
        that.insertLast(array, 0, endIndex);
    }

    @Override
    public void clear() {
        allocator.releaseReference(array, 0, endIndex);
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

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        final int expectedVersion = version;
        for (int index = 0; index < endIndex; index++) {
            allocator.load(array, index, consumedSlot);
            consumer.accept(consumedSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T consumedSlot) {
        final int expectedVersion = version;
        for (int index = 0; index < endIndex; index++) {
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
        for (int index = 0; index < endIndex; index++) {
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

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        final int expectedVersion = version;
        for (int index = 0; index < endIndex; index++) {
            allocator.load(array, index, consumedSlot);
            consumer.accept(consumedSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            allocator.store(array, index, consumedSlot);
        }
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "size = " + endIndex + ", capacity = " + allocator.length(array) + ", version = " + version;
    }
}
