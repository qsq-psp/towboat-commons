package mujica.ds.of_int.map;

import mujica.ds.of_int.IntSlot;
import mujica.ds.of_int.PublicIntSlot;
import mujica.ds.of_int.list.IntEntryConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.IntConsumer;

/**
 * Created on 2026/1/12.
 */
public abstract class NavigableIntMap extends IterableIntMap {

    protected NavigableIntMap() {
        super();
    }

    @NotNull
    @Override
    public abstract NavigableIntMap duplicate();

    /**
     * outKey < inKey
     */
    public boolean lowerKey(@NotNull IntSlot keySlot) {
        final int inKey = keySlot.getInt();
        forEachKey(key -> {
            int outKey = keySlot.getInt();
            if ((outKey == inKey || outKey < key) && key < inKey) {
                keySlot.setInt(key);
            }
        });
        return keySlot.getInt() != inKey;
    }

    /**
     * outKey < inKey
     */
    @NotNull
    public OptionalInt lowerKey(int key) {
        final PublicIntSlot slot = new PublicIntSlot(key);
        if (lowerKey(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey <= inKey
     */
    public boolean floorKey(@NotNull IntSlot keySlot) {
        if (getInt(keySlot.getInt()) != 0) {
            return true;
        }
        return lowerKey(keySlot);
    }

    /**
     * outKey <= inKey
     */
    @NotNull
    public OptionalInt floorKey(int key) {
        final PublicIntSlot slot = new PublicIntSlot(key);
        if (floorKey(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey > inKey
     */
    public boolean higherKey(@NotNull IntSlot keySlot) {
        final int inKey = keySlot.getInt();
        forEachKey(key -> {
            int outKey = keySlot.getInt();
            if (inKey < key && (key < outKey || inKey == outKey)) {
                keySlot.setInt(key);
            }
        });
        return keySlot.getInt() != inKey;
    }

    /**
     * outKey > inKey
     */
    @NotNull
    public OptionalInt higherKey(int key) {
        final PublicIntSlot slot = new PublicIntSlot(key);
        if (higherKey(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    /**
     * outKey >= inKey
     */
    public boolean ceilingKey(@NotNull IntSlot keySlot) {
        if (getInt(keySlot.getInt()) != 0) {
            return true;
        }
        return higherKey(keySlot);
    }

    /**
     * outKey >= inKey
     */
    @NotNull
    public OptionalInt ceilingKey(int key) {
        final PublicIntSlot slot = new PublicIntSlot(key);
        if (ceilingKey(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    public boolean firstKey(@NotNull IntSlot keySlot) {
        int key = Integer.MIN_VALUE;
        do {
            if (getInt(key) != 0) {
                keySlot.setInt(key);
                return true;
            }
        } while (++key != Integer.MIN_VALUE);
        return false;
    }

    @NotNull
    public OptionalInt firstKey() {
        final PublicIntSlot slot = new PublicIntSlot();
        if (firstKey(slot)) {
            return OptionalInt.of(slot.value);
        } else {
            return OptionalInt.empty();
        }
    }

    public boolean lastKey(@NotNull IntSlot keySlot) {
        int key = Integer.MAX_VALUE;
        do {
            if (getInt(key) != 0) {
                keySlot.setInt(key);
                return true;
            }
        } while (--key != Integer.MAX_VALUE);
        return false;
    }

    @NotNull
    public OptionalInt lastKey() {
        final PublicIntSlot slot = new PublicIntSlot();
        if (lastKey(slot)) {
            return OptionalInt.of(slot.value);
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
        list.sort(Comparator.comparingInt(Entry::getIntKey));
        return list.iterator();
    }

    @NotNull
    public Iterator<Entry> descendingIterator() {
        final ArrayList<Entry> list = new ArrayList<>();
        for (Entry entry : this) {
            list.add(entry);
        }
        list.sort(Comparator.comparingInt(entry -> ~entry.getIntKey()));
        return list.iterator();
    }

    public void ascendingForEach(@NotNull IntEntryConsumer action) {
        final Iterator<Entry> iterator = ascendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getIntKey(), entry.getIntValue());
        }
    }

    public void descendingForEach(@NotNull IntEntryConsumer action) {
        final Iterator<Entry> iterator = descendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getIntKey(), entry.getIntValue());
        }
    }

    public void ascendingForEachKey(@NotNull IntConsumer action) {
        final Iterator<Entry> iterator = ascendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getIntKey());
        }
    }

    public void descendingForEachKey(@NotNull IntConsumer action) {
        final Iterator<Entry> iterator = descendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getIntKey());
        }
    }

    public void ascendingForEachValue(@NotNull IntConsumer action) {
        final Iterator<Entry> iterator = ascendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getIntValue());
        }
    }

    public void descendingForEachValue(@NotNull IntConsumer action) {
        final Iterator<Entry> iterator = descendingIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            action.accept(entry.getIntValue());
        }
    }
}
