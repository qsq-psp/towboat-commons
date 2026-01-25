package mujica.ds.of_long.heap;

import mujica.ds.of_long.LongCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * Created on 2026/1/14.
 */
public interface OrderedLongQueue extends LongCollection {

    @NotNull
    @Override
    OrderedLongQueue duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    boolean isDescending();

    @Override
    int longLength();

    @Override
    boolean isEmpty();

    @Override
    boolean isFull();

    void offer(long t);

    int remove() throws NoSuchElementException;

    int poll(long fallback);

    int element() throws NoSuchElementException;

    int peek(long fallback);

    void clear();

    @Override
    boolean contains(long t);

    @NotNull
    @Override
    long[] toLongArray();

    @Override
    void forEach(@NotNull LongConsumer action);

    @NotNull
    @Override
    Iterator<Long> iterator();

    @Override
    @NotNull
    Spliterator<Long> spliterator();

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
