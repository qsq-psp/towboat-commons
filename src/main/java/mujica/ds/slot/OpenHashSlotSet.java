package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

@CodeHistory(date = "2025/6/8", name = "LinkOpenHashSet")
@CodeHistory(date = "2026/7/27")
@Name(value = "开哈希集合", language = "zh")
public abstract class OpenHashSlotSet<S, A, N extends S> extends HashSlotSet<S, A> {

    @NotNull
    N[] hashTable;

    protected OpenHashSlotSet(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull N[] hashTable) {
        super(hashFunction);
        this.hashTable = hashTable;
    }

    @NotNull
    @Override
    public abstract SlotCollection<S, A> duplicateNew();

    @NotNull
    @Override
    public abstract SlotCollection<S, A> duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @Override
    public int firstIndexOf(@NotNull S in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(@NotNull S in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) {
        insertLastItem(in);
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        insertLastItem(in);
    }

    @Override
    public void insertLastSelf(int startIndex, int endIndex) {
        // pass
    }

    @Override
    public void removeRange(int startIndex, int endIndex) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void truncate(int newEndIndex) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        size = 0;
        version++;
    }

    @CodeHistory(date = "2026/7/28")
    @Name(value = "单向链表开哈希集合", language = "zh")
    public static class Link1<S, A, N extends S> extends OpenHashSlotSet<S, A, N> {

        @NotNull
        final NodeAllocator1<N> nodeAllocator;

        public Link1(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull NodeAllocator1<N> nodeAllocator) {
            super(hashFunction, nodeAllocator.newNodeArray(hashFunction.getCapacityPolicy().initialCapacity()));
            this.nodeAllocator = nodeAllocator;
        }

        Link1(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull NodeAllocator1<N> nodeAllocator, @NotNull N[] hashTable, int size) {
            super(hashFunction, hashTable);
            this.nodeAllocator = nodeAllocator;
            this.size = size;
        }

        @NotNull
        @Override
        public SlotCollection<S, A> duplicateNew() {
            return new Link1<>(hashFunction, nodeAllocator);
        }

        @NotNull
        @Override
        public HashSlotSet<S, A> duplicate() {
            final N[] oldHashTable = hashTable;
            final int tableLength = oldHashTable.length;
            final N[] newHashTable = nodeAllocator.newNodeArray(tableLength);
            System.arraycopy(oldHashTable, 0, newHashTable, 0, tableLength); // should be deep copy
            return new Link1<>(hashFunction, nodeAllocator, newHashTable, size);
        }

        @Override
        public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
            final int modulo = hashTable.length;
            int countedSize = 0;
            final HashSet<N> set = new HashSet<>();
            for (int index = 0; index < modulo; index++) {
                N node = hashTable[index];
                if (node == null) {
                    continue;
                }
                do {
                    countedSize++;
                    int cachedHash = nodeAllocator.getTag(node);
                    int currentHash = hashFunction.slotHash(node);
                    if (cachedHash != currentHash) {
                        consumer.accept(new RuntimeException("changed hash expect " + cachedHash + " actual " + currentHash));
                    }
                    int expectedIndex = (cachedHash & Integer.MAX_VALUE) % modulo;
                    if (expectedIndex != index) {
                        consumer.accept(new RuntimeException("misplaced element " + node + " expect index " + expectedIndex + " actual index " + index));
                    }
                    if (!set.add(node)) {
                        consumer.accept(new RuntimeException("duplicate node " + node));
                        break; // prevent infinite loop
                    }
                    node = nodeAllocator.getFirstLink(node);
                } while (node != null);
            }
            if (size != countedSize) {
                consumer.accept(new RuntimeException("size mismatch accumulated " + size + " counted " + countedSize));
            }
            if (size != set.size()) {
                consumer.accept(new RuntimeException("size mismatch accumulated " + size + " distinct " + set.size()));
            }
        }

        @Override
        public boolean contains(@NotNull S in) {
            final int hash = hashFunction.slotHash(in);
            N node = hashTable[(hash & Integer.MAX_VALUE) % hashTable.length];
            while (node != null) {
                if (hash == nodeAllocator.getTag(node) && hashFunction.slotEquals(in, node)) {
                    return true;
                }
                node = nodeAllocator.getFirstLink(node); // node = node.next;
            }
            return false;
        }

