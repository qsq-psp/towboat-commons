package mujica.ds.of_long;

import mujica.ds.DataStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * Created on 2026/1/14.
 */
public interface LongCollection extends DataStructure, Iterable<Long> {

    @Override
    @NotNull
    LongCollection duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    int longLength();

    boolean isEmpty();

    boolean isFull();

    boolean contains(long t);

    @NotNull
    long[] toLongArray();

    void forEach(@NotNull LongConsumer action);

    @NotNull
    @Override
    Iterator<Long> iterator();

    @Override
    @NotNull
    Spliterator<Long> spliterator();

    boolean equals(Object object);

    int hashCode();

    @NotNull
    @Override
    String summaryToString();

    @NotNull
    @Override
    String detailToString();
}
