package mujica.ds.of_int.heap;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2025/7/7")
public abstract class AbstractOrderedIntQueue implements OrderedIntQueue {

    private static final long serialVersionUID = 0xc80f5979dec544ddL;

    int endIndex;

    transient int modCount;

    protected AbstractOrderedIntQueue() {
        super();
    }

    @NotNull
    @Override
    public abstract AbstractOrderedIntQueue duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @Override
    public boolean isDescending() {
        return false;
    }

    @Override
    public int intLength() {
        return endIndex;
    }

    @Override
    public boolean isEmpty() {
        return endIndex == 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public abstract void offer(int t);

    @Override
    public abstract int remove() throws NoSuchElementException;

    @Override
    public abstract int poll(int fallback);

    @Override
    public abstract int element() throws NoSuchElementException;

    @Override
    public abstract int peek(int fallback);

    @Override
    public abstract void clear();

    @Override
    public boolean contains(int t) {
        for (int v : this) {
            if (t == v) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public int[] toArray() {
        final int[] array = new int[endIndex];
        int i = 0;
        for (int v : this) {
            array[i++] = v;
        }
        assert i == array.length;
        return array;
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
        return Spliterators.spliterator(iterator(), endIndex, Spliterator.SIZED);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof OrderedIntQueue)) {
            return false;
        }
        final OrderedIntQueue that = (OrderedIntQueue) object;
        if (this.intLength() != that.intLength() || this.isDescending() != that.isDescending()) {
            return false;
        }
        final int[] thisArray = this.toArray();
        final int[] thatArray = that.toArray();
        Arrays.sort(thisArray);
        Arrays.sort(thatArray);
        return Arrays.equals(thisArray, thatArray);
    }

    @Override
    public int hashCode() {
        final int[] array = toArray();
        Arrays.sort(array);
        return Arrays.hashCode(array);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + endIndex + ">";
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
        return getClass().getSimpleName() + " " + summaryToString() + " " + detailToString();
    }
}