        @Override
        public boolean removeFirstEqualItem(@NotNull S slot) {
            final int hash = hashFunction.slotHash(slot);
            final int index = (hash & Integer.MAX_VALUE) % hashTable.length;
            N previous = null;
            N current = hashTable[index];
            while (current != null) {
                if (hash == nodeAllocator.getTag(current) && hashFunction.slotEquals(slot, current)) {
                    hashFunction.move(current, slot);
                    if (previous == null) {
                        hashTable[index] = nodeAllocator.getFirstLink(current); // head = current.next;
                    } else {
                        nodeAllocator.setFirstLink(previous, nodeAllocator.getFirstLink(current)); // previous.next = current.next;
                    }
                    nodeAllocator.releaseNode(current);
                    size--;
                    version++;
                    return true;
                }
                previous = current;
                current = nodeAllocator.getFirstLink(current); // current = current.next;
            }
            return false;
        }

        @Override
        public void insertLastItem(@NotNull S in) {
            final int hash = hashFunction.slotHash(in);
            final int index = (hash & Integer.MAX_VALUE) % hashTable.length;
            final N head = hashTable[index];
            N node = head;
            while (node != null) {
                if (hash == nodeAllocator.getTag(node) && hashFunction.slotEquals(in, node)) {
                    return;
                }
                node = nodeAllocator.getFirstLink(node);
            }
            node = nodeAllocator.newNode();
            nodeAllocator.setTag(node, hash);
            nodeAllocator.setFirstLink(node, head);
            hashFunction.move(in, node);
            hashTable[index] = node;
            size++;
            version++;
        }

        @Override
        public void clear() {
            if (size == 0) {
                return;
            }
            final int modulo = hashTable.length;
            for (int index = 0; index < modulo; index++) {
                N node = hashTable[index];
                if (node == null) {
                    continue;
                }
                do {
                    nodeAllocator.releaseNode(node);
                    node = nodeAllocator.getFirstLink(node);
                } while (node != null);
                hashTable[index] = null;
            }
            size = 0;
            version++;
        }

        @NotNull
        @Override
        public GapIterator<S> iterator(int startGapIndex) {
            return new GapIterator<>() {

                int previousTableIndex;

                N previousNode;

                int nextTableIndex;

                N nextNode;

                int expectedVersion = version;

                {
                    int index = startGapIndex;
                    if (index < 0) {
                        index += size + 1;
                    }
                    if (index < 0 || index > size) {
                        throw new IndexOutOfBoundsException();
                    }
                    nextTableIndex = -1;
                    while (true) {
                        while (nextNode == null && ++nextTableIndex < hashTable.length) {
                            nextNode = hashTable[nextTableIndex];
                        }
                        if (index-- <= 0) {
                            break;
                        }
                        if (nextNode == null) {
                            throw new IndexOutOfBoundsException();
                        }
                        previousTableIndex = nextTableIndex;
                        previousNode = nextNode;
                        nextNode = nodeAllocator.getFirstLink(nextNode);
                    }
                }

                @Override
                public boolean previousGap() throws ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (previousNode != null) {
                        nextTableIndex = previousTableIndex;
                        nextNode = previousNode;
                        N newPreviousNode = hashTable[previousTableIndex];
                        if (newPreviousNode == previousNode) { // previousNode is the link head
                            previousNode = null;
                            newPreviousNode = null;
                            while (--previousTableIndex >= 0) {
                                newPreviousNode = hashTable[previousTableIndex];
                                if (newPreviousNode != null) {
                                    break;
                                }
                            }
                            while (newPreviousNode != null) {
                                previousNode = newPreviousNode;
                                newPreviousNode = nodeAllocator.getFirstLink(newPreviousNode);
                            }
                        } else {
                            while (newPreviousNode != null) {
                                N next = nodeAllocator.getFirstLink(newPreviousNode);
                                if (next == previousNode) {
                                    previousNode = newPreviousNode;
                                    break;
                                }
                                newPreviousNode = next;
                            }
                            if (newPreviousNode == null) {
                                throw new RuntimeException();
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
                    if (nextNode != null) {
                        previousTableIndex = nextTableIndex;
                        previousNode = nextNode;
                        nextNode = nodeAllocator.getFirstLink(nextNode);
                        while (nextNode == null && ++nextTableIndex < hashTable.length) {
                            nextNode = hashTable[nextTableIndex];
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
                    if (previousNode == null) {
                        throw new NoSuchElementException();
                    }
                    hashFunction.move(previousNode, out);
                }

                @Override
                public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (nextNode == null) {
                        throw new NoSuchElementException();
                    }
                    hashFunction.move(nextNode, out);
                }

                @Override
                public void setPrevious(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                    removePrevious(null);
                    insertPrevious(in); // iterator is destroyed
                }

                @Override
                public void setNext(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                    removeNext(null);
                    insertNext(in); // iterator is destroyed
                }

                @Override
                public void removePrevious(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (previousNode == null) {
                        throw new NoSuchElementException();
                    }
                    N newPreviousNode = hashTable[previousTableIndex];
                    if (newPreviousNode == previousNode) {
                        hashTable[previousTableIndex] = nodeAllocator.getFirstLink(newPreviousNode);
                        nodeAllocator.releaseNode(newPreviousNode);
                        size--;
                        expectedVersion = ++version;
                        while (--previousTableIndex >= 0) {
                            newPreviousNode = hashTable[previousTableIndex];
                            if (newPreviousNode != null) {
                                break;
                            }
                        }
                        previousNode = newPreviousNode;
                        while (newPreviousNode != null) {
                            newPreviousNode = nodeAllocator.getFirstLink(newPreviousNode);
                            previousNode = newPreviousNode;
                        }
                    } else {
                        while (newPreviousNode != null) {
                            N next = nodeAllocator.getFirstLink(newPreviousNode);
                            if (next == previousNode) {
                                nodeAllocator.setFirstLink(newPreviousNode, nodeAllocator.getFirstLink(next));
                                nodeAllocator.releaseNode(next);
                                size--;
                                expectedVersion = ++version;
                                previousNode = newPreviousNode;
                                break;
                            }
                            newPreviousNode = next;
                        }
                        if (newPreviousNode == null) {
                            throw new RuntimeException();
                        }
                    }
                }

                @Override
                public void removeNext(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (nextNode == null) {
                        throw new NoSuchElementException();
                    }
                    final N newNextNode = nodeAllocator.getFirstLink(nextNode);
                    if (previousTableIndex == nextTableIndex) {
                        nodeAllocator.setFirstLink(previousNode, newNextNode); // previousNode.next = nextNode.next;
                    } else {
                        hashTable[nextTableIndex] = newNextNode;
                    }
                    nodeAllocator.releaseNode(nextNode);
                    size--;
                    expectedVersion = ++version;
                    nextNode = newNextNode;
                    while (nextNode == null && ++nextTableIndex < hashTable.length) {
                        nextNode = hashTable[nextTableIndex];
                    }
                }

                @Override
                public void insertPrevious(@NotNull S in) throws ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    insertLastItem(in);
                    expectedVersion = -1; // iterator is destroyed
                }

                @Override
                public void insertNext(@NotNull S in) throws ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    insertLastItem(in);
                    expectedVersion = -1; // iterator is destroyed
                }
            };
        }
    }

