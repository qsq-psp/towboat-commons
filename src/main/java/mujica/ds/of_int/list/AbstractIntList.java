package mujica.ds.of_int.list;

import mujica.reflect.function.IntEntryConsumer;
import mujica.reflect.function.IntEntryPredicate;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

@CodeHistory(date = "2025/5/30")
public abstract class AbstractIntList implements IntList {

    private static final long serialVersionUID = 0x26e6669ce2dcb8f6L;

    protected AbstractIntList() {
        super();
    }

    @NotNull
    @Override
    public AbstractIntList duplicate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // pass; always healthy
    }

    @Override
    public boolean isEmpty() {
        return intLength() == 0;
    }

    @Override
    public boolean isFull() {
        return false; // never full
    }

    @Override
    public boolean contains(int t) {
        return firstIndexOf(t) != -1;
    }

    @Override
    public void forEach(@NotNull IntConsumer action) {
        final int n = intLength();
        for (int i = 0; i < n; i++) {
            action.accept(getInt(i));
        }
    }

    @Override
    public void forEach(@NotNull IntEntryConsumer action) {
        final int n = intLength();
        for (int i = 0; i < n; i++) {
            action.accept(i, getInt(i));
        }
    }

    /**
     * No check for commodification; simple and fast
     */
    @CodeHistory(date = "2025/5/30")
    private class NoModificationIterator implements PrimitiveIterator.OfInt {

        private int index;

        @Override
        public boolean hasNext() {
            return index < intLength();
        }

        @Override
        public int nextInt() {
            return getInt(index++);
        }

        @Override
        public void remove() {
            final int newIndex = index - 1;
            removeAt(newIndex);
            index = newIndex;
        }
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new NoModificationIterator();
    }

    /**
     * No check for commodification; simple and fast
     */
    @CodeHistory(date = "2025/5/30")
    private class NoModificationSpliterator implements Spliterator.OfInt {

        private final int limit;

        private int position;

        NoModificationSpliterator() {
            super();
            limit = intLength();
        }

        NoModificationSpliterator(int position, int limit) {
            super();
            this.position = position;
            this.limit = limit;
        }

        @Override
        public long estimateSize() {
            return intLength();
        }

        @Override
        public int characteristics() {
            return SIZED | SUBSIZED;
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (position < intLength()) {
                action.accept(getInt(position));
                return true;
            } else {
                return false;
            }
        }

        @Override
        @Nullable
        public OfInt trySplit() {
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
    public Spliterator<Integer> spliterator() {
        return new NoModificationSpliterator();
    }

    /**
     * No check for commodification; simple and fast
     */
    @CodeHistory(date = "2025/5/30")
    private class NoModificationListIterator implements ListIterator<Integer> {

        private int index;

        NoModificationListIterator(int index) {
            super();
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index < intLength();
        }

        @Override
        public Integer next() {
            return getInt(index++);
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public Integer previous() {
            return getInt(--index);
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
        public void set(Integer integer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(Integer integer) {
            throw new UnsupportedOperationException();
        }
    }

    @NotNull
    @Override
    public ListIterator<Integer> listIterator(int i) {
        return new NoModificationListIterator(i);
    }

    @NotNull
    @Override
    public int[] toArray() {
        final int[] array = new int[intLength()];
        getAll(array, 0);
        return array;
    }

    @Override
    public void getAll(@NotNull int[] dst, int dstOffset) {
        getRange(0, dst, dstOffset, intLength());
    }

    @Override
    public void getRange(int srcOffset, @NotNull int[] dst, int dstOffset, int length) {
        for (int i = 0; i < length; i++) {
            dst[dstOffset++] = getInt(srcOffset++);
        }
    }

    @Override
    public abstract int getInt(int i);

    @Override
    public int getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getInt(0);
    }

    @Override
    public int getFirst(int k) {
        if (isEmpty()) {
            return k;
        }
        return getInt(0);
    }

    @Override
    public int getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getInt(intLength() - 1);
    }

    @Override
    public int getLast(int k) {
        if (isEmpty()) {
            return k;
        }
        return getInt(intLength() - 1);
    }

    @Override
    public boolean offerAt(int i, int t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerFirst(int t) {
        return offerAt(0, t);
    }

    @Override
    public boolean offerLast(int t) {
        return offerAt(intLength(), t);
    }

    @Override
    public int removeAt(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int removeFirst() {
        try {
            return removeAt(0);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int removeLast() {
        try {
            return removeAt(intLength() - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void removeRange(int fi, int ti) {
        for (int i = ti + 1; i >= fi; i--) {
            removeAt(i);
        }
    }

    public void removeIf(@NotNull IntPredicate filter) {
        int n = intLength();
        int i = 0;
        while (i < n) {
            if (filter.test(getInt(i))) {
                i++;
            } else {
                removeAt(i);
                n--;
            }
        }
    }

    @Override
    public void removeIf(@NotNull IntEntryPredicate filter) {
        int n = intLength();
        int i = 0;
        while (i < n) {
            if (filter.test(i, getInt(i))) {
                i++;
            } else {
                removeAt(i);
                n--;
            }
        }
    }

    @Override
    public int setInt(int i, int t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void swap(int i, int j) {
        setInt(i, setInt(j, getInt(i)));
    }

    public void map(@NotNull IntUnaryOperator operator) {
        final int n = intLength();
        for (int i = 0; i < n; i++) {
            setInt(i, operator.applyAsInt(getInt(i)));
        }
    }

    public int reduce(@NotNull IntBinaryOperator operator, int k) {
        final int n = intLength();
        if (n == 0) {
            return k;
        }
        int t = getInt(0);
        for (int i = 0; i < n; i++) {
            t = operator.applyAsInt(t, getInt(i));
        }
        return t;
    }

    @Override
    public int firstIndexOf(int t) {
        return firstIndexOf(0, t);
    }

    @Override
    public int firstIndexOf(int i, int t) {
        final int n = intLength();
        for (; i < n; i++) {
            if (getInt(i) == t) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(int t) {
        return lastIndexOf(intLength() - 1, t);
    }

    @Override
    public int lastIndexOf(int i, int t) {
        for (; i >= 0; i--) {
            if (getInt(i) == t) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void clear() {
        removeRange(0, intLength());
    }

    @Override
    public void reverse() {
        int i = 0;
        int j = intLength() - 1;
        while (i < j) {
            swap(i, j);
            i++;
            j--;
        }
    }

    @Override
    public void rotate(int d) {
        final int n = intLength();
        if (n <= 1) {
            return;
        }
        d %= n;
        if (d == 0) {
            return;
        }
        if (d < 0) {
            d += n;
        }
        if (d == 1) {
            int t = getInt(n - 1);
            for (int i = 0; i < n; i++) {
                t = setInt(i, t);
            }
        } else if (d == n - 1) {
            int t = getInt(0);
            for (int i = n - 1; i >= 0; i--) {
                t = setInt(i, t);
            }
        } else {
            int[] array = toArray();
            int b = n - d;
            for (int i = 0; i < d; i++) {
                setInt(i, array[b + i]);
            }
            for (int i = d; i < n; i++) {
                setInt(i, array[i - d]);
            }
        }
    }

    @Override
    public void sort(boolean descending) {
        final int n = intLength();
        if (n <= 2) {
            if (n <= 1) {
                return;
            }
            if (descending) {
                if (getInt(0) < getInt(1)) {
                    swap(0, 1);
                }
            } else {
                if (getInt(0) > getInt(1)) {
                    swap(0, 1);
                }
            }
            return;
        }
        final int[] array = toArray();
        Arrays.sort(array);
        if (descending) {
            int i = 0;
            for (int j = array.length - 1; j >= 0; j--) {
                setInt(i++, array[j]);
            }
        } else {
            for (int i = array.length - 1; i >= 0; i--) {
                setInt(i, array[i]);
            }
        }
    }

    @Override
    public int min() {
        int w = Integer.MAX_VALUE;
        for (int v : this) {
            if (v < w) {
                w = v;
            }
        }
        return w;
    }

    @Override
    public int max() {
        int w = Integer.MIN_VALUE;
        for (int v : this) {
            if (v > w) {
                w = v;
            }
        }
        return w;
    }

    @Override
    public int sum() {
        int w = 0;
        for (int v : this) {
            w += v;
        }
        return w;
    }

    @Override
    public int xor() {
        int w = 0;
        for (int v : this) {
            w ^= v;
        }
        return w;
    }

    @Override
    public void bitwiseNot() {
        bitwiseXor(-1);
    }

    @Override
    public void bitwiseAnd(int t) {
        if (t == -1) {
            return;
        }
        final int n = intLength();
        for (int i = 0; i < n; i++) {
            setInt(i, getInt(i) & t);
        }
    }

    @Override
    public void bitwiseOr(int t) {
        if (t == 0) {
            return;
        }
        final int n = intLength();
        for (int i = 0; i < n; i++) {
            setInt(i, getInt(i) | t);
        }
    }

    @Override
    public void bitwiseXor(int t) {
        if (t == 0) {
            return;
        }
        final int n = intLength();
        for (int i = 0; i < n; i++) {
            setInt(i, getInt(i) ^ t);
        }
    }

    @Override
    public boolean equals(@NotNull IntList that) {
        final int n = this.intLength();
        if (that.intLength() != n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (this.getInt(i) != that.getInt(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof IntList && this.equals((IntList) o);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        final int n = intLength();
        for (int i = 0; i < n; i++) {
            hash = 31 * hash + getInt(i);
        }
        return hash;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + intLength() + ">";
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
        final int n = intLength();
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(getInt(i));
        }
        sb.append("]");
    }

    @Override
    public String toString() {
        return getClass().getName() + " " + summaryToString() + " " + detailToString();
    }
}
