package mujica.ds.i32.set;

import mujica.ds.any.list.MonotonicityDirection;
import mujica.ds.i32.I32Slot;
import mujica.ds.i32.S32;
import mujica.ds.i32.list.CopyOnResizeIntList;
import mujica.ds.i32.list.IntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.IntConsumer;

@CodeHistory(date = "2026/1/17")
public abstract class NavigableIntSet extends IntSet {

    private static final long serialVersionUID = 0x09DAE57FDA577EDEL;

    protected NavigableIntSet() {
        super();
    }

    @NotNull
    @Override
    public abstract NavigableIntSet duplicate();

    /**
     * outKey < inKey
     */
    public boolean lower(@NotNull I32Slot keySlot) {
        final int inKey = keySlot.getI32();
        int outKey = inKey;
        for (int key : this) {
            if ((outKey == inKey || outKey < key) && key < inKey) {
                outKey = key;
            }
        }
        if (outKey == inKey) {
            return false;
        } else {
            keySlot.setI32(outKey);
            return true;
        }
    }

    /**
     * outKey < inKey
     */
    @NotNull
    public OptionalInt lower(int key) {
        final S32 slot = new S32(key);
        if (lower(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey <= inKey
     */
    public boolean floor(@NotNull I32Slot keySlot) {
        if (contains(keySlot.getI32())) {
            return true;
        }
        return lower(keySlot);
    }

    /**
     * outKey <= inKey
     */
    @NotNull
    public OptionalInt floor(int key) {
        final S32 slot = new S32(key);
        if (floor(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey > inKey
     */
    public boolean higher(@NotNull I32Slot keySlot) {
        final int inKey = keySlot.getI32();
        int outKey = inKey;
        for (int key : this) {
            if (inKey < key && (key < outKey || inKey == outKey)) {
                outKey = key;
            }
        }
        if (outKey == inKey) {
            return false;
        } else {
            keySlot.setI32(outKey);
            return true;
        }
    }

    /**
     * outKey > inKey
     */
    @NotNull
    public OptionalInt higher(int key) {
        final S32 slot = new S32(key);
        if (higher(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey >= inKey
     */
    public boolean ceiling(@NotNull I32Slot keySlot) {
        if (contains(keySlot.getI32())) {
            return true;
        }
        return higher(keySlot);
    }

    /**
     * outKey >= inKey
     */
    @NotNull
    public OptionalInt ceiling(int key) {
        final S32 slot = new S32(key);
        if (ceiling(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    public boolean first(@NotNull I32Slot keySlot) {
        int outKey = Integer.MIN_VALUE;
        for (int key : this) {
            if (key == Integer.MIN_VALUE) {
                keySlot.setI32(key);
                return true;
            }
            if (key < outKey || outKey == Integer.MIN_VALUE) {
                outKey = key;
            }
        }
        if (outKey == Integer.MIN_VALUE) {
            return false;
        } else {
            keySlot.setI32(outKey);
            return true;
        }
    }

    @NotNull
    public OptionalInt first() {
        final S32 slot = new S32();
        if (first(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    public boolean last(@NotNull I32Slot keySlot) {
        int outKey = Integer.MAX_VALUE;
        for (int key : this) {
            if (key == Integer.MAX_VALUE) {
                keySlot.setI32(key);
                return true;
            }
            if (key > outKey || outKey == Integer.MAX_VALUE) {
                outKey = key;
            }
        }
        if (outKey == Integer.MAX_VALUE) {
            return false;
        } else {
            keySlot.setI32(outKey);
            return true;
        }
    }

    @NotNull
    public OptionalInt last() {
        final S32 slot = new S32();
        if (last(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    public void ascendingForEach(@NotNull IntConsumer action) {
        final IntList list = new CopyOnResizeIntList(null);
        for (int key : this) {
            list.offerLast(key);
        }
        list.sort(MonotonicityDirection.ASCENDING);
        list.forEach(action);
    }

    public void descendingForEach(@NotNull IntConsumer action) {
        final IntList list = new CopyOnResizeIntList(null);
        for (int key : this) {
            list.offerLast(key);
        }
        list.sort(MonotonicityDirection.DESCENDING);
        list.forEach(action);
    }

    @NotNull
    public Iterator<Integer> ascendingIterator() {
        final IntList list = new CopyOnResizeIntList(null);
        for (int key : this) {
            list.offerLast(key);
        }
        list.sort(MonotonicityDirection.ASCENDING);
        return list.iterator();
    }

    @NotNull
    public Iterator<Integer> descendingIterator() {
        final IntList list = new CopyOnResizeIntList(null);
        for (int key : this) {
            list.offerLast(key);
        }
        list.sort(MonotonicityDirection.DESCENDING);
        return list.iterator();
    }
}
