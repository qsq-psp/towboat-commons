package mujica.ds.of_int.heap;

import mujica.ds.of_int.IntCollection;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2025/7/7")
public interface OrderedIntQueue extends IntCollection {

    @NotNull
    @Override
    OrderedIntQueue duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    boolean isDescending();

    @Override
    int intLength();

    @Override
    boolean isEmpty();

    @Override
    boolean isFull();

    void offer(int t);

    int remove() throws NoSuchElementException;

    int poll(int fallback);

    int element() throws NoSuchElementException;

    int peek(int fallback);

    void clear();

    @Override
    boolean contains(int t);

    @NotNull
    @Override
    int[] toArray();

    @Override
    void forEach(@NotNull IntConsumer action);

    @NotNull
    @Override
    Iterator<Integer> iterator();

    @Override
    @NotNull
    Spliterator<Integer> spliterator();

    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

    @NotNull
    @Override
    String summaryToString();

    @NotNull
    @Override
    String detailToString();
}
