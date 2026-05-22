package mujica.ds.of_int.set;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * Created on 2026/1/17.
 */
@CodeHistory(date = "2026/1/17")
@DirectSubclass({IntInterval.class, NavigableIntMapAsIntervalIntSet.class, SegmentTreeIntervalIntSet.class})
public abstract class IntervalIntSet extends NavigableIntSet {

    private static final long serialVersionUID = 0xDF952E9F0A10BC52L;

    protected IntervalIntSet() {
        super();
    }

    @NotNull
    @Override
    public abstract IntervalIntSet duplicate();

    @Override
    public int intLength() {
        final long n = intLengthAsLong();
        if (n < Integer.MAX_VALUE) {
            return (int) n;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public long intLengthAsLong() {
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
    public Iterator<IntInterval> ascendingIntervalIterator() {
        final Iterator<Integer> iterator = ascendingIterator();
        final ArrayList<IntInterval> list = new ArrayList<>();
        PrivateIntInterval interval = null;
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (interval != null) {
                if (interval.getRight() + 1 == key) {
                    interval.setRight(interval.getRight() + 1);
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new PrivateIntInterval(key, key);
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    @NotNull
    public Iterator<IntInterval> descendingIntervalIterator() {
        final Iterator<Integer> iterator = descendingIterator();
        final ArrayList<IntInterval> list = new ArrayList<>();
        PrivateIntInterval interval = null;
        while (iterator.hasNext()) {
            int key = iterator.next();
            if (interval != null) {
                if (interval.getLeft() - 1 == key) {
                    interval.setLeft(interval.getLeft() - 1);
                    continue;
                } else {
                    list.add(interval);
                }
            }
            interval = new PrivateIntInterval(key, key);
        }
        if (interval != null) {
            list.add(interval);
        }
        return list.iterator();
    }

    @NotNull
    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(iterator(), intLengthAsLong(), Spliterator.DISTINCT | Spliterator.SIZED);
    }
}
