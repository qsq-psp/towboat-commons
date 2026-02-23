package mujica.ds.of_int.set;

import mujica.ds.InvariantException;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntCollection;
import mujica.algebra.random.RandomContext;
import mujica.ds.of_int.list.CopyOnResizeIntList;
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

@CodeHistory(date = "2025/6/25")
public abstract class IntSet implements IntCollection {

    protected IntSet() {
        super();
    }

    @NotNull
    @Override
    public abstract IntSet duplicate();

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final CopyOnResizeIntList intList = new CopyOnResizeIntList(null);
        for (int v : this) {
            intList.offerLast(v);
        }
        intList.sort(MonotonicityDirection.ASCENDING);
        final int n = intList.intLength();
        int v0 = intList.getInt(0);
        for (int i = 1; i < n; i++) {
            int v1 = intList.getInt(i);
            if (v0 >= v1) {
                consumer.accept(new InvariantException(v0 + " >= " + v1));
            }
            v0 = v1;
        }
    }

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

    public boolean add(int t) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(int t) {
        for (Iterator<Integer> it = iterator(); it.hasNext();) {
            if (t == it.next()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public void flip(int t) {
        if (contains(t)) {
            remove(t);
        } else {
            add(t);
        }
    }

    public void removeIf(@NotNull IntPredicate filter) {
        for (Iterator<Integer> it = iterator(); it.hasNext();) {
            if (filter.test(it.next())) {
                it.remove();
            }
        }
    }

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

    public void clear() {
        final Iterator<Integer> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @NotNull
    @Override
    public int[] toIntArray() {
        final CopyOnResizeIntList intList = new CopyOnResizeIntList(null);
        for (int v : this) {
            intList.offerLast(v);
        }
        return intList.toIntArray();
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
