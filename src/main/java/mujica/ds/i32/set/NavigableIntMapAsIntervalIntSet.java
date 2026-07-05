package mujica.ds.i32.set;

import mujica.ds.InvariantException;
import mujica.ds.i32.I32Slot;
import mujica.ds.i32.S32;
import mujica.ds.i32.map.IntMap;
import mujica.ds.i32.map.NavigableIntMap;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Created on 2026/1/18.
 */
public class NavigableIntMapAsIntervalIntSet extends IntervalIntSet {

    @NotNull
    final NavigableIntMap map;

    final S32 slot = new S32();

    public NavigableIntMapAsIntervalIntSet(@NotNull NavigableIntMap map) {
        super();
        this.map = map;
    }

    @NotNull
    @Override
    public NavigableIntMapAsIntervalIntSet duplicate() {
        return new NavigableIntMapAsIntervalIntSet(map.duplicate());
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        map.checkHealth(consumer);
        long start = Integer.MIN_VALUE;
        for (IntMap.Entry entry : map) {
            int left = entry.getIntKey();
            int right = entry.getIntValue();
            if (left < start) {
                consumer.accept(new InvariantException(left + "<" + start));
            }
            if (right < left) {
                consumer.accept(new InvariantException(right + "<" + left));
            }
            start = right + 2; // start is long so there is no overflow
        }
    }

    @Override
    public long intLengthAsLong() {
        long n = 0L;
        for (IntMap.Entry entry : map) {
            n = n + 1L + entry.getIntValue() - entry.getIntKey();
        }
        return n;
    }

    @Override
    public boolean isEmpty() {
        return map.nonZeroKeyCount() == 0;
    }

    @Override
    public boolean isFull() {
        return map.getInt(Integer.MIN_VALUE) == Integer.MAX_VALUE;
    }

    @Override
    public boolean contains(int t) {
        slot.setI32(t);
        return map.floorKey(slot) && t <= map.getInt(slot.getI32());
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean lower(@NotNull I32Slot keySlot) {
        final int inKey = keySlot.getI32();
        if (!map.lowerKey(keySlot)) {
            return false;
        }
        final int right = map.getInt(keySlot.getI32());
        keySlot.setI32(Math.min(inKey - 1, right));
        return true;
    }

    @Override
    public boolean floor(@NotNull I32Slot keySlot) {
        final int inKey = keySlot.getI32();
        if (!map.floorKey(keySlot)) {
            return false;
        }
        final int right = map.getInt(keySlot.getI32());
        keySlot.setI32(Math.min(inKey, right));
        return true;
    }

    @Override
    public boolean higher(@NotNull I32Slot keySlot) {
        final int inKey = keySlot.getI32();
        if (map.floorKey(keySlot)) {
            int right = map.getInt(keySlot.getI32());
            if (inKey < right) {
                keySlot.setI32(inKey + 1);
                return true;
            }
        }
        keySlot.setI32(inKey);
        return map.higherKey(keySlot);
    }

    @Override
    public boolean ceiling(@NotNull I32Slot keySlot) {
        final int inKey = keySlot.getI32();
        if (map.floorKey(keySlot)) {
            int right = map.getInt(keySlot.getI32());
            if (inKey <= right) {
                keySlot.setI32(inKey);
                return true;
            }
        }
        keySlot.setI32(inKey);
        return map.ceilingKey(keySlot);
    }

    @Override
    public boolean first(@NotNull I32Slot keySlot) {
        return map.firstKey(keySlot);
    }

    @Override
    public boolean last(@NotNull I32Slot keySlot) {
        if (map.lastKey(keySlot)) {
            keySlot.setI32(map.getInt(keySlot.getI32()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int intervalLeft(int inKey) {
        slot.setI32(inKey);
        if (map.floorKey(slot)) {
            int left = slot.getI32();
            int right = map.getInt(left);
            if (inKey <= right) {
                return left;
            } else {
                return right + 1;
            }
        } else {
            return Integer.MIN_VALUE;
        }
    }

    @Override
    public int intervalRight(int inKey) {
        slot.setI32(inKey);
        if (map.floorKey(slot)) {
            int right = map.getInt(slot.getI32());
            if (inKey <= right) {
                return right;
            }
        }
        slot.setI32(inKey);
        if (map.higherKey(slot)) {
            return slot.getI32() - 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean add(int t) {
        return super.add(t);
    }

    @Override
    public boolean remove(int t) {
        return super.remove(t);
    }

    @Override
    public void intervalAdd(int left, int right) {
        super.intervalAdd(left, right);
    }

    @Override
    public void intervalRemove(int left, int right) {
        super.intervalRemove(left, right);
    }

    @Override
    public void forEach(@NotNull IntConsumer action) {
        for (IntMap.Entry e : map) {
            int l = e.getIntKey();
            int r = e.getIntValue();
            while (l < r) {
                action.accept(l++);
            }
            action.accept(r);
        }
    }

    @Override
    public void ascendingForEach(@NotNull IntConsumer action) {
        final Iterator<IntMap.Entry> i = map.ascendingIterator();
        while (i.hasNext()) {
            IntMap.Entry e = i.next();
            int l = e.getIntKey();
            int r = e.getIntValue();
            while (l < r) {
                action.accept(l++);
            }
            action.accept(r);
        }
    }

    @Override
    public void descendingForEach(@NotNull IntConsumer action) {
        final Iterator<IntMap.Entry> i = map.descendingIterator();
        while (i.hasNext()) {
            IntMap.Entry e = i.next();
            int l = e.getIntKey();
            int r = e.getIntValue();
            while (l < r) {
                action.accept(r--);
            }
            action.accept(l);
        }
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new AscendingIterator(map.iterator());
    }

    @NotNull
    @Override
    public Iterator<Integer> ascendingIterator() {
        return new AscendingIterator(map.ascendingIterator());
    }

    @NotNull
    @Override
    public Iterator<Integer> descendingIterator() {
        return new DescendingIterator(map.descendingIterator());
    }

    private static class AscendingIterator implements PrimitiveIterator.OfInt {

        @NotNull
        final Iterator<IntMap.Entry> iterator;

        IntMap.Entry entry;

        int value;

        private AscendingIterator(@NotNull Iterator<IntMap.Entry> iterator) {
            super();
            this.iterator = iterator;
            if (iterator.hasNext()) {
                entry = iterator.next();
                value = entry.getIntKey();
            }
        }

        @Override
        public boolean hasNext() {
            return entry != null;
        }

        @Override
        public int nextInt() {
            final int result = value;
            value++;
            if (entry.getIntValue() < value) {
                if (iterator.hasNext()) {
                    entry = iterator.next();
                    value = entry.getIntKey();
                } else {
                    entry = null;
                }
            }
            return result;
        }
    }

    private static class DescendingIterator implements PrimitiveIterator.OfInt {

        @NotNull
        final Iterator<IntMap.Entry> iterator;

        IntMap.Entry entry;

        int value;

        private DescendingIterator(@NotNull Iterator<IntMap.Entry> iterator) {
            super();
            this.iterator = iterator;
            if (iterator.hasNext()) {
                entry = iterator.next();
                value = entry.getIntValue();
            }
        }

        @Override
        public boolean hasNext() {
            return entry != null;
        }

        @Override
        public int nextInt() {
            final int result = value;
            value--;
            if (value < entry.getIntKey()) {
                if (iterator.hasNext()) {
                    entry = iterator.next();
                    value = entry.getIntValue();
                } else {
                    entry = null;
                }
            }
            return result;
        }
    }
}
