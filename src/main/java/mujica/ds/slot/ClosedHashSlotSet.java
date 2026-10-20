package mujica.ds.slot;

import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2025/7/4", name = "ClosedHashSet")
@CodeHistory(date = "2026/7/23")
public abstract class ClosedHashSlotSet<S, A> extends HashSlotSet<S, A> {

    private static final long serialVersionUID = 0x7ad47fcf06e07cfdL;

    static final int SPECIAL_EMPTY = -1;

    static final int SPECIAL_REMOVED = -2;

    transient int modulo;

    @NotNull
    A hashTable;

    @NotNull
    int[] hashCache;

    protected ClosedHashSlotSet(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction) {
        super(hashFunction);
        this.modulo = hashFunction.getCapacityPolicy().initialCapacity();
        this.hashTable = hashFunction.newArray(modulo);
        this.hashCache = new int[modulo];
        Arrays.fill(hashCache, SPECIAL_EMPTY);
    }

    ClosedHashSlotSet(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull A hashTable, @NotNull int[] hashCache, int size) {
        super(hashFunction);
        this.modulo = hashCache.length;
        this.hashTable = hashTable;
        this.hashCache = hashCache;
        this.size = size;
    }

    @NotNull
    @Override
    public abstract SlotCollection<S, A> duplicateNew();

    @NotNull
    @Override
    public abstract SlotCollection<S, A> duplicate();

