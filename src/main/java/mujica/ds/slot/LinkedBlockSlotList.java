package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2025/3/14", project = "Ultramarine", name = "BlockLinkedList")
@CodeHistory(date = "2026/7/14")
public class LinkedBlockSlotList<S, A> extends VersionSlotList<S, A> {

    @CodeHistory(date = "2026/7/14")
    @Name(value = "double linked list node", language = "en")
    @Name(value = "双向链表节点", language = "zh")
    static class LinkedBlock<A> {

        LinkedBlock<A> previous;

        LinkedBlock<A> next;

        @NotNull
        final A array;

        int size;

        LinkedBlock(@NotNull A array) {
            super();
            this.array = array;
        }
    }

    @NotNull
    final SlotArrayAllocator<S, A> allocator;

    final int blockCapacity;

    LinkedBlock<A> head;

    LinkedBlock<A> tail;

    public LinkedBlockSlotList(@NotNull SlotArrayAllocator<S, A> allocator, int blockCapacity) {
        super();
        if (blockCapacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.allocator = allocator;
        this.blockCapacity = blockCapacity;
    }

    @NotNull
    @Override
    @Name(value = "duplicate double linked list", language = "en")
    @Name(value = "复制双向链表", language = "zh")
    public LinkedBlockSlotList<S, A> duplicate() {
        final LinkedBlockSlotList<S, A> that = new LinkedBlockSlotList<>(allocator, blockCapacity);
        LinkedBlock<A> thisBlock = this.head;
        while (thisBlock != null) {
            LinkedBlock<A> thatBlock = new LinkedBlock<>(allocator.cloneArray(thisBlock.array));
            thatBlock.size = thisBlock.size;
            if (that.head == null) {
                that.head = thatBlock;
            }
            if (that.tail != null) {
                that.tail.next = thatBlock;
            }
            thatBlock.previous = that.tail;
            that.tail = thatBlock;
            thisBlock = thisBlock.next;
        }
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final HashSet<LinkedBlock<A>> set = new HashSet<>();
        LinkedBlock<A> previous = null;
        LinkedBlock<A> current = head;
        while (current != null) {
            if (!set.add(current)) {
                consumer.accept(new RuntimeException("revisit link block " + current));
                return;
            }
            if (previous != current.previous) {
                consumer.accept(new RuntimeException("back link from " + current + " to " + previous));
            }
            int actualBlockCapacity = allocator.length(current.array);
            if (blockCapacity != actualBlockCapacity) {
                consumer.accept(new RuntimeException("block capacity expected " + blockCapacity + " actual " + actualBlockCapacity));
            }
            if (current.size <= 0) {
                consumer.accept(new RuntimeException("too small block size " + current.size));
            }
            if (current.size > blockCapacity) {
                consumer.accept(new RuntimeException("too large block size " + current.size));
            }
            previous = current;
            current = current.next;
        }
        if (previous != tail) {
            consumer.accept(new RuntimeException("tail link expected " + previous + " actual " + tail));
        }
    }

    @Override
    @NotNull
    protected SlotArrayAllocator<S, A> getAllocator() {
        return allocator;
    }

    @Override
    public int size() {
        int sum = 0;
        LinkedBlock<A> block = head;
        while (block != null) {
            sum += block.size;
            block = block.next;
        }
        return sum;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public int firstIndexOf(@NotNull S in) {
        final S tempSlot = getAllocator().newSlot();
        try {
            int base = 0;
            LinkedBlock<A> block = head;
            while (block != null) {
                for (int index = 0; index < block.size; index++) {
                    allocator.load(block.array, index, tempSlot);
                    if (in.equals(tempSlot)) {
                        return Math.addExact(base, index);
                    }
                }
                base = Math.addExact(base, block.size);
                block = block.next;
            }
            return -1;
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
    }

    @Override
    public int lastIndexOf(@NotNull S in) {
        final S tempSlot = getAllocator().newSlot();
        try {
            int base = -1; // this value is not used
            LinkedBlock<A> block = tail;
            BLOCKS:
            while (block != null) {
                for (int index = block.size - 1; index >= 0; index--) {
                    allocator.load(block.array, index, tempSlot);
                    if (in.equals(tempSlot)) {
                        base = index;
                        break BLOCKS;
                    }
                }
                base = Math.addExact(base, block.size);
                block = block.previous;
            }
            if (block == null) {
                return -1;
            }
            block = block.previous;
            while (block != null) {
                base = Math.addExact(base, block.size);
                block = block.previous;
            }
            return base;
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        LinkedBlock<A> block = head;
        while (block != null) {
            if (index < block.size) {
                allocator.load(block.array, index, out);
                return;
            }
            index -= block.size;
            block = block.next;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        LinkedBlock<A> block = head;
        while (block != null) {
            if (index < block.size) {
                allocator.store(block.array, index, in);
                return;
            }
            index -= block.size;
            block = block.next;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S slot) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        LinkedBlock<A> block = head;
        while (block != null) {
            if (index < block.size) {
                allocator.exchange(block.array, index, slot);
                return;
            }
            index -= block.size;
            block = block.next;
        }
        throw new IndexOutOfBoundsException();
    }

    void removeEmptyBlock(@NotNull LinkedBlock<A> block) { // empty blocks must be removed
        if (block.previous != null) {
            block.previous.next = block.next;
        } else {
            assert head == block;
            head = block.next;
        }
        if (block.next != null) {
            block.next.previous = block.previous;
        } else {
            assert tail == block;
            tail = block.previous;
        }
        block.previous = null;
        block.next = null;
        allocator.releaseArray(block.array);
    }

    @Override
    public void removeItemAt(int index, @Nullable S slot) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        LinkedBlock<A> block = head;
        while (block != null) {
            if (index < block.size) {
                if (slot != null) {
                    allocator.load(block.array, index, slot);
                }
                int endLength = block.size - index - 1;
                if (endLength > 0) {
                    allocator.copySelf(block.array, index + 1, index, endLength);
                }
                if (--block.size == 0) {
                    removeEmptyBlock(block);
                } else {
                    allocator.releaseReference(block.array, block.size);
                }
                version++;
                return;
            }
            index -= block.size;
            block = block.next;
        }
        throw new IndexOutOfBoundsException();
    }

    @NotNull
    @Override
    public GapIterator<S> iterator(int startGapIndex) {

        return new GapIterator<>() {

            LinkedBlock<A> previousBlock;

            int previousIndex;

            LinkedBlock<A> nextBlock;

            int nextIndex;

            int expectedVersion = version;

            @Override
            public boolean previousGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (previousBlock != null) {
                    nextBlock = previousBlock;
                    nextIndex = previousIndex;
                    if (previousIndex > 0) {
                        previousIndex--;
                    } else {
                        previousBlock = previousBlock.previous;
                        if (previousBlock != null) {
                            previousIndex = previousBlock.size - 1;
                            assert previousIndex >= 0;
                        } else {
                            previousIndex = -1;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean nextGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (nextBlock != null) {
                    previousBlock = nextBlock;
                    previousIndex = nextIndex;
                    if (nextIndex + 1 < nextBlock.size) {
                        nextIndex++;
                    } else {
                        nextBlock = nextBlock.next;
                        if (nextBlock != null) {
                            nextIndex = 0;
                            assert nextBlock.size > 0;
                        } else {
                            nextIndex = -1;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void getPrevious(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (previousBlock == null) {
                    throw new NoSuchElementException();
                }
                allocator.load(previousBlock.array, previousIndex, out);
            }

            @Override
            public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (nextBlock == null) {
                    throw new NoSuchElementException();
                }
                allocator.load(nextBlock.array, nextIndex, out);
            }

            @Override
            public void setPrevious(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (previousBlock == null) {
                    throw new NoSuchElementException();
                }
                allocator.store(previousBlock.array, previousIndex, in);
            }

            @Override
            public void setNext(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (nextBlock == null) {
                    throw new NoSuchElementException();
                }
                allocator.store(nextBlock.array, nextIndex, in);
            }

            @Override
            public void removePrevious(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (previousBlock == null) {
                    throw new NoSuchElementException();
                }
                if (out != null) {
                    allocator.load(previousBlock.array, previousIndex, out);
                }
                final int endLength = previousBlock.size - previousIndex - 1;
                if (endLength > 0) {
                    allocator.copySelf(previousBlock.array, previousIndex + 1, previousIndex, endLength);
                }
                if (previousBlock == nextBlock) {
                    nextIndex--;
                    assert nextIndex >= 0;
                }
                if (--previousBlock.size == 0) {
                    removeEmptyBlock(previousBlock);
                } else {
                    allocator.releaseReference(previousBlock.array, previousBlock.size);
                }
                if (nextIndex > 0) {
                    assert previousBlock == nextBlock;
                    previousIndex = nextIndex - 1;
                } else {
                    if (nextBlock != null) {
                        previousBlock = nextBlock.previous;
                    } else {
                        previousBlock = tail;
                    }
                    if (previousBlock != null) {
                        previousIndex = previousBlock.size - 1;
                        assert previousIndex >= 0;
                    } else {
                        previousIndex = -1;
                    }
                }
                expectedVersion = ++version;
            }

            @Override
            public void removeNext(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (nextBlock == null) {
                    throw new NoSuchElementException();
                }
                if (out != null) {
                    allocator.load(nextBlock.array, nextIndex, out);
                }
                final int endLength = nextBlock.size - nextIndex - 1;
                if (endLength > 0) {
                    allocator.copySelf(nextBlock.array, nextIndex + 1, nextIndex, endLength);
                }
                if (--nextBlock.size == 0) {
                    removeEmptyBlock(nextBlock);
                } else {
                    allocator.releaseReference(nextBlock.array, nextBlock.size);
                }
                if (endLength <= 0) {
                    assert endLength == 0;
                    nextBlock = nextBlock.next;
                    if (nextBlock != null) {
                        nextIndex = 0;
                        assert nextBlock.size > 0;
                    } else {
                        nextIndex = -1;
                    }
                }
                expectedVersion = ++version;
            }

            @Override
            public void insertPrevious(@NotNull S in) throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // todo
            }

            @Override
            public void insertNext(@NotNull S in) throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                // todo
            }
        };
    }
}
