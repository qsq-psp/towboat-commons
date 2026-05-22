package mujica.ds.of_int.map;

import mujica.ds.InvariantException;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2026/1/17", name = "IntValueInterval")
@CodeHistory(date = "2026/5/8")
@DirectSubclass({PrivateIntValueInterval.class})
public abstract class IntValueIntInterval extends IntervalIntMap implements IntSlot {

    protected IntValueIntInterval() {
        super();
    }

    @NotNull
    @Override
    public abstract IntValueIntInterval duplicate();

    public abstract int getLeft();

    public abstract int getRight();

    @Override
    public abstract int getInt();

    public abstract void setLeft(int newLeft);

    public abstract void setRight(int newRight);

    @Override
    public abstract void setInt(int newValue);

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int left = getLeft();
        final int right = getRight();
        if (left > right) {
            throw new InvariantException("left = " + left + ", right = " + right);
        }
        if (getInt() == 0) {
            throw new InvariantException("value = 0");
        }
    }

    @Override
    public int getInt(int key) {
        if (getLeft() <= key && key <= getRight()) {
            return getInt();
        } else {
            return 0;
        }
    }

    @Override
    public int putInt(int key, int newValue) {
        if (newValue != 0) {
            if (key == getLeft() && key == getRight()) {
                int oldValue = getInt();
                setInt(newValue);
                return oldValue;
            }
            if (getInt() == newValue) {
                if (key != Integer.MAX_VALUE && key + 1 == getLeft()) {
                    setLeft(key);
                }
                if (key != Integer.MIN_VALUE && key - 1 == getRight()) {
                    setRight(key);
                }
            }
        } else {
            if (key < getLeft() || getRight() < key) {
                return 0;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int addInt(int key, int delta) {
        if (delta == 0) {
            return getInt(key);
        }
        if (key == getLeft() && key == getRight()) {
            int oldValue = getInt();
            int newValue = oldValue + delta;
            if (newValue == 0) {
                throw new IllegalArgumentException();
            }
            setInt(newValue);
            return oldValue;
        }
        if (getInt() == delta) {
            if (key != Integer.MAX_VALUE && key + 1 == getLeft()) {
                setLeft(key);
            }
            if (key != Integer.MIN_VALUE && key - 1 == getRight()) {
                setRight(key);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int addIntExact(int key, int delta) {
        if (delta == 0) {
            return getInt(key);
        }
        if (key == getLeft() && key == getRight()) {
            int oldValue = getInt();
            int newValue = Math.addExact(oldValue, delta);
            if (newValue == 0) {
                throw new IllegalArgumentException();
            }
            setInt(newValue);
            return oldValue;
        }
        if (getInt() == delta) {
            if (key != Integer.MAX_VALUE && key + 1 == getLeft()) {
                setLeft(key);
            }
            if (key != Integer.MIN_VALUE && key - 1 == getRight()) {
                setRight(key);
            }
        }
        throw new IllegalArgumentException();
    }

    @NotNull
    @Override
    public Iterator<Entry> iterator() {
        return new Iterator<>() {

            final SimpleIntMapEntry entry = new SimpleIntMapEntry();

            int next = getLeft();

            final long limit = getRight() + 1L;

            @Override
            public boolean hasNext() {
                return next < limit;
            }

            @Override
            public Entry next() {
                if (next < limit) {
                    entry.key = next++;
                    entry.value = getInt();
                    return entry;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    @Override
    public long nonZeroKeyCount() {
        return getRight() + 1L - getLeft();
    }

    @Override
    public long sumOfValues() {
        return nonZeroKeyCount() * getInt();
    }

    protected static final int HASH_MULTIPLIER = 59;

    @Override
    public int hashCode() {
        return (getLeft() * HASH_MULTIPLIER + getRight()) * HASH_MULTIPLIER + getInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntValueIntInterval)) {
            return false;
        }
        final IntValueIntInterval that = (IntValueIntInterval) obj;
        return this.getLeft() == that.getLeft() && this.getRight() == that.getRight() && this.getInt() == that.getInt();
    }

    @Override
    public String toString() {
        return "[" + getLeft() + ", " + getRight() + "] = " + getInt();
    }
}
