package mujica.ds.generic.heap;

import mujica.ds.generic.list.TruncateList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

@CodeHistory(date = "2025/5/23", project = "Ultramarine", name = "ListPriorityHeap")
@CodeHistory(date = "2025/6/8")
public abstract class ListPriorityQueue<E> extends AbstractPriorityQueue<E> {

    @NotNull
    protected final TruncateList<E> list;

    protected ListPriorityQueue(@Nullable Comparator<E> comparator, @NotNull TruncateList<E> list) {
        super(comparator);
        this.list = list;
    }

    public ListPriorityQueue(@Nullable Comparator<E> comparator, int initialCapacity) {
        this(comparator, new TruncateList<>(initialCapacity));
    }

    public ListPriorityQueue(@Nullable Comparator<E> comparator) {
        this(comparator, new TruncateList<>());
    }

    @NotNull
    @Override
    public ListPriorityQueue<E> clone() {
        return (ListPriorityQueue<E>) duplicate();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public abstract boolean offer(E element); // size and modCount update inside

    abstract void removeRoot(); // size and modCount update inside

    abstract void buildHeap();

    @Override
    public E remove() {
        final E element = list.getFirst();
        removeRoot();
        return element;
    }

    @Override
    public E poll() {
        if (list.isEmpty()) {
            return null;
        }
        final E element = list.getFirst();
        removeRoot();
        return element;
    }

    @Override
    public E element() {
        return list.getFirst();
    }

    @Override
    public E peek() {
        return list.getFirst(null);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }
        if (collection.size() >= list.size()) {
            list.addAll(collection);
            buildHeap();
        } else {
            for (E element : collection) {
                offer(element);
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (list.removeAll(collection)) {
            buildHeap();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        if (list.removeIf(filter)) {
            buildHeap();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (list.retainAll(collection)) {
            buildHeap();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        list.clear();
    }

    class DefaultIterator implements Iterator<E> {

        int index;

        int lastRemove; // if remove() is not used, there is no extra processing

        int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public E next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            try {
                E element = list.get(index);
                index++; // if index out of bounds index remain unchanged
                return element;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (index == lastRemove) {
                throw new IllegalStateException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            lastRemove = --index;
            ListPriorityQueue.this.remove(lastRemove);
            expectedModCount = modCount;
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new DefaultIterator();
    }

    @Override
    public boolean contains(Object object) {
        return list.contains(object);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public void stringifyDetail(@NotNull StringBuilder sb) {
        list.stringifyDetail(sb);
    }
}
