package mujica.ds.of_int.set;

import mujica.ds.of_int.list.CopyOnResizeIntList;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Created on 2025/6/25.
 */
@CodeHistory(date = "2025/6/25")
public abstract class AbstractIntSet implements IntSet {

    @NotNull
    @Override
    public abstract AbstractIntSet duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @Override
    public int intLength() {
        int n = 0;
        for (int ignore : this) {
            n++;
        }
        return n;
    }

    @Override
    public boolean isEmpty() {
        return !iterator().hasNext();
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean contains(int t) {
        for (int v : this) {
            if (t == v) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(int t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(int t) {
        for (Iterator<Integer> it = iterator(); it.hasNext();) {
            if (t == it.next()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeIf(@NotNull IntPredicate filter) {
        for (Iterator<Integer> it = iterator(); it.hasNext();) {
            if (filter.test(it.next())) {
                it.remove();
            }
        }
    }

    @Override
    public int getArbitrary(@Nullable RandomContext rc) throws NoSuchElementException {
        final Iterator<Integer> it = iterator();
        int v;
        if (it.hasNext()) {
            v = it.next();
        } else {
            throw new NoSuchElementException();
        }
        if (rc != null) {
            int n = 1;
            while (it.hasNext()) {
                int t = it.next();
                if (rc.nextInt(++n) == 0) {
                    v = t;
                }
            }
        }
        return v;
    }

    @Override
    public int removeArbitrary(@Nullable RandomContext rc) throws NoSuchElementException {
        final Iterator<Integer> it = iterator();
        if (rc == null) {
            if (it.hasNext()) {
                int t = it.next();
                it.remove();
                return t;
            } else {
                throw new NoSuchElementException();
            }
        } else {
            int n = intLength();
            if (n == 0) {
                throw new NoSuchElementException();
            }
            n = rc.nextInt(n);
            while (n-- > 0) {
                it.next();
            }
            int t = it.next();
            it.remove();
            return t;
        }
    }

    @Override
    public void clear() {
        final Iterator<Integer> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @NotNull
    @Override
    public int[] toArray() {
        final CopyOnResizeIntList intList = new CopyOnResizeIntList(null);
        for (int v : this) {
            intList.offerLast(v);
        }
        return intList.toArray();
    }

    @Override
    public void forEach(@NotNull IntConsumer action) {
        for (int v : this) {
            action.accept(v);
        }
    }

    @NotNull
    @Override
    public abstract Iterator<Integer> iterator();

    @NotNull
    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(iterator(), intLength(), Spliterator.DISTINCT | Spliterator.SIZED);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + intLength() + ">";
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder();
        stringifyDetail(sb);
        return sb.toString();
    }

    public void stringifyDetail(@NotNull StringBuilder sb) {
        sb.append("[");
        boolean subsequent = false;
        for (int v : this) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(v);
            subsequent = true;
        }
        sb.append("]");
    }

    @Override
    public String toString() {
        return getClass().getName() + " " + summaryToString() + " " + detailToString();
    }
}
