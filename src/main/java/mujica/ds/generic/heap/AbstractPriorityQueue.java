package mujica.ds.generic.heap;

import mujica.ds.generic.ComparableComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/5/23", project = "Ultramarine", name = "PriorityHeap")
@CodeHistory(date = "2025/6/2")
@Name(value = "优先队列", language = "zh")
public abstract class AbstractPriorityQueue<E> extends AbstractCollection<E> implements PriorityQueue<E> {

    private static final long serialVersionUID = 0x579b13c090bb61a7L;

    @NotNull
    final Comparator<E> comparator;

    transient int modCount;

    protected AbstractPriorityQueue(@Nullable Comparator<E> comparator) {
        super();
        if (comparator == null) {
            comparator = new ComparableComparator<>();
        }
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public AbstractPriorityQueue<E> clone() throws CloneNotSupportedException {
        return (AbstractPriorityQueue<E>) super.clone();
    }

    @NotNull
    @Override
    public AbstractPriorityQueue<E> duplicate() {
        return duplicate(UnaryOperator.identity());
    }

    @NotNull
    public abstract AbstractPriorityQueue<E> duplicate(@NotNull UnaryOperator<E> operator);

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @Override
    @NotNull
    public Comparator<E> getComparator() {
        return comparator;
    }

    @Override
    public abstract int size();

    public abstract long sumOfDepth();

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean remove(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public abstract boolean offer(E element);

    @Override
    public abstract E remove();

    @Override
    public abstract E poll();

    @Override
    public abstract E element();

    @Override
    public abstract E peek();

    public void removeAndTransfer(@NotNull Collection<E> collection) {
        collection.add(remove());
    }

    public void removeAllAndTransfer(@NotNull Collection<E> collection) {
        collection.addAll(this);
        clear();
    }

    @Override
    @NotNull
    public abstract Iterator<E> iterator(); // forEach and removeIf already defined

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + size() + ">";
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
        for (E element : this) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(element);
            subsequent = true;
        }
        sb.append("]");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + summaryToString() + " " + detailToString();
    }
}