    @CodeHistory(date = "2026/7/28")
    @Name(value = "双向链表开哈希集合", language = "zh")
    public static class Link2<S, A, N extends S> extends OpenHashSlotSet<S, A, N> {

        @NotNull
        final NodeAllocator2<N> nodeAllocator;

        public Link2(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull NodeAllocator2<N> nodeAllocator) {
            super(hashFunction, nodeAllocator.newNodeArray(hashFunction.getCapacityPolicy().initialCapacity()));
            this.nodeAllocator = nodeAllocator;
            final int modulo = hashTable.length;
            for (int index = 0; index < modulo; index++) {
                hashTable[index] = nodeAllocator.newNode(); // dummy; first = head, second = tail
            }
        }

        Link2(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull NodeAllocator2<N> nodeAllocator, @NotNull N[] hashTable, int size) {
            super(hashFunction, hashTable);
            this.nodeAllocator = nodeAllocator;
            this.size = size;
        }

        @NotNull
        @Override
        public SlotCollection<S, A> duplicateNew() {
            return new Link2<>(hashFunction, nodeAllocator);
        }

        @NotNull
        @Override
        public HashSlotSet<S, A> duplicate() {
            final N[] oldHashTable = hashTable;
            final int tableLength = oldHashTable.length;
            final N[] newHashTable = nodeAllocator.newNodeArray(tableLength);
            System.arraycopy(oldHashTable, 0, newHashTable, 0, tableLength); // should be deep copy
            return new Link2<>(hashFunction, nodeAllocator, newHashTable, size);
        }

