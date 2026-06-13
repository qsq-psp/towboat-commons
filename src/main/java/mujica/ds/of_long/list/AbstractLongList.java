package mujica.ds.of_long.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

/**
 * Created on 2026/5/11.
 */
@CodeHistory(date = "2026/5/11")
public abstract class AbstractLongList implements LongList {

    protected AbstractLongList() {
        super();
    }

    @NotNull
    @Override
    public LongList duplicate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // pass; always healthy
    }

    @Override
    public abstract int longLength();

    @Override
    public boolean isEmpty() {
        return longLength() == 0;
    }

    @Override
    public boolean isFull() {
        return false; // never full
    }

    @Override
    public boolean contains(long t) {
        return firstIndexOf(t) != -1;
    }

    @Override
    public void forEach(@NotNull LongConsumer action) {
        final int n = longLength();
        for (int i = 0; i < n; i++) {
            action.accept(getLong(i));
        }
    }

    @NotNull
    @Override
    public Iterator<Long> iterator() {
        return new PrimitiveIterator.OfLong() {

            private int index;

            @Override
            public boolean hasNext() {
                return index < longLength();
            }

            @Override
            public long nextLong() {
                return getLong(index++); // no check for commodification; simple and fast
            }

            @Override
            public void remove() {
                final int newIndex = index - 1;
                removeAt(newIndex);
                index = newIndex;
            }
        };
    }

    @CodeHistory(date = "2026/5/14")
    private class NoModificationSpliterator implements Spliterator.OfLong {

        private final int limit;

        private int position;

        NoModificationSpliterator() {
            super();
            limit = longLength();
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
        public boolean tryAdvance(@NotNull LongConsumer action) {
            if (position < limit) {
                action.accept(getLong(position)); // no check for commodification; simple and fast
                return true;
            } else {
                return false;
            }
        }

        @Override
        @Nullable
        public OfLong trySplit() {
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
    public Spliterator<Long> spliterator() {
        return new NoModificationSpliterator();
    }

    @NotNull
    @Override
    public long[] toLongArray() {
        final long[] a = new long[longLength()];
        getAll(a, 0);
        return a;
    }

    @Override
    public void getAll(@NotNull long[] dst, int dstOffset) {
        getRange(0, dst, dstOffset, longLength());
    }

    @Override
    public void getRange(int srcOffset, @NotNull long[] dst, int dstOffset, int length) {
        for (int i = 0; i < length; i++) {
            dst[dstOffset++] = getLong(srcOffset++);
        }
    }

    @Override
    public abstract long getLong(int index);

    @Override
    public long getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getLong(0);
    }

    @Override
    public long getFirst(int k) {
        if (isEmpty()) {
            return k;
        }
        return getLong(0);
    }

    @Override
    public long getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getLong(longLength() - 1);
    }

    @Override
    public long getLast(int k) {
        if (isEmpty()) {
            return k;
        }
        return getLong(longLength() - 1);
    }

    @Override
    public boolean offerAt(int i, long t) {
        return false;
    }

    @Override
    public boolean offerFirst(long t) {
        return false;
    }

    @Override
    public boolean offerLast(long t) {
        return false;
    }

    @Override
    public long removeAt(int i) {
        return 0;
    }

    @Override
    public long removeFirst() {
        return 0;
    }

    @Override
    public long removeLast() {
        return 0;
    }

    @Override
    public void removeRange(int startIndex, int endIndex) {

    }

    @Override
    public void removeIf(@NotNull LongPredicate filter) {

    }

    @Override
    public long setLong(int i, long t) {
        return 0;
    }

    @Override
    public void swap(int i, int j) {

    }

    @Override
    public int firstIndexOf(long t) {
        return 0;
    }

    @Override
    public int firstIndexOf(int i, long t) {
        return 0;
    }

    @Override
    public int lastIndexOf(long t) {
        return 0;
    }

    @Override
    public int lastIndexOf(int i, long t) {
        return 0;
    }

    @Override
    public void clear() {
        removeRange(0, longLength());
    }

    @Override
    public boolean equals(@NotNull LongList that) {
        final int n = this.longLength();
        if (that.longLength() != n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (this.getLong(i) != that.getLong(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof LongList && this.equals((LongList) o);
    }

    @Override
    public int hashCode() {
        long hash = 1L;
        final int n = longLength();
        for (int i = 0; i < n; i++) {
            hash = 31L * hash + getLong(i);
        }
        return Long.hashCode(hash);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + longLength() + ">";
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
        final int n = longLength();
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(getLong(i));
        }
        sb.append("]");
    }

    @Override
    public String toString() {
        return getClass().getName() + " " + summaryToString() + " " + detailToString();
    }
}
