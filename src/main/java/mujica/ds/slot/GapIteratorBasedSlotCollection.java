package mujica.ds.slot;

import mujica.algebra.random.RandomContext;
import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

@CodeHistory(date = "2026/7/21")
public abstract class GapIteratorBasedSlotCollection<S, A> implements SlotCollection<S, A> {

    @Override
    @NotNull
    public abstract GapIteratorBasedSlotCollection<S, A> duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @NotNull
    protected abstract SlotArrayAllocator<S, A> getAllocator();

    @Override
    public int size() {
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
    public void exchangeItemAt(int index, @NotNull S ioSlot) throws IndexOutOfBoundsException {
        final S tempSlot = getAllocator().newSlot();
        try {
            GapIterator<S> iterator = iterator(index);
            iterator.getNext(tempSlot);
            iterator.setNext(ioSlot);
            getAllocator().move(tempSlot, ioSlot);
        } catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        } finally {
            getAllocator().releaseSlot(tempSlot);
        }
    }

    @Override
    public void removeItemAt(int index, @Nullable S slot) throws IndexOutOfBoundsException {
        try {
            iterator(index).removeNext(slot);
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

        } else {
            removeFirstItem(out);
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
    public void truncate(int newEndIndex) throws IndexOutOfBoundsException {
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
                iterator.setPrevious(tempSlot);
                iterator.previousGap();
            }
        } finally {
            allocator.releaseSlot(tempSlot);
            allocator.releaseArray(tempArray);
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
        return 0L;
    }

    @Override
    public long sortPart(@NotNull Sort<A> sort, int newEndIndex) {
        return 0L;
    }

    @Override
    public long sortUnique(@NotNull Sort<A> sort) {
        return 0L;
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
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(tempSlot);
            consumer.accept(tempSlot);
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(tempSlot);
            if (predicate.test(tempSlot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(tempSlot);
            if (!predicate.test(tempSlot)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(tempSlot);
            consumer.accept(tempSlot);
            iterator.setPrevious(tempSlot);
        }
    }

    @Override
    public <T extends S> void removeIf(@NotNull Predicate<T> filter, @NotNull T tempSlot) {
        final GapIterator<S> iterator = iterator(0);
        while (iterator.nextGap()) {
            iterator.getPrevious(tempSlot);
            if (!filter.test(tempSlot)) {
                iterator.removePrevious(null);
            }
        }
    }

    @NotNull
    @Override
    public abstract GapIterator<S> iterator(int startGapIndex);
}
