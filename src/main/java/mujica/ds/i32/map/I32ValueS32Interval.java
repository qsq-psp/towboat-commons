package mujica.ds.i32.map;

import mujica.ds.InvariantException;
import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@CodeHistory(date = "2026/1/17", name = "IntValueInterval")
@CodeHistory(date = "2026/5/8")
@DirectSubclass({Interval32.class})
public abstract class I32ValueS32Interval extends IntervalS32Map implements I32Slot {

    protected I32ValueS32Interval() {
        super();
    }

    @NotNull
    @Override
    public abstract I32ValueS32Interval duplicate();

    public abstract int getLeft();

    public abstract int getRight();

    @Override
    public abstract int getI32();

    public abstract void setLeft(int newLeft);

    public abstract void setRight(int newRight);

    @Override
    public abstract void setI32(int newValue);

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int left = getLeft();
        final int right = getRight();
        if (left > right) {
            throw new InvariantException("left = " + left + ", right = " + right);
        }
        if (getI32() == 0) {
            throw new InvariantException("value = 0");
        }
    }

    @Override
    public int getI32(int key) {
        if (getLeft() <= key && key <= getRight()) {
            return getI32();
        } else {
            return 0;
        }
    }

    @Override
    public int putI32(int key, int newValue) {
        if (newValue != 0) {
            if (key == getLeft() && key == getRight()) {
                int oldValue = getI32();
                setI32(newValue);
                return oldValue;
            }
            if (getI32() == newValue) {
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
    public int addI32(int key, int delta) {
        if (delta == 0) {
            return getI32(key);
        }
        if (key == getLeft() && key == getRight()) {
            int oldValue = getI32();
            int newValue = oldValue + delta;
            if (newValue == 0) {
                throw new IllegalArgumentException();
            }
            setI32(newValue);
            return oldValue;
        }
        if (getI32() == delta) {
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
    public int addS32(int key, int delta) {
        if (delta == 0) {
            return getI32(key);
        }
        if (key == getLeft() && key == getRight()) {
            int oldValue = getI32();
            int newValue = Math.addExact(oldValue, delta);
            if (newValue == 0) {
                throw new IllegalArgumentException();
            }
            setI32(newValue);
            return oldValue;
        }
        if (getI32() == delta) {
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
                    entry.value = getI32();
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
        return nonZeroKeyCount() * getI32();
    }

    protected static final int HASH_MULTIPLIER = 59;

    @Override
    public int hashCode() {
        return (getLeft() * HASH_MULTIPLIER + getRight()) * HASH_MULTIPLIER + getI32();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof I32ValueS32Interval)) {
            return false;
        }
        final I32ValueS32Interval that = (I32ValueS32Interval) obj;
        return this.getLeft() == that.getLeft() && this.getRight() == that.getRight() && this.getI32() == that.getI32();
    }

    @Override
    public String toString() {
        return "[" + getLeft() + ", " + getRight() + "] = " + getI32();
    }
}
