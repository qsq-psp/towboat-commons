package mujica.ds.generic;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.ListIterator;

@CodeHistory(date = "2023/3/17", project = "Ultramarine")
@CodeHistory(date = "2025/3/11")
public class ReverseIterator<E> implements ListIterator<E> {

    private final ListIterator<E> iterator;

    public ReverseIterator(@NotNull ListIterator<E> iterator) {
        super();
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasPrevious();
    }

    @Override
    public E next() {
        return iterator.previous();
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasNext();
    }

    @Override
    public E previous() {
        return iterator.next();
    }

    @Override
    public int nextIndex() {
        return iterator.previousIndex();
    }

    @Override
    public int previousIndex() {
        return iterator.nextIndex();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void set(E e) {
        iterator.set(e);
    }

    @Override
    public void add(E e) {
        iterator.add(e);
    }
}
