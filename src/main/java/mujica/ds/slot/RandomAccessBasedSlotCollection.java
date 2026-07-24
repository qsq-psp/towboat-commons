package mujica.ds.slot;

import mujica.algebra.random.RandomContext;
import mujica.ds.i32.I32;
import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

@CodeHistory(date = "2026/7/9", name = "SlotList")
@CodeHistory(date = "2026/7/22")
@DirectSubclass({VersionSlotList.class, CopyOnWriteSlotList.class})
public abstract class RandomAccessBasedSlotCollection<S, A> implements SlotCollection<S, A> {

    @Override
    @NotNull
    public abstract RandomAccessBasedSlotCollection<S, A> duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @NotNull
    protected abstract SlotArrayAllocator<S, A> getAllocator();

    @Override
    public abstract int size();

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean contains(@NotNull S in) {
        return firstIndexOf(in) != -1;
    }

    @Override
    public int firstIndexOf(@NotNull S in) {
        final int listSize = size();
        final S tempSlot = getAllocator().newSlot();
        try {
            for (int index = 0; index < listSize; index++) {
                getItemAt(index, tempSlot);
                if (in.equals(tempSlot)) {
                    return index;
                }
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
            for (int index = size() - 1; index >= 0; index--) {
                getItemAt(index, tempSlot);
                if (in.equals(tempSlot)) {
                    return index;
                }
            }
            return -1;
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
    }

    @Override
    public abstract void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException;

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S slot) throws IndexOutOfBoundsException {
        final S tempSlot = getAllocator().newSlot();
        getItemAt(index, tempSlot);
        setItemAt(index, slot);
        getAllocator().move(tempSlot, slot);
    }

    @Override
    public void removeItemAt(int index, @Nullable S slot) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getFirstItem(@NotNull S out) throws NoSuchElementException {
        try {
            getItemAt(0, out);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void getLastItem(@NotNull S out) throws NoSuchElementException {
        try {
            getItemAt(size() - 1, out);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void getArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException {
        if (rc != null) {
            try {
                getItemAt(rc.nextInt(size()), out);
            } catch (ArithmeticException e) {
                throw new NoSuchElementException();
            }
        } else {
            getFirstItem(out);
        }
    }

    @Override
    public void removeFirstItem(@Nullable S out) throws NoSuchElementException {
        try {
            removeItemAt(0, out);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void removeLastItem(@Nullable S out) throws NoSuchElementException {
        try {
            removeItemAt(size() - 1, out);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void removeArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException {
        if (rc != null) {
            try {
                removeItemAt(rc.nextInt(size()), out);
            } catch (ArithmeticException e) {
                throw new NoSuchElementException();
            }
        } else {
            removeFirstItem(out);
        }
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        insertItemBefore(0, in);
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        insertItemBefore(size(), in);
    }

    @Override
    public void insertLast(@NotNull A thatArray) {
        insertLast(thatArray, 0, getAllocator().length(thatArray));
    }

    @Override
    public void insertLast(@NotNull A thatArray, int startIndex, int endIndex) {
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final S tempSlot = allocator.newSlot();
        for (int index = startIndex; index < endIndex; index++) {
            allocator.load(thatArray, index, tempSlot);
            insertLastItem(tempSlot);
        }
    }

    @Override
    public void insertLastSelf(int startIndex, int endIndex) {
        final S tempSlot = getAllocator().newSlot();
        for (int index = startIndex; index < endIndex; index++) {
            getItemAt(index, tempSlot);
            insertLastItem(tempSlot);
        }
    }

    @Override
    public void appendTo(@NotNull SlotCollection<S, A> that) {
        forEach(that::insertLastItem, getAllocator().newSlot());
    }

    @Override
    public void removeRange(int startIndex, int endIndex) throws IndexOutOfBoundsException {
        if (!(0 <= startIndex && startIndex <= endIndex && endIndex <= size())) {
            throw new IndexOutOfBoundsException();
        }
        while (startIndex < endIndex) {
            removeItemAt(endIndex - 1, null);
            endIndex--;
        }
    }

    @Override
    public void truncate(int newEndIndex) throws IndexOutOfBoundsException {
        final int listSize = size();
        if (!(0 <= newEndIndex && newEndIndex <= listSize)) {
            throw new IndexOutOfBoundsException();
        }
        removeRange(newEndIndex, listSize);
    }

    @Override
    public void clear() {
        truncate(0);
    }

    @Override
    public void reverse() {
        final S lowSlot = getAllocator().newSlot();
        final S highSlot = getAllocator().newSlot();
        int lowIndex = 0;
        int highIndex = size() - 1;
        while (lowIndex < highIndex) {
            getItemAt(lowIndex, lowSlot);
            getItemAt(highIndex, highSlot);
            setItemAt(highIndex, lowSlot);
            setItemAt(lowIndex, highSlot);
            lowIndex++;
            highIndex--;
        }
    }

    @Override
    public void rotate(int distance) {
        // todo
    }

    @Override
    public void sort(boolean descending) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long sort(@NotNull Sort<A> sort) {
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final S tempSlot = allocator.newSlot();
        final A tempArray = allToArray();
        try {
            int thisSize = allocator.length(tempArray);
            long operationCount = sort.sort(tempArray, 0, thisSize);
            for (int index = 0; index < thisSize; index++) {
                allocator.load(tempArray, index, tempSlot);
                setItemAt(index, tempSlot);
            }
            return operationCount;
        } finally {
            allocator.releaseSlot(tempSlot);
            allocator.releaseArray(tempArray);
        }
    }

    @Override
    public long sortPart(@NotNull Sort<A> sort, int newEndIndex) {
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final S tempSlot = allocator.newSlot();
        final A tempArray = allToArray();
        try {
            int thisSize = allocator.length(tempArray);
            if (!(0 <= newEndIndex && newEndIndex <= thisSize)) {
                throw new IndexOutOfBoundsException();
            }
            long operationCount = sort.sortPart(tempArray, 0, newEndIndex, thisSize);
            for (int index = 0; index < newEndIndex; index++) {
                allocator.load(tempArray, index, tempSlot);
                setItemAt(index, tempSlot);
            }
            truncate(newEndIndex);
            return operationCount;
        } finally {
            allocator.releaseSlot(tempSlot);
            allocator.releaseArray(tempArray);
        }
    }

    @Override
    public long sortUnique(@NotNull Sort<A> sort) {
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final S tempSlot = allocator.newSlot();
        final A tempArray = allToArray();
        try {
            int thisSize = allocator.length(tempArray);
            I32 endSlot = new I32(thisSize);
            long operationCount = sort.sortUnique(tempArray, 0, endSlot);
            thisSize = endSlot.getI32();
            for (int index = 0; index < thisSize; index++) {
                allocator.load(tempArray, index, tempSlot);
                setItemAt(index, tempSlot);
            }
            truncate(thisSize);
            return operationCount;
        } finally {
            allocator.releaseSlot(tempSlot);
            allocator.releaseArray(tempArray);
        }
    }

    @Override
    public void getRange(int srcIndex, @NotNull A dst, int dstIndex, int length) {
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final S tempSlot = allocator.newSlot();
        while (length > 0) {
            getItemAt(srcIndex++, tempSlot);
            allocator.store(dst, dstIndex++, tempSlot);
            length--;
        }
    }

    @NotNull
    @Override
    public A rangeToArray(int startIndex, int endIndex) {
        final int length = Math.subtractExact(endIndex, startIndex);
        final A newArray = getAllocator().newArray(length);
        getRange(startIndex, newArray, 0, length);
        return newArray;
    }

    @Override
    public void getAll(@NotNull A dst, int dstIndex) {
        getRange(0, dst, dstIndex, size());
    }

    @NotNull
    @Override
    public A allToArray() {
        final int length = size();
        final A newArray = getAllocator().newArray(length);
        getRange(0, newArray, 0, length);
        return newArray;
    }

    @Override
    public boolean internItem(@NotNull S slot) {
        final S tempSlot0 = getAllocator().newSlot();
        if (some(tempSlot1 -> tempSlot1.equals(slot), tempSlot0)) {
            getAllocator().move(tempSlot0, slot);
            return true;
        } else {
            insertLastItem(slot);
            return false;
        }
    }

    @Override
    public boolean tryGetFirstItem(@NotNull S out) {
        if (isEmpty()) {
            return false;
        }
        try {
            getItemAt(0, out);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean tryGetLastItem(@NotNull S out) {
        if (isEmpty()) {
            return false;
        }
        try {
            getItemAt(size() - 1, out);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean tryRemoveFirstItem(@Nullable S out) {
        if (isEmpty()) {
            return false;
        }
        try {
            removeItemAt(0, out);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean tryRemoveLastItem(@Nullable S out) {
        if (isEmpty()) {
            return false;
        }
        try {
            removeItemAt(size() - 1, out);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean tryGetArbitraryItem(@NotNull S out, @Nullable RandomContext rc) {
        if (isEmpty()) {
            return false;
        }
        if (rc != null) {
            try {
                getItemAt(rc.nextInt(size()), out);
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        } else {
            try {
                getItemAt(0, out);
                return true;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
    }

    @Override
    public boolean tryRemoveArbitraryItem(@NotNull S out, @Nullable RandomContext rc) {
        if (isEmpty()) {
            return false;
        }
        if (rc != null) {
            try {
                removeItemAt(rc.nextInt(size()), out);
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        } else {
            try {
                removeItemAt(0, out);
                return true;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
    }

    @Override
    public boolean tryInternItem(@NotNull S slot) {
        final S temp0 = getAllocator().newSlot();
        if (some(temp1 -> temp1.equals(slot), temp0)) {
            getAllocator().move(temp0, slot);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, tempSlot);
            consumer.accept(tempSlot);
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, tempSlot);
            if (predicate.test(tempSlot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, tempSlot);
            if (!predicate.test(tempSlot)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, tempSlot);
            consumer.accept(tempSlot);
            setItemAt(index, tempSlot);
        }
    }

    @Override
    public <T extends S> void removeIf(@NotNull Predicate<T> filter, @NotNull T tempSlot) {
        int listSize = size();
        int index = 0;
        while (index < listSize) {
            getItemAt(index, tempSlot);
            if (filter.test(tempSlot)) {
                index++;
            } else {
                removeItemAt(index, null);
                listSize--;
            }
        }
    }

    @NotNull
    @Override
    public GapIterator<S> iterator(int startGapIndex) {
        final int gapSize = size() + 1;
        final int nonNegativeGapIndex = startGapIndex < 0 ? gapSize + startGapIndex : startGapIndex;
        if (!(0 <= nonNegativeGapIndex && nonNegativeGapIndex < gapSize)) {
            throw new IndexOutOfBoundsException();
        }
        return new GapIterator<>() {

            int gapIndex = nonNegativeGapIndex;

            @Override
            public boolean previousGap() {
                if (gapIndex > 0) {
                    gapIndex--;
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean nextGap() {
                if (gapIndex < size()) {
                    gapIndex++;
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void getPrevious(@NotNull S out) throws NoSuchElementException {
                try {
                    getItemAt(gapIndex - 1, out);
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void getNext(@NotNull S out) throws NoSuchElementException {
                try {
                    getItemAt(gapIndex, out);
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void setPrevious(@NotNull S in) throws NoSuchElementException {
                try {
                    setItemAt(gapIndex - 1, in);
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void setNext(@NotNull S in) throws NoSuchElementException {
                try {
                    setItemAt(gapIndex, in);
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void removePrevious(@Nullable S out) throws NoSuchElementException {
                try {
                    removeItemAt(gapIndex - 1, out);
                    gapIndex--;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void removeNext(@Nullable S out) throws NoSuchElementException {
                try {
                    removeItemAt(gapIndex, out);
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void insertPrevious(@NotNull S in) {
                insertItemBefore(gapIndex, in);
                gapIndex++;
            }

            @Override
            public void insertNext(@NotNull S in) {
                insertItemBefore(gapIndex, in);
            }
        };
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "[size = " + size() + "]";
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        final S tempSlot = getAllocator().newSlot();
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            if (index != 0) {
                sb.append(", ");
            }
            getItemAt(index, tempSlot);
            sb.append(tempSlot);
        }
        return sb.append("}").toString();
    }
}