    private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        modulo = hashCache.length;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (modulo != hashCache.length) {
            consumer.accept(new RuntimeException("modulo mismatch with hashCache"));
        }
        if (modulo != hashFunction.length(hashTable)) {
            consumer.accept(new RuntimeException("modulo mismatch with hashTable"));
        }
    }

    protected abstract void resetTableIndex(int positiveHashCode);

    protected abstract int getTableIndex();

    protected abstract boolean nextTableIndex();

    @NotNull
    protected String transientInfo() {
        return "modulo = " + modulo;
    }

    private void rehash() {
        final int oldModulo = modulo;
        final A oldHashTable = hashTable;
        final int[] oldHashCache = hashCache;
        final S tempSlot = hashFunction.newSlot();
        try {
            modulo = hashFunction.getCapacityPolicy().largerCapacity(oldModulo);
            hashTable = hashFunction.newArray(modulo);
            hashCache = new int[modulo];
            Arrays.fill(hashCache, SPECIAL_EMPTY);
            LOOP:
            for (int oldIndex = 0; oldIndex < oldModulo; oldIndex++) {
                int cachedHash = oldHashCache[oldIndex];
                if (cachedHash < 0) {
                    continue;
                }
                resetTableIndex(cachedHash);
                do {
                    int newIndex = getTableIndex();
                    if (hashCache[newIndex] == SPECIAL_EMPTY) {
                        hashFunction.load(oldHashTable, oldIndex, tempSlot);
                        assert (hashFunction.slotHash(tempSlot) & Integer.MAX_VALUE) == cachedHash;
                        hashFunction.store(hashTable, newIndex, tempSlot);
                        hashCache[newIndex] = cachedHash;
                        continue LOOP;
                    }
                } while (nextTableIndex());
                throw new RuntimeException("ClosedHashSlotSet.rehash(): oldModulo = " + oldModulo + ", oldIndex = " + oldIndex + ", " + transientInfo());
            }
            hashFunction.releaseArray(oldHashTable);
            version++;
        } finally {
            hashFunction.releaseSlot(tempSlot);
        }
    }

    @Override
    public boolean contains(@NotNull S in) {
        final int positiveHashCode = hashFunction.slotHash(in) & Integer.MAX_VALUE; // normalizedHash
        S equalSlot = null;
        try {
            resetTableIndex(positiveHashCode);
            do {
                int tableIndex = getTableIndex();
                int cachedHash = hashCache[tableIndex];
                if (cachedHash == positiveHashCode) {
                    if (equalSlot == null) {
                        equalSlot = hashFunction.newSlot();
                    }
                    hashFunction.load(hashTable, tableIndex, equalSlot);
                    if (hashFunction.slotEquals(in, equalSlot)) {
                        return true;
                    }
                } else if (cachedHash == SPECIAL_EMPTY) {
                    break;
                }
            } while (nextTableIndex());
        } finally {
            if (equalSlot != null) {
                hashFunction.releaseSlot(equalSlot);
            }
        }
        return false;
    }

    @Override
    public int countItem(@NotNull S in) {
        return contains(in) ? 1 : 0;
    }

    @Override
    public int firstIndexOf(@NotNull S in) {
        final int positiveHashCode = hashFunction.slotHash(in) & Integer.MAX_VALUE; // normalizedHash
        S equalSlot = null;
        try {
            resetTableIndex(positiveHashCode);
            do {
                int tableIndex = getTableIndex();
                int cachedHash = hashCache[tableIndex];
                if (cachedHash == positiveHashCode) {
                    if (equalSlot == null) {
                        equalSlot = hashFunction.newSlot();
                    }
                    hashFunction.load(hashTable, tableIndex, equalSlot);
                    if (hashFunction.slotEquals(in, equalSlot)) {
                        int thisIndex = 0;
                        while (tableIndex-- > 0) {
                            if (hashCache[tableIndex] >= 0) {
                                thisIndex++;
                            }
                        }
                        return thisIndex;
                    }
                } else if (cachedHash == SPECIAL_EMPTY) {
                    break;
                }
            } while (nextTableIndex());
        } finally {
            if (equalSlot != null) {
                hashFunction.releaseSlot(equalSlot);
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(@NotNull S in) {
        return firstIndexOf(in);
    }

    private boolean removeEqualItem(@NotNull S slot, boolean moveOut) {
        final int positiveHashCode = hashFunction.slotHash(slot) & Integer.MAX_VALUE;
        S equalSlot = null;
        try {
            resetTableIndex(positiveHashCode);
            do {
                int tableIndex = getTableIndex();
                int cachedHash = hashCache[tableIndex];
                if (cachedHash == positiveHashCode) {
                    if (equalSlot == null) {
                        equalSlot = hashFunction.newSlot();
                    }
                    hashFunction.load(hashTable, tableIndex, equalSlot);
                    if (hashFunction.slotEquals(slot, equalSlot)) {
                        if (moveOut) {
                            hashFunction.move(equalSlot, slot);
                        }
                        hashFunction.releaseReference(hashTable, tableIndex);
                        hashCache[tableIndex] = SPECIAL_REMOVED;
                        size--;
                        version++;
                        return true;
                    }
                } else if (cachedHash == SPECIAL_EMPTY) {
                    break;
                }
            } while (nextTableIndex());
        } finally {
            if (equalSlot != null) {
                hashFunction.releaseSlot(equalSlot);
            }
        }
        return false;
    }

    @Override
    public boolean removeFirstEqualItem(@NotNull S inOut) {
        return removeEqualItem(inOut, true);
    }

    @Override
    public boolean removeLastEqualItem(@NotNull S inOut) {
        return removeEqualItem(inOut, true);
    }

    @Override
    public boolean removeOneEqualItem(@NotNull S inOut) {
        return removeEqualItem(inOut, true);
    }

    @Override
    public int removeAllEqualItems(@NotNull S in) {
        return removeEqualItem(in, false) ? 1 : 0;
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
    public void insertLastItem(@NotNull S in) {
        final int positiveHashCode = hashFunction.slotHash(in) & Integer.MAX_VALUE;
        S equalSlot = null;
        try {
            while (true) {
                int firstSpecialIndex = -1;
                resetTableIndex(positiveHashCode);
                do {
                    int tableIndex = getTableIndex();
                    int cachedHash = hashCache[tableIndex];
                    if (cachedHash == positiveHashCode) {
                        if (equalSlot == null) {
                            equalSlot = hashFunction.newSlot();
                        }
                        hashFunction.load(hashTable, tableIndex, equalSlot);
                        if (hashFunction.slotEquals(in, equalSlot)) {
                            return;
                        }
                    } else if (cachedHash < 0) {
                        if (firstSpecialIndex == -1) {
                            firstSpecialIndex = tableIndex;
                        }
                        if (cachedHash == SPECIAL_EMPTY) {
                            break;
                        }
                    }
                } while (nextTableIndex());
                if (firstSpecialIndex != -1) {
                    hashFunction.store(hashTable, firstSpecialIndex, in);
                    hashCache[firstSpecialIndex] = positiveHashCode;
                    size++;
                    version++;
                    return;
                }
                rehash();
            }
        } finally {
            if (equalSlot != null) {
                hashFunction.releaseSlot(equalSlot);
            }
        }
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
    public void setSize(int newEndIndex) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        hashFunction.releaseReference(hashTable); // released index can be released again
        Arrays.fill(hashCache, SPECIAL_EMPTY);
        size = 0;
        version++;
    }

    @Override
    public void reverse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rotate(int distance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long sort(@NotNull Sort<A> sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long sortPart(@NotNull Sort<A> sort, int newEndIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long sortUnique(@NotNull Sort<A> sort) {
        throw new UnsupportedOperationException();
    }

    private int convertGapIndex(int gapIndex) {
        if (gapIndex < 0) {
            if (++gapIndex >= 0) {
                return modulo;
            }
            for (int tableIndex = modulo - 1; tableIndex >= 0; tableIndex--) {
                if (hashCache[tableIndex] >= 0 && ++gapIndex >= 0) {
                    return tableIndex;
                }
            }
        } else {
            if (gapIndex-- <= 0) {
                return 0;
            }
            for (int tableIndex = 0; tableIndex < modulo; tableIndex++) {
                if (hashCache[tableIndex] >= 0 && gapIndex-- <= 0) {
                    return tableIndex + 1;
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @NotNull
    @Override
    public GapIterator<S> iterator(int startGapIndex) {
        final int nonNegativeHashTableGapIndex = convertGapIndex(startGapIndex);
        return new GapIterator<>() {

            int gapIndex = nonNegativeHashTableGapIndex;

            int expectedVersion = version;

            @Override
            public boolean previousGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                while (gapIndex > 0) {
                    if (hashCache[--gapIndex] >= 0) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean nextGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                while (gapIndex < modulo) {
                    if (hashCache[gapIndex++] >= 0) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void getPrevious(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                while (gapIndex > 0) {
                    if (hashCache[gapIndex - 1] < 0) { // empty or removed
                        gapIndex--;
                    } else {
                        hashFunction.load(hashTable, gapIndex - 1, out);
                        return;
                    }
                }
                throw new NoSuchElementException();
            }

            @Override
            public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                while (gapIndex < modulo) {
                    if (hashCache[gapIndex] < 0) { // empty or removed
                        gapIndex++;
                    } else {
                        hashFunction.load(hashTable, gapIndex, out);
                        return;
                    }
                }
                throw new NoSuchElementException();
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
                while (gapIndex > 0) {
                    if (hashCache[gapIndex - 1] < 0) { // empty or removed
                        gapIndex--;
                    } else {
                        if (out != null) {
                            hashFunction.load(hashTable, gapIndex - 1, out);
                        }
                        hashFunction.releaseReference(hashTable, gapIndex - 1);
                        hashCache[gapIndex - 1] = SPECIAL_REMOVED;
                        size--;
                        expectedVersion = ++version;
                        return;
                    }
                }
                throw new NoSuchElementException();
            }

            @Override
            public void removeNext(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                while (gapIndex < modulo) {
                    if (hashCache[gapIndex] < 0) { // empty or removed
                        gapIndex++;
                    } else {
                        if (out != null) {
                            hashFunction.load(hashTable, gapIndex, out);
                        }
                        hashFunction.releaseReference(hashTable, gapIndex);
                        hashCache[gapIndex] = SPECIAL_REMOVED;
                        size--;
                        expectedVersion = ++version;
                        return;
                    }
                }
                throw new NoSuchElementException();
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

    @CodeHistory(date = "2025/6/5", name = "LinearClosedHashSet")
    @CodeHistory(date = "2025/6/26", name = "LinearClosedHashIntSet")
    @CodeHistory(date = "2026/7/25")
    public static class Linear<S, A> extends ClosedHashSlotSet<S, A> {

        private static final long serialVersionUID = 0x4c6495beb2b3a570L;

        private transient int limit, tableIndex;

        public Linear(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction) {
            super(hashFunction);
        }

        Linear(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull A hashTable, @NotNull int[] hashCache, int size) {
            super(hashFunction, hashTable, hashCache, size);
        }

        @NotNull
        @Override
        public SlotCollection<S, A> duplicateNew() {
            return new Linear<>(hashFunction);
        }

        @NotNull
        @Override
        public HashSlotSet<S, A> duplicate() {
            return new Linear<>(hashFunction, hashFunction.cloneArray(hashTable), hashCache.clone(), size);
        }

        @Override
        protected void resetTableIndex(int positiveHashCode) {
            limit = positiveHashCode % modulo;
            tableIndex = limit;
        }

        @Override
        protected int getTableIndex() {
            return tableIndex;
        }

        @Override
        protected boolean nextTableIndex() {
            tableIndex++;
            if (tableIndex == modulo) {
                tableIndex = 0;
            }
            return tableIndex != limit;
        }

        @NotNull
        @Override
        protected String transientInfo() {
            return "modulo = " + modulo + ", limit = " + limit + ", tableIndex = " + tableIndex;
        }
    }

    @CodeHistory(date = "2025/6/29", name = "QuadraticClosedHashSet")
    @CodeHistory(date = "2025/7/3", name = "QuadraticClosedHashIntSet")
    @CodeHistory(date = "2026/7/25")
    public static class Quadratic<S, A> extends ClosedHashSlotSet<S, A> {

        private static final long serialVersionUID = 0x047c13ec8b1668f5L;

        private transient int base, countingIndex;

        public Quadratic(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction) {
            super(hashFunction);
            hashFunction.getCapacityPolicy().requireQuadratic();
        }

        Quadratic(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull A hashTable, @NotNull int[] hashCache, int size) {
            super(hashFunction, hashTable, hashCache, size);
        }

        @NotNull
        @Override
        public SlotCollection<S, A> duplicateNew() {
            return new Quadratic<>(hashFunction);
        }

        @NotNull
        @Override
        public HashSlotSet<S, A> duplicate() {
            return new Quadratic<>(hashFunction, hashFunction.cloneArray(hashTable), hashCache.clone(), size);
        }

        @Override
        protected void resetTableIndex(int positiveHashCode) {
            base = positiveHashCode % modulo;
            countingIndex = 0;
        }

        @Override
        protected int getTableIndex() {
            int tableIndex = countingIndex;
            if ((tableIndex & 1) == 0) { // even
                tableIndex >>= 1;
                tableIndex = (base - tableIndex * tableIndex) % modulo;
                if (tableIndex < 0) {
                    tableIndex += modulo;
                }
            } else {
                tableIndex = (tableIndex + 1) >> 1;
                tableIndex = (base + tableIndex * tableIndex) % modulo;
            }
            return tableIndex;
        }

        @Override
        protected boolean nextTableIndex() {
            countingIndex++;
            return countingIndex < modulo;
        }

        @NotNull
        @Override
        protected String transientInfo() {
            return "modulo = " + modulo + ", base = " + base + ", countingIndex = " + countingIndex;
        }
    }

    @CodeHistory(date = "2025/7/4", name = "CongruentClosedHashSet")
    @CodeHistory(date = "2026/7/25")
    public static class Congruent<S, A> extends ClosedHashSlotSet<S, A> {

        private static final long serialVersionUID = 0x158032ad8c029262L;

        private transient int addend, tableIndex, countingIndex;

        public Congruent(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction) {
            super(hashFunction);
            hashFunction.getCapacityPolicy().requireCongruent();
        }

        Congruent(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction, @NotNull A hashTable, @NotNull int[] hashCache, int size) {
            super(hashFunction, hashTable, hashCache, size);
        }

        @NotNull
        @Override
        public SlotCollection<S, A> duplicateNew() {
            return new Congruent<>(hashFunction);
        }

        @NotNull
        @Override
        public HashSlotSet<S, A> duplicate() {
            return new Congruent<>(hashFunction, hashFunction.cloneArray(hashTable), hashCache.clone(), size);
        }

        @Override
        protected void resetTableIndex(int positiveHashCode) {
            addend = positiveHashCode / modulo;
            tableIndex = positiveHashCode - addend * modulo;
            if (modulo > 1) {
                addend = addend % (modulo - 1) + 1;
            } else {
                addend = 1;
            }
            countingIndex = 0;
        }

        @Override
        public int getTableIndex() {
            return tableIndex;
        }

        @Override
        protected boolean nextTableIndex() {
            tableIndex += addend;
            if (tableIndex >= modulo) {
                tableIndex -= modulo;
            }
            countingIndex++;
            return countingIndex < modulo;
        }

        @NotNull
        @Override
        protected String transientInfo() {
            return "modulo = " + modulo + ", addend = " + addend + ", tableIndex = " + tableIndex + ", countingIndex = " + countingIndex;
        }
    }
}
