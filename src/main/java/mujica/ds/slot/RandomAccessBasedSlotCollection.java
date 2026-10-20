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

    private static final long serialVersionUID = 0x73ce895dcf958639L;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    @NotNull
    public SlotCollection<S, A> clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public abstract SlotCollection<S, A> duplicateNew();

    @Override
    @NotNull
    public abstract SlotCollection<S, A> duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @NotNull
    protected abstract SlotArrayAllocator<S, A> getAllocator();

    @Override
    public long sumOfDepth() {
        return getSize();
    }

    @Override
    public int maxDepth() {
        return 1;
    }

    @Override
    public abstract int getSize();

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
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
    public int countItem(@NotNull S in) {
        int sum = 0;
        final int listSize = getSize();
        final S scanSlot = getAllocator().newSlot();
        try {
            for (int index = 0; index < listSize; index++) {
                getItemAt(index, scanSlot);
                if (in.equals(scanSlot)) {
                    sum++;
                }
            }
            return sum;
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
    }

    @Override
    public int firstIndexOf(@NotNull S in) {
        final int listSize = getSize();
        final S scanSlot = getAllocator().newSlot();
        try {
            for (int index = 0; index < listSize; index++) {
                getItemAt(index, scanSlot);
                if (in.equals(scanSlot)) {
                    return index;
                }
            }
            return -1;
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
    }

    @Override
    public int lastIndexOf(@NotNull S in) {
        final S scanSlot = getAllocator().newSlot();
        try {
            for (int index = getSize() - 1; index >= 0; index--) {
                getItemAt(index, scanSlot);
                if (in.equals(scanSlot)) {
                    return index;
                }
            }
            return -1;
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
    }

    @Override
    public abstract void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException;

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S inOut) throws IndexOutOfBoundsException {
        final S tempSlot = getAllocator().newSlot();
        getItemAt(index, tempSlot);
        setItemAt(index, inOut);
        getAllocator().move(tempSlot, inOut);
    }

    @Override
    public void removeItemAt(int index, @Nullable S out) throws IndexOutOfBoundsException {
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
            getItemAt(getSize() - 1, out);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void getArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException {
        if (rc != null) {
            try {
                getItemAt(rc.nextInt(getSize()), out);
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
            removeItemAt(getSize() - 1, out);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void removeArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException {
        if (rc != null) {
            try {
                removeItemAt(rc.nextInt(getSize()), out);
            } catch (ArithmeticException e) {
                throw new NoSuchElementException();
            }
        } else {
            removeFirstItem(out);
        }
    }

    @Override
    public boolean removeFirstEqualItem(@NotNull S inOut) {
        final int index = firstIndexOf(inOut);
        if (index == -1) {
            return false;
        }
        removeItemAt(index, inOut);
        return true;
    }

    @Override
    public boolean removeLastEqualItem(@NotNull S inOut) {
        final int index = lastIndexOf(inOut);
        if (index == -1) {
            return false;
        }
        removeItemAt(index, inOut);
        return true;
    }

    @Override
    public boolean removeOneEqualItem(@NotNull S inOut) {
        return removeFirstEqualItem(inOut);
    }

    @Override
    public int removeAllEqualItems(@NotNull S in) {
        int count = 0;
        final S scanSlot = getAllocator().newSlot();
        try {
            int thisSize = getSize();
            int index = 0;
            while (index < thisSize) {
                if (in.equals(scanSlot)) {
                    removeItemAt(index, null);
                    thisSize = getSize();
                    count++;
                } else {
                    index++;
                }
            }
            return count;
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
    }

    @Override
    public boolean internItem(@NotNull S slot) {
        final int index = firstIndexOf(slot);
        if (index == -1) {
            insertLastItem(slot);
            return false;
        }
        getItemAt(index, slot);
        return true;
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
    public void insertLast(@NotNull A thatArray) {
        insertLast(thatArray, 0, getAllocator().length(thatArray));
    }

    @Override
    public void insertLast(@NotNull A thatArray, int thatStartIndex, int thatEndIndex) {
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final S tempSlot = allocator.newSlot();
        for (int index = thatStartIndex; index < thatEndIndex; index++) {
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
        if (!(0 <= startIndex && startIndex <= endIndex && endIndex <= getSize())) {
            throw new IndexOutOfBoundsException();
        }
        while (startIndex < endIndex) {
            removeItemAt(endIndex - 1, null);
            endIndex--;
        }
    }

    @Override
    public void setSize(int newSize) throws IndexOutOfBoundsException {
        final int oldSize = getSize();
        if (!(0 <= newSize && newSize <= oldSize)) {
            throw new IndexOutOfBoundsException();
        }
        removeRange(newSize, oldSize);
    }

    @Override
    public void clear() {
        setSize(0);
    }

    @Override
    public void reverse() {
        final S lowSlot = getAllocator().newSlot();
        final S highSlot = getAllocator().newSlot();
        int lowIndex = 0;
        int highIndex = getSize() - 1;
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
        final int modulo = getSize();
        if (modulo <= 1) {
            return;
        }
        distance %= modulo;
        if (distance == 0) {
            return;
        }
        if (distance < 0) {
            distance += modulo;
        }
        if (distance == 1) {
            S getSlot = getAllocator().newSlot();
            S setSlot = getAllocator().newSlot();
            try {
                getItemAt(modulo - 1, getSlot);
                for (int index = 0; index < modulo; index++) {
                    S tempSlot = getSlot;
                    getSlot = setSlot;
                    setSlot = tempSlot;
                    getItemAt(index, getSlot);
                    setItemAt(index, setSlot);
                }
            } finally {
                getAllocator().releaseSlot(getSlot);
                getAllocator().releaseSlot(setSlot);
            }
        } else if (distance == modulo - 1) {
            S getSlot = getAllocator().newSlot();
            S setSlot = getAllocator().newSlot();
            try {
                getItemAt(0, getSlot);
                for (int index = modulo - 1; index >= 0; index--) {
                    S tempSlot = getSlot;
                    getSlot = setSlot;
                    setSlot = tempSlot;
                    getItemAt(index, getSlot);
                    setItemAt(index, setSlot);
                }
            } finally {
                getAllocator().releaseSlot(getSlot);
                getAllocator().releaseSlot(setSlot);
            }
        } else {
            A tempArray = allToArray();
            try {
                clear();
                insertLast(tempArray, modulo - distance, modulo);
                insertLast(tempArray, 0, modulo - distance);
            } finally {
                getAllocator().releaseArray(tempArray);
            }
        }
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
            setSize(newEndIndex);
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
            setSize(thisSize);
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
        try {
            while (length > 0) {
                getItemAt(srcIndex++, tempSlot);
                allocator.store(dst, dstIndex++, tempSlot);
                length--;
            }
        } finally {
            allocator.releaseSlot(tempSlot);
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
        getRange(0, dst, dstIndex, getSize());
    }

    @NotNull
    @Override
    public A allToArray() {
        final int length = getSize();
        final A newArray = getAllocator().newArray(length);
        getRange(0, newArray, 0, length);
        return newArray;
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
            getItemAt(getSize() - 1, out);
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
            removeItemAt(getSize() - 1, out);
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
                getItemAt(rc.nextInt(getSize()), out);
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
                removeItemAt(rc.nextInt(getSize()), out);
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
        final int index = firstIndexOf(slot);
        if (index == -1) {
            return false;
        }
        getItemAt(index, slot);
        return true;
    }

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        final int listSize = getSize();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, consumedSlot);
            consumer.accept(consumedSlot);
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T consumedSlot) {
        final int listSize = getSize();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, consumedSlot);
            if (predicate.test(consumedSlot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T consumedSlot) {
        final int listSize = getSize();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, consumedSlot);
            if (!predicate.test(consumedSlot)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        final int listSize = getSize();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, consumedSlot);
            consumer.accept(consumedSlot);
            setItemAt(index, consumedSlot);
        }
    }

    @Override
    public <T extends S> void removeIf(@NotNull Predicate<T> filter, @NotNull T consumedSlot) {
        int listSize = getSize();
        int index = 0;
        while (index < listSize) {
            getItemAt(index, consumedSlot);
            if (filter.test(consumedSlot)) {
                removeItemAt(index, null);
                listSize--;
            } else {
                index++;
            }
        }
    }

    @NotNull
    @Override
    public GapIterator<S> iterator(int startGapIndex) {
        final int gapSize = getSize() + 1;
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
                if (gapIndex < getSize()) {
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
        return "size = " + getSize();
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder();
        final int listSize = getSize();
        final S tempSlot = getAllocator().newSlot();
        try {
            for (int index = 0; index < listSize; index++) {
                if (index != 0) {
                    sb.append(", ");
                }
                getItemAt(index, tempSlot);
                sb.append(tempSlot);
            }
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
        return sb.toString();
    }

    @Override
    @NotNull
    public String toString() {
        try {
            String summary = summaryToString();
            try {
                return getClass().getSimpleName() + " [" + summary + "] {" + detailToString() + "}";
            } catch (RuntimeException e) {
                return getClass().getSimpleName() + " [" + summary + "] " + e;
            }
        } catch (RuntimeException e) {
            return getClass().getSimpleName() + " " + e;
        }
    }
}