        @Override
        public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
            final int modulo = hashTable.length;
            int countedSize = 0;
            final IdentityHashMap<N, N> dummySet = new IdentityHashMap<>();
            final HashSet<N> loadedSet = new HashSet<>();
            for (int index = 0; index < modulo; index++) {
                N node = hashTable[index];
                if (node == null) {
                    consumer.accept(new RuntimeException("null dummy node at " + index));
                    continue;
                }
                if (dummySet.put(node, node) != null) {
                    consumer.accept(new RuntimeException("duplicate dummy node at " + index));
                }
                N head = nodeAllocator.getFirstLink(node);
                N tail = nodeAllocator.getSecondLink(node);
                if (head == null || tail == null) {
                    if (head != null) {
                        consumer.accept(new RuntimeException("only head not null"));
                    }
                    if (tail != null) {
                        consumer.accept(new RuntimeException("only tail not null"));
                    }
                    return;
                }
                node = head;
                if (nodeAllocator.getSecondLink(node) != null) {
                    consumer.accept(new RuntimeException("unexpected head"));
                }
                while (node != null) {
                    countedSize++;
                    int cachedHash = nodeAllocator.getTag(node);
                    int currentHash = hashFunction.slotHash(node);
                    if (cachedHash != currentHash) {
                        consumer.accept(new RuntimeException("changed hash expect " + cachedHash + " actual " + currentHash));
                    }
                    int expectedIndex = (cachedHash & Integer.MAX_VALUE) % modulo;
                    if (expectedIndex != index) {
                        consumer.accept(new RuntimeException("misplaced element " + node + " expect index " + expectedIndex + " actual index " + index));
                    }
                    if (!loadedSet.add(node)) {
                        consumer.accept(new RuntimeException("duplicate node " + node));
                        break; // prevent infinite loop
                    }
                    N next = nodeAllocator.getFirstLink(node);
                    if (next != null) {
                        if (nodeAllocator.getSecondLink(next) != node) {
                            consumer.accept(new RuntimeException("bad pair"));
                        }
                    } else {
                        if (node != tail) {
                            consumer.accept(new RuntimeException("unexpected tail"));
                        }
                    }
                    node = next;
                }
            }
            if (size != countedSize) {
                consumer.accept(new RuntimeException("size mismatch accumulated " + size + " counted " + countedSize));
            }
            if (size != loadedSet.size()) {
                consumer.accept(new RuntimeException("size mismatch accumulated " + size + " distinct " + loadedSet.size()));
            }
        }

        @Override
        public boolean contains(@NotNull S in) {
            final int hash = hashFunction.slotHash(in);
            N node = hashTable[(hash & Integer.MAX_VALUE) % hashTable.length];
            while (true) {
                node = nodeAllocator.getFirstLink(node); // node = node.next;
                if (node == null) {
                    return false;
                }
                if (hash == nodeAllocator.getTag(node) && hashFunction.slotEquals(in, node)) {
                    return true;
                }
            }
        }

        @Override
        public boolean removeFirstEqualItem(@NotNull S slot) {
            final int hash = hashFunction.slotHash(slot);
            final N dummy = hashTable[(hash & Integer.MAX_VALUE) % hashTable.length];
            N current = dummy;
            while (true) {
                current = nodeAllocator.getFirstLink(current);
                if (current == null) {
                    return false;
                }
                if (hash == nodeAllocator.getTag(current) && hashFunction.slotEquals(slot, current)) {
                    N next = nodeAllocator.getFirstLink(current);
                    N previous = nodeAllocator.getSecondLink(current);
                    nodeAllocator.setFirstLink(Objects.requireNonNullElse(previous, dummy), next);
                    nodeAllocator.setSecondLink(Objects.requireNonNullElse(next, dummy), previous);
                    nodeAllocator.releaseNode(current);
                    size--;
                    version++;
                    return true;
                }
            }
        }

        @Override
        public void insertLastItem(@NotNull S in) {
            final int hash = hashFunction.slotHash(in);
            final N dummy = hashTable[(hash & Integer.MAX_VALUE) % hashTable.length];
            final N oldTail = nodeAllocator.getSecondLink(dummy);
            N current = nodeAllocator.getFirstLink(dummy);
            if (current != null) {
                do {
                    if (hash == nodeAllocator.getTag(current) && hashFunction.slotEquals(in, current)) {
                        return;
                    }
                    current = nodeAllocator.getFirstLink(current);
                } while (current != null);
                current = nodeAllocator.newNode();
                nodeAllocator.setFirstLink(oldTail, current);
            } else {
                current = nodeAllocator.newNode();
                nodeAllocator.setFirstLink(dummy, current);
            }
            nodeAllocator.setTag(current, hash);
            nodeAllocator.setFirstLink(current, null);
            nodeAllocator.setSecondLink(current, oldTail);
            nodeAllocator.setSecondLink(dummy, current);
            hashFunction.move(in, current);
            size++;
            version++;
        }

