package mujica.ds.of_int.set;

import mujica.ds.of_int.IntSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.function.IntPredicate;

/**
 * Created on 2026/1/18.
 */
public class JdkNavigableIntSet extends NavigableIntSet {

    @NotNull
    final NavigableSet<Integer> set;

    public JdkNavigableIntSet(@NotNull NavigableSet<Integer> set) {
        super();
        this.set = set;
    }

    public JdkNavigableIntSet() {
        this(new TreeSet<>());
    }

    @NotNull
    @Override
    public JdkNavigableIntSet duplicate() {
        return new JdkNavigableIntSet(); // todo
    }

    @Override
    public int intLength() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(int t) {
        return set.contains(t);
    }

    @Override
    public boolean add(int t) {
        return set.add(t);
    }

    @Override
    public boolean remove(int t) {
        return set.remove(t);
    }

    @Override
    public void removeIf(@NotNull IntPredicate filter) {
        set.removeIf(filter::test);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @NotNull
    @Override
    public int[] toIntArray() {
        final int n = set.size();
        final int[] array = new int[n];
        int i = 0;
        for (int t : this) {
            array[i++] = t;
        }
        assert i == n;
        return array;
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return set.iterator();
    }

    @Override
    public boolean lower(@NotNull IntSlot keySlot) {
        final Integer key = set.lower(keySlot.getInt());
        if (key == null) {
            return false;
        } else {
            keySlot.setInt(key);
            return true;
        }
    }

    @Override
    public boolean floor(@NotNull IntSlot keySlot) {
        final Integer key = set.floor(keySlot.getInt());
        if (key == null) {
            return false;
        } else {
            keySlot.setInt(key);
            return true;
        }
    }

    @Override
    public boolean higher(@NotNull IntSlot keySlot) {
        final Integer key = set.higher(keySlot.getInt());
        if (key == null) {
            return false;
        } else {
            keySlot.setInt(key);
            return true;
        }
    }

    @Override
    public boolean ceiling(@NotNull IntSlot keySlot) {
        final Integer key = set.ceiling(keySlot.getInt());
        if (key == null) {
            return false;
        } else {
            keySlot.setInt(key);
            return true;
        }
    }

    @Override
    public boolean first(@NotNull IntSlot keySlot) {
        final Integer t = set.first();
        if (t == null) {
            return false;
        } else {
            keySlot.setInt(t);
            return true;
        }
    }

    @Override
    public boolean last(@NotNull IntSlot keySlot) {
        final Integer t = set.last();
        if (t == null) {
            return false;
        } else {
            keySlot.setInt(t);
            return true;
        }
    }

    @NotNull
    @Override
    public Iterator<Integer> ascendingIterator() {
        return set.iterator();
    }

    @NotNull
    @Override
    public Iterator<Integer> descendingIterator() {
        return set.descendingIterator();
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }
}
