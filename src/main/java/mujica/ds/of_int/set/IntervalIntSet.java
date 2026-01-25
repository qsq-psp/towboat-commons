package mujica.ds.of_int.set;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created on 2026/1/17.
 */
public abstract class IntervalIntSet extends NavigableIntSet {

    protected IntervalIntSet() {
        super();
    }

    @NotNull
    @Override
    public abstract IntervalIntSet duplicate();

    @Override
    public int intLength() {
        final long n = longLength();
        if (n < Integer.MAX_VALUE) {
            return (int) n;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public long longLength() {
        long n = 0L;
        for (int ignore : this) {
            n++;
        }
        return n;
    }

    public int intervalLeft(int inKey) {
        final boolean value = contains(inKey);
        int outKey = inKey;
        do {
            outKey--;
        } while (outKey < inKey && value == contains(outKey));
        return outKey;
    }

    public int intervalRight(int inKey) {
        final boolean value = contains(inKey);
        int outKey = inKey;
        do {
            outKey++;
        } while (outKey > inKey && value == contains(outKey));
        return outKey;
    }

    public void intervalAdd(int left, int right) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            add(key);
        }
        add(right);
    }

    public void intervalRemove(int left, int right) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            remove(key);
        }
        remove(right);
    }

    public void intervalFlip(int left, int right) {
        if (left > right) {
            return;
        }
        for (int key = left; key < right; key++) {
            flip(key);
        }
        flip(right);
    }

    @NotNull
    public Iterator<Interval> ascendingIntervalIterator() {
        final Iterator<Integer> iterator = ascendingIterator();
        final ArrayList<Interval> list = new ArrayList<>();
        SimpleInterval interval = null;
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (interval != null) {
                if (interval.right + 1 == key) {
                    interval.right++;
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new SimpleInterval(key, key);
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    @NotNull
    public Iterator<Interval> descendingIntervalIterator() {
        final Iterator<Integer> iterator = descendingIterator();
        final ArrayList<Interval> list = new ArrayList<>();
        SimpleInterval interval = null;
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (interval != null) {
                if (interval.left - 1 == key) {
                    interval.left++;
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new SimpleInterval(key, key);
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }
}
