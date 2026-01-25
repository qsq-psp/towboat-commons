package mujica.ds.of_int.set;

import mujica.ds.of_int.IntSlot;
import mujica.ds.of_int.PublicIntSlot;
import mujica.ds.of_int.list.CopyOnResizeIntList;
import mujica.ds.of_int.list.IntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.IntConsumer;

/**
 * Created on 2026/1/17.
 */
@CodeHistory(date = "2026/1/17")
public abstract class NavigableIntSet extends IntSet {

    protected NavigableIntSet() {
        super();
    }

    @NotNull
    @Override
    public abstract NavigableIntSet duplicate();

    /**
     * outKey < inKey
     */
    public boolean lower(@NotNull IntSlot keySlot) {
        final int inKey = keySlot.getInt();
        int outKey = inKey;
        for (int key : this) {
            if ((outKey == inKey || outKey < key) && key < inKey) {
                outKey = key;
            }
        }
        if (outKey == inKey) {
            return false;
        } else {
            keySlot.setInt(outKey);
            return true;
        }
    }

    /**
     * outKey < inKey
     */
    @NotNull
    public OptionalInt lower(int key) {
        final PublicIntSlot slot = new PublicIntSlot(key);
        if (lower(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey <= inKey
     */
    public boolean floor(@NotNull IntSlot keySlot) {
        if (contains(keySlot.getInt())) {
            return true;
        }
        return lower(keySlot);
    }

    /**
     * outKey <= inKey
     */
    @NotNull
    public OptionalInt floor(int key) {
        final PublicIntSlot slot = new PublicIntSlot(key);
        if (floor(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey > inKey
     */
    public boolean higher(@NotNull IntSlot keySlot) {
        final int inKey = keySlot.getInt();
        int outKey = inKey;
        for (int key : this) {
            if (inKey < key && (key < outKey || inKey == outKey)) {
                outKey = key;
            }
        }
        if (outKey == inKey) {
            return false;
        } else {
            keySlot.setInt(outKey);
            return true;
        }
    }

    /**
     * outKey > inKey
     */
    @NotNull
    public OptionalInt higher(int key) {
        final PublicIntSlot slot = new PublicIntSlot(key);
        if (higher(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey >= inKey
     */
    public boolean ceiling(@NotNull IntSlot keySlot) {
        if (contains(keySlot.getInt())) {
            return true;
        }
        return higher(keySlot);
    }

    /**
     * outKey >= inKey
     */
    @NotNull
    public OptionalInt ceiling(int key) {
        final PublicIntSlot slot = new PublicIntSlot(key);
        if (ceiling(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    public boolean first(@NotNull IntSlot keySlot) {
        int outKey = Integer.MIN_VALUE;
        for (int key : this) {
            if (key == Integer.MIN_VALUE) {
                keySlot.setInt(key);
                return true;
            }
            if (key < outKey || outKey == Integer.MIN_VALUE) {
                outKey = key;
            }
        }
        if (outKey == Integer.MIN_VALUE) {
            return false;
        } else {
            keySlot.setInt(outKey);
            return true;
        }
    }

    @NotNull
    public OptionalInt first() {
        final PublicIntSlot slot = new PublicIntSlot();
        if (first(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    public boolean last(@NotNull IntSlot keySlot) {
        int outKey = Integer.MAX_VALUE;
        for (int key : this) {
            if (key == Integer.MAX_VALUE) {
                keySlot.setInt(key);
                return true;
            }
            if (key > outKey || outKey == Integer.MAX_VALUE) {
                outKey = key;
            }
        }
        if (outKey == Integer.MAX_VALUE) {
            return false;
        } else {
            keySlot.setInt(outKey);
            return true;
        }
    }

    @NotNull
    public OptionalInt last() {
        final PublicIntSlot slot = new PublicIntSlot();
        if (last(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    public void ascendingForEach(@NotNull IntConsumer action) {
        final IntList list = new CopyOnResizeIntList(null);
        for (int key : this) {
            list.offerLast(key);
        }
        list.sort(false);
        list.forEach(action);
    }

    public void descendingForEach(@NotNull IntConsumer action) {
        final IntList list = new CopyOnResizeIntList(null);
        for (int key : this) {
            list.offerLast(key);
        }
        list.sort(true);
        list.forEach(action);
    }

    @NotNull
    public Iterator<Integer> ascendingIterator() {
        final IntList list = new CopyOnResizeIntList(null);
        for (int key : this) {
            list.offerLast(key);
        }
        list.sort(false);
        return list.iterator();
    }

    @NotNull
    public Iterator<Integer> descendingIterator() {
        final IntList list = new CopyOnResizeIntList(null);
        for (int key : this) {
            list.offerLast(key);
        }
        list.sort(true);
        return list.iterator();
    }
}
