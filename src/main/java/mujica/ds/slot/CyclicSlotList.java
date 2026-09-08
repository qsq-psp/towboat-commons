package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@CodeHistory(date = "2026/7/11")
@Name(value = "循环列表", language = "zh")
@DirectSubclass({CyclicRewriteSlotList.class})
public class CyclicSlotList<S, A> extends CenterAlignedSlotList<S, A> {

    boolean empty;

    public CyclicSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator) {
        super(allocator, null);
        empty = true;
    }

    CyclicSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator, @NotNull A array, int startIndex, int endIndex) {
        super(allocator, array, startIndex, endIndex);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new CyclicSlotList<>(allocator);
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
    public int getSize() {
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
        if (startIndex != endIndex) {
            return false;
        }
        final int capacity = allocator.length(array);
        return allocator.getCapacityPolicy().tryLargerCapacity(capacity) == capacity;
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
    public void exchangeItemAt(int index, @NotNull S inOut) throws IndexOutOfBoundsException {
        if (empty || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int listSize = endIndex - startIndex;
        if (listSize > 0) {
            index += startIndex;
            if (index >= endIndex) {
                throw new IndexOutOfBoundsException();
            }
            allocator.exchange(array, index, inOut);
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
        allocator.exchange(array, index, inOut);
    }

    @Override
    public void removeItemAt(int thisIndex, @Nullable S out) throws IndexOutOfBoundsException {
        if (empty || thisIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        int arrayIndex = startIndex + thisIndex;
        if (startIndex < endIndex) {
            int endLength = endIndex - arrayIndex - 1;
            if (endLength < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (out != null) {
                allocator.load(array, arrayIndex, out);
            }
            if (thisIndex < endLength) {
                if (thisIndex > 0) {
                    allocator.copyWithin(array, startIndex, startIndex + 1, thisIndex);
                }
                allocator.releaseReference(array, startIndex++);
            } else {
                if (endLength > 0) {
                    allocator.copyWithin(array, arrayIndex + 1, arrayIndex, endLength);
                }
                allocator.releaseReference(array, --endIndex);
                if (thisIndex == 0) {
                    empty = true;
                }
            }
        } else {
            int capacity = allocator.length(array);
            if (arrayIndex < capacity) {
                if (out != null) {
                    allocator.load(array, arrayIndex, out);
                }
                if (thisIndex > 0) {
                    allocator.copyWithin(array, startIndex, startIndex + 1, thisIndex);
                }
                allocator.releaseReference(array, startIndex);
                startIndex++;
                if (startIndex == capacity) {
                    startIndex = 0;
                    if (startIndex == endIndex) {
                        empty = true;
                    }
                }
            } else {
                arrayIndex -= capacity;
                if (arrayIndex < endIndex) {
                    if (out != null) {
                        allocator.load(array, arrayIndex, out);
                    }
                    if (arrayIndex + 1 < endIndex) {
                        allocator.copyWithin(array, arrayIndex + 1, arrayIndex, endIndex - (arrayIndex + 1));
                    }
                    if (endIndex == 0) {
                        endIndex = capacity - 1;
                        if (startIndex == endIndex) {
                            empty = true;
                        }
                    } else {
                        endIndex--;
                    }
                    allocator.releaseReference(array, endIndex);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            }
        }
        version++;
    }

    private void enlargeArray(int newCapacity) {
        final A newArray = allocator.newArray(newCapacity);
        if (!empty) {
            int listSize = endIndex - startIndex;
            if (listSize > 0) {
                allocator.copy(array, startIndex, newArray, 0, listSize);
            } else {
                int oldCapacity = allocator.length(array);
                listSize += oldCapacity;
                assert listSize > 0;
                allocator.copy(array, startIndex, newArray, 0, oldCapacity - startIndex);
                allocator.copy(array, 0, newArray, oldCapacity - startIndex, endIndex);
                startIndex = 0;
                endIndex = oldCapacity;
            }
        }
        array = newArray;
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        if (gapIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        int capacity = allocator.length(array);
        if (empty) {
            if (gapIndex != 0) {
                throw new IndexOutOfBoundsException();
            }
            if (capacity == 0) {
                enlargeArray(capacity = allocator.getCapacityPolicy().largerCapacity(capacity));
            }
            allocator.store(array, 0, in);
            empty = false;
            startIndex = 0;
            if (capacity == 1) {
                endIndex = 0;
            } else {
                endIndex = 1;
            }
            version++;
            return;
        }
        int arrayIndex = startIndex + gapIndex;
        if (startIndex < endIndex) {
            int endLength = endIndex - arrayIndex;
            if (endLength < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (gapIndex < endLength && startIndex > 0) {
                if (gapIndex > 0) {
                    allocator.copyWithin(array, startIndex, startIndex - 1, gapIndex);
                }
                startIndex--;
                allocator.store(array, arrayIndex - 1, in);
            } else {
                if (endLength > 0) {
                    allocator.copyWithin(array, arrayIndex, arrayIndex + 1, endLength);
                }
                endIndex++;
                if (endIndex == capacity) {
                    endIndex = 0;
                }
                allocator.store(array, arrayIndex, in);
            }
            version++;
            return;
        } else if (endIndex < startIndex) {
            if (arrayIndex < capacity) {
                if (gapIndex > 0) {
                    allocator.copyWithin(array, startIndex, startIndex - 1, gapIndex);
                }
                startIndex--;
                assert startIndex >= 0;
                allocator.store(array, arrayIndex - 1, in);
            } else {
                arrayIndex -= capacity;
                if (arrayIndex < endIndex) {
                    allocator.copyWithin(array, arrayIndex, arrayIndex + 1, endIndex - arrayIndex);
                }
                endIndex++;
                assert endIndex < capacity;
                allocator.store(array, arrayIndex, in);
            }
            version++;
            return;
        }
        enlargeArray(capacity = allocator.getCapacityPolicy().largerCapacity(capacity));
        if (gapIndex > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        if (gapIndex < endIndex) {
            allocator.copyWithin(array, gapIndex, gapIndex + 1, endIndex - gapIndex);
        }
        allocator.store(array, gapIndex, in);
        endIndex++;
        if (endIndex == capacity) {
            endIndex = 0;
        }
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        insertItemBefore(0, in);
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        insertItemBefore(getSize(), in);
    }

    @Override
    public void insertLast(@NotNull A thatArray, int thatStartIndex, int thatEndIndex) {
        final S tempSlot = allocator.newSlot();
        for (int index = thatStartIndex; index < thatEndIndex; index++) {
            allocator.load(thatArray, index, tempSlot);
            insertLastItem(tempSlot);
        }
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
            allocator.releaseReference(array, startIndex, capacity);
            if (endIndex > 0) {
                allocator.releaseReference(array, 0, endIndex);
            }
        }
        empty = true;
        version++;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "size = " + getSize() + ", start = " + startIndex + ", end = " + endIndex + ", modulo = " + allocator.length(array) + ", version = " + version;
    }
}
