package mujica.ds.of_boolean.list;

import mujica.ds.of_int.list.IntList;
import mujica.reflect.function.BooleanConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created on 2026/3/12.
 */
@CodeHistory(date = "2026/3/12")
public abstract class AbstractBooleanList implements BooleanList {

    protected AbstractBooleanList() {
        super();
    }

    @NotNull
    @Override
    public AbstractBooleanList duplicate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // pass; always healthy
    }

    @Override
    public abstract int booleanLength();

    @Override
    public boolean isEmpty() {
        return booleanLength() == 0;
    }

    @Override
    public boolean isFull() {
        return false; // never full
    }

    @Override
    public boolean contains(boolean t) {
        final int n = booleanLength();
        for (int i = 0; i < n; i++) {
            if (getBoolean(i) == t) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void forEach(@NotNull BooleanConsumer action) {
        final int n = booleanLength();
        for (int i = 0; i < n; i++) {
            action.accept(getBoolean(i));
        }
    }

    @NotNull
    @Override
    public Iterator<Boolean> iterator() {
        return new Iterator<>() {

            private int index;

            @Override
            public boolean hasNext() {
                return index < booleanLength();
            }

            @Override
            public Boolean next() {
                return getBoolean(index++); // no check for commodification; simple and fast
            }

            @Override
            public void remove() {
                final int newIndex = index - 1;
                removeAt(newIndex);
                index = newIndex;
            }
        };
    }

    @CodeHistory(date = "2026/3/36")
    private class NoModificationSpliterator implements SpliteratorOfBoolean {

        private final int limit;

        private int position;

        NoModificationSpliterator() {
            super();
            limit = booleanLength();
        }

        NoModificationSpliterator(int position, int limit) {
            super();
            this.position = position;
            this.limit = limit;
        }

        @Override
        public long estimateSize() {
            return limit - position;
        }

        @Override
        public int characteristics() {
            return SIZED | SUBSIZED;
        }

        @Override
        public boolean tryAdvance(BooleanConsumer action) {
            if (position < limit) {
                action.accept(getBoolean(position)); // no check for commodification; simple and fast
                return true;
            } else {
                return false;
            }
        }

        @Override
        public SpliteratorOfBoolean trySplit() {
            final int start = position;
            final int split = (start + limit) >>> 1; // no overflow
            if (start < split) {
                position = split;
                return new NoModificationSpliterator(split, start);
            } else {
                return null;
            }
        }
    }

    @NotNull
    @Override
    public Spliterator<Boolean> spliterator() {
        return new NoModificationSpliterator();
    }

    @CodeHistory(date = "2025/5/30")
    private class NoModificationListIterator implements ListIterator<Boolean> {

        private int index;

        NoModificationListIterator(int index) {
            super();
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index < booleanLength();
        }

        @Override
        public Boolean next() {
            return getBoolean(index++); // no check for commodification; simple and fast
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public Boolean previous() {
            return getBoolean(--index);
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(Boolean x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(Boolean x) {
            throw new UnsupportedOperationException();
        }
    }

    @NotNull
    @Override
    public ListIterator<Boolean> listIterator(int i) {
        return new NoModificationListIterator(i);
    }

    @NotNull
    @Override
    public boolean[] toBooleanArray() {
        final boolean[] array = new boolean[booleanLength()];
        getAll(array, 0);
        return array;
    }

    @Override
    public void getAll(@NotNull boolean[] dst, int dstOffset) {
        getRange(0, dst, dstOffset, booleanLength());
    }

    @Override
    public void getRange(int srcOffset, @NotNull boolean[] dst, int dstOffset, int length) {
        for (int i = 0; i < length; i++) {
            dst[dstOffset++] = getBoolean(srcOffset++);
        }
    }

    @Override
    public abstract boolean getBoolean(int index);

    @Override
    public boolean getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getBoolean(0);
    }

    @Override
    public boolean getFirst(boolean k) {
        if (isEmpty()) {
            return k;
        }
        return getBoolean(0);
    }

    @Override
    public boolean getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getBoolean(booleanLength() - 1);
    }

    @Override
    public boolean getLast(boolean k) {
        if (isEmpty()) {
            return k;
        }
        return getBoolean(booleanLength() - 1);
    }

    @Override
    public boolean offerAt(int i, boolean t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerFirst(boolean t) {
        return offerAt(0, t);
    }

    @Override
    public boolean offerLast(boolean t) {
        return offerAt(booleanLength(), t);
    }

    @Override
    public boolean removeAt(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeFirst() {
        try {
            return removeAt(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean removeLast() {
        try {
            return removeAt(booleanLength() - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void removeRange(int startIndex, int endIndex) {
        for (int index = endIndex + 1; index >= startIndex; index--) {
            removeAt(index);
        }
    }

    @Override
    public boolean setBoolean(int i, boolean t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean flipBoolean(int i) {
        return setBoolean(i, !getBoolean(i));
    }

    @Override
    public void swap(int i, int j) {
        setBoolean(i, setBoolean(j, getBoolean(i)));
    }

    @Override
    public void clear() {
        removeRange(0, booleanLength());
    }

    @Override
    public void reverse() {
        int i = 0;
        int j = booleanLength() - 1;
        while (i < j) {
            swap(i, j);
            i++;
            j--;
        }
    }

    @Override
    public void rotate(int d) {
        // todo
    }

    @Override
    public int count(boolean t) {
        final int n = booleanLength();
        int c = 0;
        for (int i = 0; i < n; i++) {
            if (getBoolean(i) == t) {
                c++;
            }
        }
        return c;
    }

    @Override
    public boolean equals(@NotNull BooleanList that) {
        final int n = this.booleanLength();
        if (that.booleanLength() != n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (this.getBoolean(i) != that.getBoolean(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof BooleanList && this.equals((BooleanList) o);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        final int n = booleanLength();
        for (int i = 0; i < n; i++) {
            hash = 31 * hash + (getBoolean(i) ? 1231 : 1237);
        }
        return hash;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + booleanLength() + ">";
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder();
        stringifyDetail(sb);
        return sb.toString();
    }

    public void stringifyDetail(@NotNull StringBuilder sb) {
        sb.append("[");
        final int n = booleanLength();
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(getBoolean(i));
        }
        sb.append("]");
    }

    @Override
    public String toString() {
        return getClass().getName() + " " + summaryToString() + " " + detailToString();
    }
}
