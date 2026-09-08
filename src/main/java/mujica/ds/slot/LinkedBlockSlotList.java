package mujica.ds.slot;

import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2025/3/14", project = "Ultramarine", name = "BlockLinkedList")
@CodeHistory(date = "2026/7/14")
@Name(value = "分块链表", language = "zh")
public class LinkedBlockSlotList<S, A> extends GapIteratorBasedSlotCollection<S, A> {

    private static final long serialVersionUID = 0x88858DEC79595C74L;

    @CodeHistory(date = "2026/7/14")
    static class LinkedBlock<A> implements Serializable {

        private static final long serialVersionUID = 0x03125776187421C3L;

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

    @Name(value = "modCount", language = "en")
    transient int version;

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
    public SlotCollection<S, A> duplicateNew() {
        return new LinkedBlockSlotList<>(allocator, blockCapacity);
    }

    @NotNull
    @Override
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
    public int getSize() {
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
    public void exchangeItemAt(int index, @NotNull S inOut) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        LinkedBlock<A> block = head;
        while (block != null) {
            if (index < block.size) {
                allocator.exchange(block.array, index, inOut);
                return;
            }
            index -= block.size;
            block = block.next;
        }
        throw new IndexOutOfBoundsException();
    }

    private void removeBlock(@NotNull LinkedBlock<A> block) {
        // empty blocks must be removed
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
    public void removeItemAt(int index, @Nullable S out) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        LinkedBlock<A> block = head;
        while (block != null) {
            if (index < block.size) {
                if (out != null) {
                    allocator.load(block.array, index, out);
                }
                int endLength = block.size - index - 1;
                if (endLength > 0) {
                    allocator.copyWithin(block.array, index + 1, index, endLength);
                }
                if (--block.size == 0) {
                    removeBlock(block);
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

    private void insertItem(@NotNull LinkedBlock<A> block, int gapIndex, @NotNull S in) {
        if (block.size == blockCapacity) {
            if (gapIndex <= blockCapacity - gapIndex) {
                LinkedBlock<A> next = new LinkedBlock<>(allocator.newArray(blockCapacity));
                next.next = block.next;
                if (block.next == null) {
                    assert tail == block;
                    tail = next;
                } else {
                    block.next.previous = next;
                }
                next.previous = block;
                block.next = next;
                allocator.copy(block.array, gapIndex, next.array, 0, blockCapacity - gapIndex);
                next.size = blockCapacity - gapIndex;
                allocator.releaseReference(block.array, gapIndex, blockCapacity);
                block.size = gapIndex;
            } else {
                LinkedBlock<A> previous = new LinkedBlock<>(allocator.newArray(blockCapacity));
                previous.previous = block.previous;
                if (block.previous == null) {
                    assert head == block;
                    head = previous;
                } else {
                    block.previous.next = previous;
                }
                previous.next = block;
                block.previous = previous;
                allocator.copy(block.array, 0, previous.array, 0, gapIndex);
                previous.size = gapIndex;
                if (gapIndex < blockCapacity) {
                    allocator.copyWithin(block.array, gapIndex, 0, blockCapacity - gapIndex);
                }
                allocator.releaseReference(block.array, blockCapacity - gapIndex);
                block.size = blockCapacity - gapIndex;
            }
        }
        assert block.size < blockCapacity;
        if (gapIndex < block.size) {
            allocator.copyWithin(block.array, gapIndex, gapIndex + 1, block.size - gapIndex);
        }
        allocator.store(block.array, gapIndex, in);
        block.size++;
        version++;
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        if (gapIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        LinkedBlock<A> block = head;
        if (block == null) {
            if (gapIndex != 0) {
                throw new IndexOutOfBoundsException();
            }
            assert tail == null;
            block = new LinkedBlock<>(allocator.newArray(blockCapacity));
            head = block;
            tail = block;
            insertItem(block, 0, in);
            return;
        }
        do {
            if (gapIndex > block.size) {
                gapIndex -= block.size;
                block = block.next;
                continue;
            } else if (gapIndex < block.size) {
                insertItem(block, gapIndex, in);
            } else {
                LinkedBlock<A> next = block.next;
                if (next == null) {
                    assert tail == block;
                    insertItem(block, gapIndex, in);
                } else if (block.size <= next.size) {
                    insertItem(block, gapIndex, in);
                } else {
                    insertItem(next, 0, in);
                }
            }
            break;
        } while (block != null);
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void getFirstItem(@NotNull S out) throws NoSuchElementException {
        if (head == null) {
            throw new NoSuchElementException();
        }
        allocator.load(head.array, 0, out);
    }

    @Override
    public void getLastItem(@NotNull S out) throws NoSuchElementException {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        allocator.load(tail.array, tail.size - 1, out);
    }

    @Override
    public void getArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException {
        if (rc != null) {
            int count = 0;
            LinkedBlock<A> block = head;
            while (block != null) {
                count += block.size;
                int index = rc.nextInt(count);
                if (index < block.size) {
                    allocator.load(block.array, index, out);
                }
                block = block.next;
            }
            if (count == 0) {
                throw new NoSuchElementException();
            }
        } else {
            getFirstItem(out);
        }
    }

    @Override
    public void removeFirstItem(@Nullable S out) throws NoSuchElementException {
        if (head == null) {
            throw new NoSuchElementException();
        }
        if (out != null) {
            allocator.load(head.array, 0, out);
        }
        final int endLength = head.size - 1;
        if (endLength > 0) {
            allocator.copyWithin(head.array, 1, 0, endLength);
            head.size = endLength;
        } else {
            head.size = 0;
            removeBlock(head);
        }
        version++;
    }

    @Override
    public void removeLastItem(@Nullable S out) throws NoSuchElementException {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        if (out != null) {
            allocator.load(tail.array, tail.size - 1, out);
        }
        if (--tail.size > 0) {
            allocator.releaseReference(tail.array, tail.size);
        } else {
            removeBlock(tail);
        }
        version++;
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        if (head == null) {
            assert tail == null;
            LinkedBlock<A> block = new LinkedBlock<>(allocator.newArray(blockCapacity));
            head = block;
            tail = block;
        }
        insertItem(head, 0, in);
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        if (tail == null) {
            assert head == null;
            LinkedBlock<A> block = new LinkedBlock<>(allocator.newArray(blockCapacity));
            head = block;
            tail = block;
        }
        insertItem(tail, tail.size, in);
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

            {
                int index = startGapIndex;
                if (index < 0) {
                    previousBlock = tail;
                    if (previousBlock != null) {
                        previousIndex = previousBlock.size - 1;
                    }
                    nextBlock = null;
                } else {
                    previousBlock = null;
                    nextBlock = head;
                    nextIndex = 0;
                    while (nextBlock != null) {
                        if (index <= nextBlock.size) {
                            break;
                        }
                    }
                    // todo
                }
            }

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
                    allocator.copyWithin(previousBlock.array, previousIndex + 1, previousIndex, endLength);
                }
                if (previousBlock == nextBlock) {
                    nextIndex--;
                    assert nextIndex >= 0;
                }
                if (--previousBlock.size == 0) {
                    removeBlock(previousBlock);
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
                    allocator.copyWithin(nextBlock.array, nextIndex + 1, nextIndex, endLength);
                }
                if (--nextBlock.size == 0) {
                    removeBlock(nextBlock);
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
