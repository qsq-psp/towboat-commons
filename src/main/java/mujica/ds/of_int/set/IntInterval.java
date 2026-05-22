package mujica.ds.of_int.set;

import mujica.algebra.random.RandomContext;
import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

@CodeHistory(date = "2018/7/4", project = "existence", name = "BoundI")
@CodeHistory(date = "2025/3/9")
public abstract class IntInterval extends IntervalIntSet {

    private static final long serialVersionUID = 0x0110FF0793B7AA3AL;

    protected IntInterval() {
        super();
    }

    @NotNull
    @Override
    public abstract IntInterval duplicate();

    public abstract int getLeft();

    public abstract int getRight();

    public abstract void setLeft(int newLeft);

    public abstract void setRight(int newRight);

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int left = getLeft();
        final int right = getRight();
        if (left > right) {
            throw new InvariantException("left = " + left + ", right = " + right);
        }
    }

    @Override
    public int intLength() {
        int count = getRight() - getLeft() + 1;
        if (count <= 0) {
            count = Integer.MAX_VALUE;
        }
        return count;
    }

    @Override
    public long intLengthAsLong() {
        return ((long) getRight()) - getLeft() + 1L;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isFull() {
        return getLeft() == Integer.MIN_VALUE && getRight() == Integer.MAX_VALUE;
    }

    @Override
    public boolean contains(int t) {
        return getLeft() <= t && t <= getRight();
    }

    @Override
    public boolean add(int t) {
        int bound;
        if (t < (bound = getLeft())) {
            if (t + 1 == bound) {
                setLeft(t);
                return true;
            }
        } else if (t > (bound = getRight())) {
            if (t - 1 == bound) {
                setRight(t);
                return true;
            }
        } else {
            return false;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean remove(int t) {
        final int left = getLeft();
        final int right = getRight();
        if (left < right) {
            if (t <= left) {
                if (t == left) {
                    setLeft(t + 1);
                    return true;
                } else {
                    return false;
                }
            } else if (t >= right) {
                if (t == right) {
                    setRight(t - 1);
                    return true;
                } else {
                    return false;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int getArbitrary(@Nullable RandomContext rc) throws NoSuchElementException {
        final int left = getLeft();
        if (rc == null) {
            return left;
        }
        final int right = getRight();
        if (right == Integer.MAX_VALUE) {
            if (left == Integer.MIN_VALUE) {
                return rc.nextInt();
            } else {
                return rc.nextInt(left - 1, right) + 1;
            }
        } else {
            return rc.nextInt(left, right + 1);
        }
    }

    @Override
    public int removeArbitrary(@Nullable RandomContext rc) throws NoSuchElementException {
        final int left = getLeft();
        final int right = getRight();
        if (left < right) {
            if (rc == null || rc.nextBoolean()) {
                setLeft(left + 1);
                return left;
            } else {
                setRight(right - 1);
                return right;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public int[] toIntArray() {
        final int left = getLeft();
        final int right = getRight();
        final int n = right - left + 1;
        final int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = left + i;
        }
        return array;
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new PrimitiveIterator.OfInt() {

            int next = getLeft();

            final long limit = getRight() + 1L;

            @Override
            public int nextInt() {
                if (next < limit) {
                    return next++;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public boolean hasNext() {
                return next < limit;
            }
        };
    }

    @NotNull
    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(iterator(), intLengthAsLong(), Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.SIZED);
    }

    protected static final int HASH_MULTIPLIER = 59;

    @Override
    public int hashCode() {
        return getLeft() * HASH_MULTIPLIER + getRight();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntInterval)) {
            return false;
        }
        final IntInterval that = (IntInterval) obj;
        return this.getLeft() == that.getLeft() && this.getRight() == that.getRight();
    }

    @Override
    public String toString() {
        return "[" + getLeft() + ", " + getRight() + "]";
    }
}
