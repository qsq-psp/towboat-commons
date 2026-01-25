package mujica.ds.of_int.map;

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

    public int intervalleft(int inKey) {
        final int value = getInt(inKey);
        int outKey = inKey;
        do {
            outKey--;
        } while (outKey < inKey && value == getInt(outKey));
        return outKey;
    }

    public int intervalright(int inKey) {
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
    public Iterator<IntValueInterval> ascendingIntervalIterator() {
        final Iterator<Entry> iterator = ascendingIterator();
        final ArrayList<IntValueInterval> list = new ArrayList<>();
        SimpleIntValueInterval interval = null;
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (interval != null) {
                if (interval.value == entry.getIntValue() && interval.right + 1 == entry.getIntKey()) {
                    interval.right++;
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new SimpleIntValueInterval(entry.getIntKey(), entry.getIntKey(), entry.getIntValue());
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    @NotNull
    public Iterator<IntValueInterval> descendingIntervalIterator() {
        final Iterator<Entry> iterator = descendingIterator();
        final ArrayList<IntValueInterval> list = new ArrayList<>();
        SimpleIntValueInterval interval = null;
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (interval != null) {
                if (interval.value == entry.getIntValue() && interval.left - 1 == entry.getIntKey()) {
                    interval.left++;
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new SimpleIntValueInterval(entry.getIntKey(), entry.getIntKey(), entry.getIntValue());
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    public void ascendingForEachInterval(@NotNull IntValueIntervalConsumer action) {
        final Iterator<IntValueInterval> iterator = ascendingIntervalIterator();
        while (iterator.hasNext()) {
            IntValueInterval interval = iterator.next();
            action.accept(interval.getLeft(), interval.getRight(), interval.getInt());
        }
    }

    public void descendingForEachInterval(@NotNull IntValueIntervalConsumer action) {
        final Iterator<IntValueInterval> iterator = descendingIntervalIterator();
        while (iterator.hasNext()) {
            IntValueInterval interval = iterator.next();
            action.accept(interval.getLeft(), interval.getRight(), interval.getInt());
        }
    }
}
