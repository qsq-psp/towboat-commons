package mujica.ds.of_int.set;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.IntPredicate;

/**
 * Created on 2026/1/23.
 */
public class JdkIntSet extends IntSet {

    @NotNull
    final Set<Integer> set;

    public JdkIntSet(@NotNull Set<Integer> set) {
        super();
        this.set = set;
    }

    public JdkIntSet() {
        this(new HashSet<>());
    }

    @NotNull
    @Override
    public JdkIntSet duplicate() {
        return new JdkIntSet();
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
    public int hashCode() {
        return set.hashCode();
    }
}
