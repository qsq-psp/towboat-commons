package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2026/7/11")
@DirectSubclass({CyclicRewriteSlotList.class})
public class CyclicSlotList<S, A> extends CenterAlignedSlotList<S, A> {

    boolean empty;

    public CyclicSlotList(@NotNull SlotArrayAllocator.WithPolicy<S, A> allocator) {
        super(allocator, null);
        empty = true;
    }

    CyclicSlotList(@NotNull SlotArrayAllocator.WithPolicy<S, A> allocator, @NotNull A array, int startIndex, int endIndex) {
        super(allocator, array, startIndex, endIndex);
    }

    @NotNull
    @Override
    public CyclicSlotList<S, A> duplicate() {
        if (empty) {
            return new CyclicSlotList<>(allocator);
        } else {
            return new CyclicSlotList<>(allocator, allocator.cloneArray(array), startIndex, endIndex);
        }
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (empty) {
            return;
        }
        final int capacity = allocator.length(array);
        if (!(0 <= startIndex && startIndex < capacity && 0 <= endIndex && endIndex < capacity)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        if (empty) {
            return 0;
        }
        int listSize = endIndex - startIndex;
        if (listSize > 0) {
            return listSize;
        }
        listSize += allocator.length(array);
        assert listSize > 0;
        return listSize;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean isFull() {
        if (empty) {
            return false;
        }
        if (startIndex == endIndex) {
            return false;
        }
        final int capacity = allocator.length(array);
        return allocator.getCapacityPolicy().nextCapacity(capacity) == capacity;
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (empty || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int listSize = endIndex - startIndex;
        if (listSize > 0) {
            index += startIndex;
            if (index >= endIndex) {
                throw new IndexOutOfBoundsException();
            }
            allocator.load(array, index, out);
            return;
        }
        final int capacity = allocator.length(array);
        listSize += capacity;
        if (index >= listSize) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        if (index >= capacity) {
            index -= capacity;
        }
        allocator.load(array, index, out);
    }

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        if (empty || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int listSize = endIndex - startIndex;
        if (listSize > 0) {
            index += startIndex;
            if (index >= endIndex) {
                throw new IndexOutOfBoundsException();
            }
            allocator.store(array, index, in);
            return;
        }
        final int capacity = allocator.length(array);
        listSize += capacity;
        if (index >= listSize) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        if (index >= capacity) {
            index -= capacity;
        }
        allocator.store(array, index, in);
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S slot) throws IndexOutOfBoundsException {
        if (empty || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int listSize = endIndex - startIndex;
        if (listSize > 0) {
            index += startIndex;
            if (index >= endIndex) {
                throw new IndexOutOfBoundsException();
            }
            allocator.exchange(array, index, slot);
            return;
        }
        final int capacity = allocator.length(array);
        listSize += capacity;
        if (index >= listSize) {
            throw new IndexOutOfBoundsException();
        }
        index += startIndex;
        if (index >= capacity) {
            index -= capacity;
        }
        allocator.exchange(array, index, slot);
    }

    @Override
    public void clear() {
        if (empty) {
            return;
        }
        if (startIndex < endIndex) {
            allocator.releaseReference(array, startIndex, endIndex);
        } else {
            int capacity = allocator.length(array);
            int index = startIndex;
            do {
                allocator.releaseReference(array, index);
                index++;
                if (index >= capacity) {
                    index = 0;
                }
            } while (index != endIndex);
        }
        empty = true;
        version++;
    }
}
