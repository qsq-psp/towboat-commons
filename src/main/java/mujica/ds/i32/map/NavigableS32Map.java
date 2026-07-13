package mujica.ds.i32.map;

import mujica.ds.i32.I32Slot;
import mujica.ds.i32.S32;
import mujica.ds.i32.list.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.IntConsumer;

@CodeHistory(date = "2026/1/12")
@DirectSubclass({JdkNavigableS32Map.class, IntervalS32Map.class})
public abstract class NavigableS32Map extends IterableI32Map {

    protected NavigableS32Map() {
        super();
    }

    @NotNull
    @Override
    public abstract NavigableS32Map duplicate();

    /**
     * outKey < inKey
     */
    public boolean lowerKey(@NotNull I32Slot keySlot) {
        final int inKey = keySlot.getI32();
        forEachKey(key -> {
            int outKey = keySlot.getI32();
            if ((outKey == inKey || outKey < key) && key < inKey) {
                keySlot.setI32(key);
            }
        });
        return keySlot.getI32() != inKey;
    }

    /**
     * outKey < inKey
     */
    @NotNull
    public OptionalInt lowerKey(int key) {
        final S32 slot = new S32(key);
        if (lowerKey(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey <= inKey
     */
    public boolean floorKey(@NotNull I32Slot keySlot) {
        if (getI32(keySlot.getI32()) != 0) {
            return true;
        }
        return lowerKey(keySlot);
    }

    /**
     * outKey <= inKey
     */
    @NotNull
    public OptionalInt floorKey(int key) {
        final S32 slot = new S32(key);
        if (floorKey(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey > inKey
     */
    public boolean higherKey(@NotNull I32Slot keySlot) {
        final int inKey = keySlot.getI32();
        forEachKey(key -> {
            int outKey = keySlot.getI32();
            if (inKey < key && (key < outKey || inKey == outKey)) {
                keySlot.setI32(key);
            }
        });
        return keySlot.getI32() != inKey;
    }

    /**
     * outKey > inKey
     */
    @NotNull
    public OptionalInt higherKey(int key) {
        final S32 slot = new S32(key);
        if (higherKey(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey >= inKey
     */
    public boolean ceilingKey(@NotNull I32Slot keySlot) {
        if (getI32(keySlot.getI32()) != 0) {
            return true;
        }
        return higherKey(keySlot);
    }

    /**
     * outKey >= inKey
     */
    @NotNull
    public OptionalInt ceilingKey(int key) {
        final S32 slot = new S32(key);
        if (ceilingKey(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    public boolean firstKey(@NotNull I32Slot keySlot) {
        int key = Integer.MIN_VALUE;
        do {
            if (getI32(key) != 0) {
                keySlot.setI32(key);
                return true;
            }
        } while (++key != Integer.MIN_VALUE);
        return false;
    }

    @NotNull
    public OptionalInt firstKey() {
        final S32 slot = new S32();
        if (firstKey(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    public boolean lastKey(@NotNull I32Slot keySlot) {
        int key = Integer.MAX_VALUE;
        do {
            if (getI32(key) != 0) {
                keySlot.setI32(key);
                return true;
            }
        } while (--key != Integer.MAX_VALUE);
        return false;
    }

    @NotNull
    public OptionalInt lastKey() {
        final S32 slot = new S32();
        if (lastKey(slot)) {
            return OptionalInt.of(slot.getI32());
        } else {
            return OptionalInt.empty();
        }
    }

    @NotNull
    public Iterator<Entry> ascendingIterator() {
        final ArrayList<Entry> list = new ArrayList<>();
        for (Entry entry : this) {
            list.add(entry);
        }
        list.sort(Comparator.comparingInt(Entry::getI32Key));
        return list.iterator();
    }

    @NotNull
    public Iterator<Entry> descendingIterator() {
        final ArrayList<Entry> list = new ArrayList<>();
        for (Entry entry : this) {
            list.add(entry);
        }
        list.sort(Comparator.comparingInt(entry -> ~entry.getI32Key()));
        return list.iterator();
    }

    public void ascendingForEach(@NotNull IntEntryConsumer action) {
        final Iterator<Entry> iterator = ascendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getI32Key(), entry.getI32Value());
        }
    }

    public void descendingForEach(@NotNull IntEntryConsumer action) {
        final Iterator<Entry> iterator = descendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getI32Key(), entry.getI32Value());
        }
    }

    public void ascendingForEachKey(@NotNull IntConsumer action) {
        final Iterator<Entry> iterator = ascendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getI32Key());
        }
    }

    public void descendingForEachKey(@NotNull IntConsumer action) {
        final Iterator<Entry> iterator = descendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getI32Key());
        }
    }

    public void ascendingForEachValue(@NotNull IntConsumer action) {
        final Iterator<Entry> iterator = ascendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getI32Value());
        }
    }

    public void descendingForEachValue(@NotNull IntConsumer action) {
        final Iterator<Entry> iterator = descendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getI32Value());
        }
    }
}