        @Override
        public void clear() {
            if (size == 0) {
                return;
            }
            for (N dummy : hashTable) {
                N current = nodeAllocator.getFirstLink(dummy);
                while (current != null) {
                    N next = nodeAllocator.getFirstLink(current);
                    nodeAllocator.releaseNode(current);
                    current = next;
                }
                nodeAllocator.setFirstLink(dummy, null);
                nodeAllocator.setSecondLink(dummy, null);
            }
            size = 0;
            version++;
        }

        @NotNull
        @Override
        public GapIterator<S> iterator(int startGapIndex) {
            return new GapIterator<>() {

                int previousTableIndex;

                N previousNode;

                int nextTableIndex;

                N nextNode;

                int expectedVersion = version;

                {
                    int index = startGapIndex;
                    if (index < 0) {
                        previousTableIndex = hashTable.length;
                        while (true) {
                            while (previousNode == null && previousTableIndex-- > 0) {
                                previousNode = nodeAllocator.getSecondLink(hashTable[previousTableIndex]);
                            }
                            if (++index >= 0) {
                                break;
                            }
                            if (previousNode == null) {
                                throw new IndexOutOfBoundsException();
                            }
                            nextTableIndex = previousTableIndex;
                            nextNode = previousNode;
                            previousNode = nodeAllocator.getSecondLink(previousNode);
                        }
                    } else {
                        nextTableIndex = -1;
                        while (true) {
                            while (nextNode == null && ++nextTableIndex < hashTable.length) {
                                nextNode = nodeAllocator.getFirstLink(hashTable[nextTableIndex]);
                            }
                            if (index-- <= 0) {
                                break;
                            }
                            if (nextNode == null) {
                                throw new IndexOutOfBoundsException();
                            }
                            previousTableIndex = nextTableIndex;
                            previousNode = nextNode;
                            nextNode = nodeAllocator.getFirstLink(nextNode);
                        }
                    }
                }

                @Override
                public boolean previousGap() throws ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (previousNode != null) {
                        nextTableIndex = previousTableIndex;
                        nextNode = previousNode;
                        previousNode = nodeAllocator.getSecondLink(previousNode);
                        while (previousNode == null && previousTableIndex-- > 0) {
                            previousNode = nodeAllocator.getSecondLink(hashTable[previousTableIndex]);
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
                    if (nextNode != null) {
                        previousTableIndex = nextTableIndex;
                        previousNode = nextNode;
                        nextNode = nodeAllocator.getFirstLink(nextNode);
                        while (nextNode == null && ++nextTableIndex < hashTable.length) {
                            nextNode = nodeAllocator.getFirstLink(hashTable[nextTableIndex]);
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
                    if (previousNode == null) {
                        throw new NoSuchElementException();
                    }
                    hashFunction.move(previousNode, out);
                }

                @Override
                public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (nextNode == null) {
                        throw new NoSuchElementException();
                    }
                    hashFunction.move(nextNode, out);
                }

                @Override
                public void setPrevious(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                    removePrevious(null);
                    insertPrevious(in); // iterator is destroyed
                }

                @Override
                public void setNext(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                    removeNext(null);
                    insertNext(in); // iterator is destroyed
                }

                @Override
                public void removePrevious(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (previousNode == null) {
                        throw new NoSuchElementException();
                    }
                    if (out != null) {
                        hashFunction.move(previousNode, out);
                    }
                    final N dummy = hashTable[previousTableIndex];
                    final N next = nodeAllocator.getFirstLink(previousNode);
                    final N previous = nodeAllocator.getSecondLink(previousNode);
                    nodeAllocator.setFirstLink(Objects.requireNonNullElse(previous, dummy), next);
                    nodeAllocator.setSecondLink(Objects.requireNonNullElse(next, dummy), previous);
                    nodeAllocator.releaseNode(previousNode);
                    size--;
                    expectedVersion = ++version;
                    previousTableIndex = nextTableIndex;
                    if (nextNode != null) {
                        previousNode = nodeAllocator.getSecondLink(nextNode);
                    } else {
                        previousTableIndex = hashTable.length;
                        previousNode = null;
                    }
                    while (previousNode == null && previousTableIndex-- > 0) {
                        previousNode = nodeAllocator.getSecondLink(hashTable[previousTableIndex]);
                    }
                }

                @Override
                public void removeNext(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (nextNode == null) {
                        throw new NoSuchElementException();
                    }
                    if (out != null) {
                        hashFunction.move(nextNode, out);
                    }
                    final N dummy = hashTable[nextTableIndex];
                    final N next = nodeAllocator.getFirstLink(nextNode);
                    final N previous = nodeAllocator.getSecondLink(nextNode);
                    nodeAllocator.setFirstLink(Objects.requireNonNullElse(previous, dummy), next);
                    nodeAllocator.setSecondLink(Objects.requireNonNullElse(next, dummy), previous);
                    nodeAllocator.releaseNode(nextNode);
                    size--;
                    expectedVersion = ++version;
                    nextTableIndex = previousTableIndex;
                    if (previousNode != null) {
                        nextNode = nodeAllocator.getFirstLink(previousNode);
                    } else {
                        nextTableIndex = -1;
                        nextNode = null;
                    }
                    while (nextNode == null && ++nextTableIndex < hashTable.length) {
                        nextNode = nodeAllocator.getFirstLink(hashTable[nextTableIndex]);
                    }
                }

                @Override
                public void insertPrevious(@NotNull S in) throws ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    insertLastItem(in);
                    expectedVersion = -1; // iterator is destroyed
                }

                @Override
                public void insertNext(@NotNull S in) throws ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    insertLastItem(in);
                    expectedVersion = -1; // iterator is destroyed
                }
            };
        }
    }

    @CodeHistory(date = "2026/7/29")
    @ReferenceCode(groupId = "JDK", artifactId = "java.base", version = "12", fullyQualifiedName = "java.util.LinkedHashSet")
    @Name(value = "有序开哈希集合", language = "zh")
    public static class Link3<S, A, N extends S> extends OpenHashSlotSet<S, A, N> {

        @NotNull
        final NodeAllocator3<N> nodeAllocator;

        N head;

        N tail;

        public Link3(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull NodeAllocator3<N> nodeAllocator) {
            super(hashFunction, nodeAllocator.newNodeArray(hashFunction.getCapacityPolicy().initialCapacity()));
            this.nodeAllocator = nodeAllocator;
        }

        Link3(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull NodeAllocator3<N> nodeAllocator, @NotNull N[] hashTable) {
            super(hashFunction, hashTable);
            this.nodeAllocator = nodeAllocator;
        }

        @NotNull
        @Override
        public SlotCollection<S, A> duplicateNew() {
            return new Link3<>(hashFunction, nodeAllocator);
        }

        @NotNull
        @Override
        public HashSlotSet<S, A> duplicate() {
            final N[] oldHashTable = hashTable;
            final int tableLength = oldHashTable.length;
            final N[] newHashTable = nodeAllocator.newNodeArray(tableLength);
            System.arraycopy(oldHashTable, 0, newHashTable, 0, tableLength); // should be deep copy
            final Link3<S, A, N> that = new Link3<>(hashFunction, nodeAllocator, newHashTable);
            that.size = this.size;
            that.head = this.head;
            that.tail = this.tail;
            return that;
        }

        @Override
        public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
            int actualSize = 0;
            {
                int modulo = hashTable.length;
                HashSet<N> set = new HashSet<>();
                for (int index = 0; index < modulo; index++) {
                    N node = hashTable[index];
                    if (node == null) {
                        continue;
                    }
                    do {
                        actualSize++;
                        int cachedHash = nodeAllocator.getTag(node);
                        int currentHash = hashFunction.slotHash(node);
                        if (cachedHash != currentHash) {
                            consumer.accept(new RuntimeException("changed hash expect " + cachedHash + " actual " + cachedHash));
                        }
                        int expectedIndex = (cachedHash & Integer.MAX_VALUE) % modulo;
                        if (expectedIndex != index) {
                            consumer.accept(new RuntimeException("misplaced element " + node + " expect index " + expectedIndex + " actual index " + index));
                        }
                        if (!set.add(node)) {
                            consumer.accept(new RuntimeException("duplicate node " + node));
                            break; // prevent infinite loop
                        }
                        node = nodeAllocator.getThirdLink(node);
                    } while (node != null);
                    set.clear();
                }
                if (size != actualSize) {
                    consumer.accept(new RuntimeException("size mismatch expect " + size + " actual " + actualSize));
                }
            }
            if (head == null || tail == null) {
                if (head != null) {
                    consumer.accept(new RuntimeException("only head not null"));
                }
                if (tail != null) {
                    consumer.accept(new RuntimeException("only tail not null"));
                }
                return;
            }
            {
                actualSize = 0;
                N node = head;
                if (nodeAllocator.getSecondLink(node) != null) {
                    consumer.accept(new RuntimeException("unexpected head"));
                }
                while (node != null) {
                    actualSize++;
                    N next = nodeAllocator.getFirstLink(node);
                    if (next != null) {
                        if (nodeAllocator.getSecondLink(next) != node) {
                            consumer.accept(new RuntimeException("bad pair"));
                        }
                    } else {
                        if (node != tail) {
                            consumer.accept(new RuntimeException("unexpected tail"));
                        }
                    }
                    node = next;
                }
                if (size != actualSize) {
                    consumer.accept(new RuntimeException("size mismatch expect " + size + " actual " + actualSize));
                }
            }
        }

        private void removeFromHashTable(@NotNull N node) {
            final int index = (nodeAllocator.getTag(node) & Integer.MAX_VALUE) % hashTable.length;
            N current = hashTable[index];
            if (current == node) {
                hashTable[index] = nodeAllocator.getThirdLink(current);
            } else {
                while (true) {
                    N next = nodeAllocator.getThirdLink(current);
                    if (next == null) {
                        throw new RuntimeException();
                    }
                    if (next == node) {
                        nodeAllocator.setThirdLink(current, nodeAllocator.getThirdLink(next));
                        break;
                    }
                    current = next;
                }
            }
        }

        private void removeFromLinkedList(@NotNull N node) {
            {
                N previous = nodeAllocator.getSecondLink(node);
                if (previous != null) {
                    nodeAllocator.setFirstLink(previous, nodeAllocator.getFirstLink(node)); // node.previous.next = node.next;
                } else {
                    assert head == node;
                    head = nodeAllocator.getFirstLink(node); // head = node.next;
                }
            }
            {
                N next = nodeAllocator.getFirstLink(node);
                if (next != null) {
                    nodeAllocator.setSecondLink(next, nodeAllocator.getSecondLink(node)); // node.next.previous = node.previous;
                } else {
                    assert tail == node;
                    tail = nodeAllocator.getSecondLink(node); // tail = node.previous
                }
            }
            // node.previous = null;
            // node.next = null;
            nodeAllocator.releaseNode(node);
        }

        private int removeNode(@NotNull N node) {
            removeFromHashTable(node);
            removeFromLinkedList(node);
            size--;
            return ++version;
        }

        @Nullable
        private N insertIntoHashTable(@NotNull S in) {
            final int hash = hashFunction.slotHash(in);
            final int index = (hash & Integer.MAX_VALUE) % hashTable.length;
            final N head = hashTable[index];
            N node = head;
            while (node != null) {
                if (hash == nodeAllocator.getTag(node) && hashFunction.slotEquals(in, node)) {
                    return null;
                }
                node = nodeAllocator.getThirdLink(node);
            }
            node = nodeAllocator.newNode();
            nodeAllocator.setTag(node, hash);
            nodeAllocator.setThirdLink(node, head);
            hashFunction.move(in, node);
            hashTable[index] = node;
            return node;
        }

        @Override
        public boolean contains(@NotNull S in) {
            final int hash = hashFunction.slotHash(in);
            N node = hashTable[(hash & Integer.MAX_VALUE) % hashTable.length];
            while (node != null) {
                if (hash == nodeAllocator.getTag(node) && hashFunction.slotEquals(in, node)) {
                    return true;
                }
                node = nodeAllocator.getThirdLink(node); // node = node.next;
            }
            return false;
        }

        @Override
        public boolean removeFirstEqualItem(@NotNull S slot) {
            final int hash = hashFunction.slotHash(slot);
            final int index = (hash & Integer.MAX_VALUE) % hashTable.length;
            N previous = null;
            N current = hashTable[index];
            while (current != null) {
                if (hash == nodeAllocator.getTag(current) && hashFunction.slotEquals(slot, current)) {
                    hashFunction.move(current, slot);
                    if (previous == null) {
                        hashTable[index] = nodeAllocator.getThirdLink(current); // head = current.next;
                    } else {
                        nodeAllocator.setThirdLink(previous, nodeAllocator.getThirdLink(current)); // previous.next = current.next;
                    }
                    removeFromLinkedList(current);
                    size--;
                    version++;
                    return true;
                }
                previous = current;
                current = nodeAllocator.getThirdLink(current); // current = current.next;
            }
            return false;
        }

        @Override
        public void insertFirstItem(@NotNull S in) {
            final N node = insertIntoHashTable(in);
            if (node == null) {
                return;
            }
            if (head == null) {
                assert tail == null;
                tail = node;
            } else {
                nodeAllocator.setSecondLink(head, node);
            }
            nodeAllocator.setSecondLink(node, null);
            nodeAllocator.setFirstLink(node, head);
            head = node;
            size++;
            version++;
        }

        @Override
        public void insertLastItem(@NotNull S in) {
            final N node = insertIntoHashTable(in);
            if (node == null) {
                return;
            }
            if (tail == null) {
                assert head == null;
                head = node;
            } else {
                nodeAllocator.setFirstLink(tail, node);
            }
            nodeAllocator.setFirstLink(node, null);
            nodeAllocator.setSecondLink(node, tail);
            tail = node;
            size++;
            version++;
        }

        @Override
        public void clear() {
            if (size == 0) {
                return;
            }
            Arrays.fill(hashTable, null);
            {
                N current = head;
                while (current != null) {
                    N next = nodeAllocator.getFirstLink(current);
                    nodeAllocator.releaseNode(current);
                    current = next;
                }
            }
            head = null;
            tail = null;
            size = 0;
            version++;
        }

        @NotNull
        @Override
        public GapIterator<S> iterator(int startGapIndex) {
            return new GapIterator<>() {

                N previousNode;

                N nextNode;

                int expectedVersion = version;

                {
                    int index = startGapIndex;
                    if (index < 0) {
                        previousNode = tail;
                        nextNode = null;
                        while (++index < 0) {
                            if (previousNode == null) {
                                throw new IndexOutOfBoundsException();
                            }
                            nextNode = previousNode;
                            previousNode = nodeAllocator.getSecondLink(previousNode);
                        }
                    } else {
                        previousNode = null;
                        nextNode = head;
                        while (index-- > 0) {
                            if (nextNode == null) {
                                throw new IndexOutOfBoundsException();
                            }
                            previousNode = nextNode;
                            nextNode = nodeAllocator.getFirstLink(nextNode);
                        }
                    }
                }

                @Override
                public boolean previousGap() throws ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (previousNode != null) {
                        nextNode = previousNode;
                        previousNode = nodeAllocator.getSecondLink(previousNode);
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
                    if (nextNode != null) {
                        previousNode = nextNode;
                        nextNode = nodeAllocator.getFirstLink(nextNode);
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
                    if (previousNode == null) {
                        throw new NoSuchElementException();
                    }
                    hashFunction.move(previousNode, out);
                }

                @Override
                public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (nextNode == null) {
                        throw new NoSuchElementException();
                    }
                    hashFunction.move(nextNode, out);
                }

                @Override
                public void setPrevious(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                    removePrevious(null);
                    insertPrevious(in);
                }

                @Override
                public void setNext(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                    removeNext(null);
                    insertNext(in);
                }

                @Override
                public void removePrevious(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (previousNode == null) {
                        throw new NoSuchElementException();
                    }
                    if (out != null) {
                        hashFunction.move(previousNode, out);
                    }
                    expectedVersion = removeNode(previousNode);
                    if (nextNode == null) {
                        previousNode = tail;
                    } else {
                        previousNode = nodeAllocator.getSecondLink(nextNode);
                    }
                }

                @Override
                public void removeNext(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    if (nextNode == null) {
                        throw new NoSuchElementException();
                    }
                    if (out != null) {
                        hashFunction.move(nextNode, out);
                    }
                    expectedVersion = removeNode(nextNode);
                    if (previousNode == null) {
                        nextNode = head;
                    } else {
                        nextNode = nodeAllocator.getFirstLink(previousNode);
                    }
                }

                @NotNull
                private N insert(@NotNull S in, @NotNull N ifFail) throws ConcurrentModificationException {
                    if (version != expectedVersion) {
                        throw new ConcurrentModificationException();
                    }
                    final N newNode = insertIntoHashTable(in);
                    if (newNode == null) {
                        return ifFail;
                    }
                    nodeAllocator.setFirstLink(newNode, nextNode);
                    nodeAllocator.setSecondLink(newNode, previousNode);
                    if (previousNode == null) {
                        assert head == nextNode;
                        head = newNode;
                    } else {
                        nodeAllocator.setFirstLink(previousNode, newNode); // previousNode.next = newNode;
                    }
                    if (nextNode == null) {
                        assert tail == previousNode;
                        tail = newNode;
                    } else {
                        nodeAllocator.setSecondLink(nextNode, newNode); // nextNode.previous = newNode;
                    }
                    expectedVersion = ++version;
                    return newNode;
                }

                @Override
                public void insertPrevious(@NotNull S in) throws ConcurrentModificationException {
                    previousNode = insert(in, previousNode);
                }

                @Override
                public void insertNext(@NotNull S in) throws ConcurrentModificationException {
                    nextNode = insert(in, nextNode);
                }
            };
        }
    }
}
