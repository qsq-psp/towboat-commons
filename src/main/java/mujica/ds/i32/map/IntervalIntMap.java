package mujica.ds.i32.map;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created on 2026/1/12.
 */
public abstract class IntervalIntMap extends NavigableIntMap {

    protected IntervalIntMap() {
        super();
    }

    @NotNull
    @Override
    public abstract IntervalIntMap duplicate();

    public int intervalLeft(int inKey) {
        final int value = getInt(inKey);
        int outKey = inKey;
        do {
            outKey--;
        } while (outKey < inKey && value == getInt(outKey));
        return outKey;
    }

    public int intervalRight(int inKey) {
        final int value = getInt(inKey);
        int outKey = inKey;
        do {
            outKey++;
        } while (outKey > inKey && value == getInt(outKey));
        return outKey;
    }

    public void intervalPutInt(int left, int right, int value) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            putInt(key, value);
        }
        putInt(right, value);
    }

    public void intervalAddInt(int left, int right, int delta) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            addInt(key, delta);
        }
        addInt(right, delta);
    }

    public void intervalLimitIntMax(int left, int right, int max) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            if (getInt(key) > max) {
                putInt(key, max);
            }
        }
        if (getInt(right) > max) {
            putInt(right, max);
        }
    }

    public void intervalLimitIntMin(int left, int right, int min) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            if (getInt(key) < min) {
                putInt(key, min);
            }
        }
        if (getInt(right) < min) {
            putInt(right, min);
        }
    }

    @NotNull
    public Iterator<IntValueIntInterval> ascendingIntervalIterator() {
        final Iterator<Entry> iterator = ascendingIterator();
        final ArrayList<IntValueIntInterval> list = new ArrayList<>();
        PrivateIntValueInterval interval = null;
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (interval != null) {
                if (interval.getI32() == entry.getIntValue() && interval.getRight() + 1 == entry.getIntKey()) {
                    interval.setRight(interval.getRight() + 1);
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new PrivateIntValueInterval(entry.getIntKey(), entry.getIntKey(), entry.getIntValue());
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    @NotNull
    public Iterator<IntValueIntInterval> descendingIntervalIterator() {
        final Iterator<Entry> iterator = descendingIterator();
        final ArrayList<IntValueIntInterval> list = new ArrayList<>();
        PrivateIntValueInterval interval = null;
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (interval != null) {
                if (interval.getI32() == entry.getIntValue() && interval.getLeft() - 1 == entry.getIntKey()) {
                    interval.setLeft(interval.getLeft() - 1);
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new PrivateIntValueInterval(entry.getIntKey(), entry.getIntKey(), entry.getIntValue());
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    public void ascendingForEachInterval(@NotNull IntValueIntervalConsumer action) {
        final Iterator<IntValueIntInterval> iterator = ascendingIntervalIterator();
        while (iterator.hasNext()) {
            IntValueIntInterval interval = iterator.next();
            action.accept(interval.getLeft(), interval.getRight(), interval.getI32());
        }
    }

    public void descendingForEachInterval(@NotNull IntValueIntervalConsumer action) {
        final Iterator<IntValueIntInterval> iterator = descendingIntervalIterator();
        while (iterator.hasNext()) {
            IntValueIntInterval interval = iterator.next();
            action.accept(interval.getLeft(), interval.getRight(), interval.getI32());
        }
    }
}
