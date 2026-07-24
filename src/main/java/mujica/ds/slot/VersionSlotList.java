package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

@CodeHistory(date = "2026/7/18")
@DirectSubclass({CopyOnResizeSlotList.class, LinkedBlockSlotList.class, LinkedSlotList.class})
public abstract class VersionSlotList<S, A> extends RandomAccessBasedSlotCollection<S, A> {

    @Name(value = "modCount", language = "en")
    transient int version;

    @Override
    public <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        final int expectedVersion = version;
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, tempSlot);
            consumer.accept(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        final int expectedVersion = version;
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, tempSlot);
            boolean predicateResult = predicate.test(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            if (predicateResult) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T tempSlot) {
        final int expectedVersion = version;
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, tempSlot);
            boolean predicateResult = predicate.test(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            if (!predicateResult) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T tempSlot) {
        final int expectedVersion = version;
        final int listSize = size();
        for (int index = 0; index < listSize; index++) {
            getItemAt(index, tempSlot);
            consumer.accept(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            setItemAt(index, tempSlot);
        }
    }

    @Override
    public <T extends S> void removeIf(@NotNull Predicate<T> filter, @NotNull T tempSlot) {
        int expectedVersion = version;
        int listSize = size();
        int index = 0;
        while (index < listSize) {
            getItemAt(index, tempSlot);
            boolean filterResult = filter.test(tempSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
            if (filterResult) {
                index++;
            } else {
                removeItemAt(index, null);
                expectedVersion = version;
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

            int expectedVersion = version;

            @Override
            public boolean previousGap() throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                if (gapIndex > 0) {
                    gapIndex--;
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
                if (gapIndex < size()) {
                    gapIndex++;
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
                try {
                    getItemAt(gapIndex - 1, out);
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                try {
                    getItemAt(gapIndex, out);
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void setPrevious(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                try {
                    setItemAt(gapIndex - 1, in);
                    expectedVersion = version;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void setNext(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                try {
                    setItemAt(gapIndex, in);
                    expectedVersion = version;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void removePrevious(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                try {
                    removeItemAt(gapIndex - 1, out);
                    gapIndex--;
                    expectedVersion = version;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void removeNext(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                try {
                    removeItemAt(gapIndex, out);
                    expectedVersion = version;
                } catch (IndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void insertPrevious(@NotNull S in) throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                insertItemBefore(gapIndex, in);
                gapIndex++;
                expectedVersion = version;
            }

            @Override
            public void insertNext(@NotNull S in) throws ConcurrentModificationException {
                if (version != expectedVersion) {
                    throw new ConcurrentModificationException();
                }
                insertItemBefore(gapIndex, in);
                expectedVersion = version;
            }
        };
    }
}
