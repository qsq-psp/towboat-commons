package mujica.ds.slot;

import mujica.algebra.random.RandomContext;
import mujica.ds.i32.I32;
import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

@CodeHistory(date = "2026/7/21")
public abstract class GapIteratorBasedSlotCollection<S, A> implements SlotCollection<S, A> {

    private static final long serialVersionUID = 0x9F5A468FB48930B3L;

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
        return -1L; // unknown
    }

    @Override
    public int maxDepth() {
        return -1; // unknown
    }

    @Override
    public int getSize() {
        int count = 0;
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            count++;
        }
        return count;
    }

    @Override
    public boolean isEmpty() {
        return !iterator(0).nextGap();
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
        final GapIterator<S> iterator = iterator(0);
        final S tempSlot = getAllocator().newSlot();
        try {
            while (iterator.nextGap()) {
                iterator.getPrevious(tempSlot);
                if (in.equals(tempSlot)) {
                    sum++;
                }
            }
            return sum;
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
    }

    @Override
    public int firstIndexOf(@NotNull S in) {
        final GapIterator<S> iterator = iterator(0);
        final S tempSlot = getAllocator().newSlot();
        try {
            int index = 0;
            while (iterator.nextGap()) {
                iterator.getPrevious(tempSlot);
                if (in.equals(tempSlot)) {
                    return index;
                }
                index++;
            }
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
        return -1;
    }

    @Override
    public int lastIndexOf(@NotNull S in) {
        final GapIterator<S> iterator = iterator(0);
        final S tempSlot = getAllocator().newSlot();
        try {
            int lastIndex = -1;
            int index = 0;
            while (iterator.nextGap()) {
                iterator.getPrevious(tempSlot);
                if (in.equals(tempSlot)) {
                    lastIndex = index;
                }
                index++;
            }
            return lastIndex;
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        try {
            iterator(index).getNext(out);
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        try {
            iterator(index).setNext(in);
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S inOut) throws IndexOutOfBoundsException {
        final S tempSlot = getAllocator().newSlot();
        try {
            GapIterator<S> iterator = iterator(index);
            iterator.getNext(tempSlot);
            iterator.setNext(inOut);
            getAllocator().move(tempSlot, inOut);
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
    }

    @Override
    public void removeItemAt(int index, @Nullable S out) throws IndexOutOfBoundsException {
        try {
            iterator(index).removeNext(out);
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        try {
            iterator(gapIndex).insertPrevious(in);
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void getFirstItem(@NotNull S out) throws NoSuchElementException {
        iterator(0).getNext(out);
    }

    @Override
    public void getLastItem(@NotNull S out) throws NoSuchElementException {
        iterator(-1).getPrevious(out);
    }

    @Override
    public void getArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException {
        if (rc != null) {
            int count = 0;
            GapIterator<S> iterator = iterator(0);
            while (iterator.nextGap()) {
                if (rc.nextInt(++count) == 0) {
                    iterator.getPrevious(out);
                }
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
        iterator(0).removeNext(out);
    }

    @Override
    public void removeLastItem(@Nullable S out) throws NoSuchElementException {
        iterator(-1).removePrevious(out);
    }

    @Override
    public void removeArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException {
        if (rc != null) {
            int count = 0;
            GapIterator<S> iterator = iterator(0);
            while (iterator.nextGap()) {
                if (rc.nextInt(++count) == 0) {
                    iterator.getPrevious(out);
                }
            }
            if (count == 0) {
                throw new NoSuchElementException();
            }
            if (!removeFirstEqualItem(out)) {
                throw new RuntimeException();
            }
        } else {
            removeFirstItem(out);
        }
    }

    @Override
    public boolean removeFirstEqualItem(@NotNull S inOut) {
        final GapIterator<S> iterator = iterator(0);
        final S scanSlot = getAllocator().newSlot();
        try {
            while (iterator.nextGap()) {
                iterator.getPrevious(scanSlot);
                if (inOut.equals(scanSlot)) {
                    iterator.removePrevious(inOut);
                    return true;
                }
            }
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
        return false;
    }

    @Override
    public boolean removeLastEqualItem(@NotNull S inOut) {
        final GapIterator<S> iterator = iterator(-1);
        final S scanSlot = getAllocator().newSlot();
        try {
            while (iterator.previousGap()) {
                iterator.getNext(scanSlot);
                if (inOut.equals(scanSlot)) {
                    iterator.removeNext(inOut);
                    return true;
                }
            }
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
        return false;
    }

    @Override
    public boolean removeOneEqualItem(@NotNull S inOut) {
        return removeFirstEqualItem(inOut);
    }

    @Override
    public int removeAllEqualItems(@NotNull S in) {
        int count = 0;
        final GapIterator<S> iterator = iterator(0);
        final S scanSlot = getAllocator().newSlot();
        try {
            while (iterator.nextGap()) {
                iterator.getPrevious(scanSlot);
                if (in.equals(scanSlot)) {
                    iterator.removePrevious(in);
                    count++;
                }
            }
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
        return count;
    }

    @Override
    public boolean internItem(@NotNull S slot) {
        final GapIterator<S> iterator = iterator(0);
        final S scanSlot = getAllocator().newSlot();
        try {
            while (iterator.nextGap()) {
                iterator.getPrevious(scanSlot);
                if (slot.equals(scanSlot)) {
                    getAllocator().move(scanSlot, slot);
                    return true;
                }
            }
            insertLastItem(slot);
            return false;
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        iterator(0).insertNext(in); // insertPrevious(in) is also okay because the iterator is no longer used
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        iterator(-1).insertPrevious(in); // insertNext(in) is also okay because the iterator is no longer used
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
        final A tempArray = rangeToArray(startIndex, endIndex);
        try {
            endIndex -= startIndex;
            insertLast(tempArray, 0, endIndex);
        } finally {
            getAllocator().releaseArray(tempArray);
        }
    }

    @Override
    public void appendTo(@NotNull SlotCollection<S, A> that) {
        forEach(that::insertLastItem, getAllocator().newSlot());
    }

    @Override
    public void removeRange(int startIndex, int endIndex) throws IndexOutOfBoundsException {
        if (!(0 <= startIndex && startIndex <= endIndex)) {
            throw new IndexOutOfBoundsException();
        }
        final GapIterator<S> iterator = iterator(endIndex);
        while (startIndex < endIndex) {
            iterator.removePrevious(null);
            startIndex++;
        }
    }

    @Override
    public void setSize(int newEndIndex) throws IndexOutOfBoundsException {
        final GapIterator<S> iterator = iterator(newEndIndex);
        while (iterator.nextGap()) {
            iterator.removePrevious(null);
        }
    }

    @Override
    public void clear() {
        final GapIterator<S> iterator = iterator(-1);
        while (iterator.previousGap()) {
            iterator.removeNext(null);
        }
    }

    @Override
    public void reverse() {
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final S tempSlot = allocator.newSlot();
        final A tempArray = allToArray();
        try {
            int length = allocator.length(tempArray);
            final GapIterator<S> iterator = iterator(-1);
            for (int index = 0; index < length; index++) {
                allocator.load(tempArray, index, tempSlot);
                iterator.setPrevious(tempSlot); // blind
                iterator.previousGap();
            }
        } finally {
            allocator.releaseSlot(tempSlot);
            allocator.releaseArray(tempArray);
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
        distance -= modulo; // ensure negative
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final S tempSlot = allocator.newSlot();
        final A tempArray = rangeToArray(0, modulo);
        try {
            final GapIterator<S> iterator = iterator(0);
            for (int index = 0; index < modulo; index++) {
                allocator.load(tempArray, (index - distance) % modulo, tempSlot);
                iterator.setNext(tempSlot); // blind
                iterator.nextGap();
            }
        } finally {
            getAllocator().releaseArray(tempArray);
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
            clear();
            insertLast(tempArray);
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
            clear();
            insertLast(tempArray, 0, newEndIndex);
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
            clear();
            insertLast(tempArray, 0, thisSize);
            return operationCount;
        } finally {
            allocator.releaseSlot(tempSlot);
            allocator.releaseArray(tempArray);
        }
    }

    @Override
    public void getRange(int srcIndex, @NotNull A dst, int dstIndex, int length) {
        if (srcIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        final SlotArrayAllocator<S, A> allocator = getAllocator();
        final GapIterator<S> iterator = iterator(srcIndex);
        final S tempSlot = allocator.newSlot();
        try {
            for (int index = 0; index < length; index++) {
                if (iterator.nextGap()) {
                    iterator.getPrevious(tempSlot);
                    allocator.store(dst, dstIndex + index, tempSlot);
                } else {
                    throw new IndexOutOfBoundsException();
                }
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
        return rangeToArray(0, getSize());
    }

    @Override
    public boolean tryGetFirstItem(@NotNull S out) {
        final GapIterator<S> iterator = iterator(0);
        if (iterator.nextGap()) {
            iterator.getPrevious(out);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryGetLastItem(@NotNull S out) {
        final GapIterator<S> iterator = iterator(-1);
        if (iterator.previousGap()) {
            iterator.getNext(out);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryRemoveFirstItem(@Nullable S out) {
        final GapIterator<S> iterator = iterator(0);
        if (iterator.nextGap()) {
            iterator.removePrevious(out);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryRemoveLastItem(@Nullable S out) {
        final GapIterator<S> iterator = iterator(-1);
        if (iterator.previousGap()) {
            iterator.removeNext(out);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryGetArbitraryItem(@NotNull S out, @Nullable RandomContext rc) {
        if (rc != null) {
            int count = 0;
            GapIterator<S> iterator = iterator(0);
            while (iterator.nextGap()) {
                if (rc.nextInt(++count) == 0) {
                    iterator.getPrevious(out);
                }
            }
            return count != 0;
        } else {
            return tryGetFirstItem(out);
        }
    }

    @Override
    public boolean tryRemoveArbitraryItem(@NotNull S out, @Nullable RandomContext rc) {
        if (rc != null) {
            int count = 0;
            GapIterator<S> iterator = iterator(0);
            while (iterator.nextGap()) {
                if (rc.nextInt(++count) == 0) {
                    iterator.getPrevious(out);
                }
            }
            return count != 0 && removeFirstEqualItem(out);
        } else {
            return tryRemoveFirstItem(out);
        }
    }

    @Override
    public boolean tryInternItem(@NotNull S slot) {
        final GapIterator<S> iterator = iterator(0);
        final S scanSlot = getAllocator().newSlot();
        try {
            while (iterator.nextGap()) {
                iterator.getPrevious(scanSlot);
                if (slot.equals(scanSlot)) {
                    getAllocator().move(scanSlot, slot);
                    return true;
                }
            }
            return false;
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
    }

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(consumedSlot);
            consumer.accept(consumedSlot);
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T consumedSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(consumedSlot);
            if (predicate.test(consumedSlot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T consumedSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(consumedSlot);
            if (!predicate.test(consumedSlot)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T consumedSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(consumedSlot);
            consumer.accept(consumedSlot);
            iterator.setPrevious(consumedSlot);
        }
    }

    @Override
    public <T extends S> void removeIf(@NotNull Predicate<T> filter, @NotNull T consumedSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(consumedSlot);
            if (filter.test(consumedSlot)) {
                iterator.removePrevious(null);
            }
        }
    }

    @NotNull
    @Override
    public abstract GapIterator<S> iterator(int startGapIndex);

    @NotNull
    @Override
    public String summaryToString() {
        return "size = " + getSize();
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder();
        final GapIterator<S> iterator = iterator(0);
        final S stringifySlot = getAllocator().newSlot();
        try {
            boolean plural = false;
            while (iterator.nextGap()) {
                if (plural) {
                    sb.append(", ");
                }
                iterator.getPrevious(stringifySlot);
                sb.append(stringifySlot);
                plural = true;
            }
        } finally {
            getAllocator().releaseSlot(stringifySlot);
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
