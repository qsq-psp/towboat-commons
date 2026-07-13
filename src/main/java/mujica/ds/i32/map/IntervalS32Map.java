package mujica.ds.i32.map;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

@CodeHistory(date = "2026/1/12")
public abstract class IntervalS32Map extends NavigableS32Map {

    protected IntervalS32Map() {
        super();
    }

    @NotNull
    @Override
    public abstract IntervalS32Map duplicate();

    public int intervalLeft(int inKey) {
        final int value = getI32(inKey);
        int outKey = inKey;
        do {
            outKey--;
        } while (outKey < inKey && value == getI32(outKey));
        return outKey;
    }

    public int intervalRight(int inKey) {
        final int value = getI32(inKey);
        int outKey = inKey;
        do {
            outKey++;
        } while (outKey > inKey && value == getI32(outKey));
        return outKey;
    }

    public void intervalPutInt(int left, int right, int value) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            putI32(key, value);
        }
        putI32(right, value);
    }

    public void intervalAddInt(int left, int right, int delta) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            addI32(key, delta);
        }
        addI32(right, delta);
    }

    public void intervalLimitIntMax(int left, int right, int max) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            if (getI32(key) > max) {
                putI32(key, max);
            }
        }
        if (getI32(right) > max) {
            putI32(right, max);
        }
    }

    public void intervalLimitIntMin(int left, int right, int min) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            if (getI32(key) < min) {
                putI32(key, min);
            }
        }
        if (getI32(right) < min) {
            putI32(right, min);
        }
    }

    @NotNull
    public Iterator<I32ValueS32Interval> ascendingIntervalIterator() {
        final Iterator<Entry> iterator = ascendingIterator();
        final ArrayList<I32ValueS32Interval> list = new ArrayList<>();
        Interval32 interval = null;
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (interval != null) {
                if (interval.getI32() == entry.getI32Value() && interval.getRight() + 1 == entry.getI32Key()) {
                    interval.setRight(interval.getRight() + 1);
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new Interval32(entry.getI32Key(), entry.getI32Key(), entry.getI32Value());
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    @NotNull
    public Iterator<I32ValueS32Interval> descendingIntervalIterator() {
        final Iterator<Entry> iterator = descendingIterator();
        final ArrayList<I32ValueS32Interval> list = new ArrayList<>();
        Interval32 interval = null;
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (interval != null) {
                if (interval.getI32() == entry.getI32Value() && interval.getLeft() - 1 == entry.getI32Key()) {
                    interval.setLeft(interval.getLeft() - 1);
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new Interval32(entry.getI32Key(), entry.getI32Key(), entry.getI32Value());
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    public void ascendingForEachInterval(@NotNull IntValueIntervalConsumer action) {
        final Iterator<I32ValueS32Interval> iterator = ascendingIntervalIterator();
        while (iterator.hasNext()) {
            I32ValueS32Interval interval = iterator.next();
            action.accept(interval.getLeft(), interval.getRight(), interval.getI32());
        }
    }

    public void descendingForEachInterval(@NotNull IntValueIntervalConsumer action) {
        final Iterator<I32ValueS32Interval> iterator = descendingIntervalIterator();
        while (iterator.hasNext()) {
            I32ValueS32Interval interval = iterator.next();
            action.accept(interval.getLeft(), interval.getRight(), interval.getI32());
        }
    }
}
