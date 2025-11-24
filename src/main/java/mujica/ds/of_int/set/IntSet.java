package mujica.ds.of_int.set;

import mujica.ds.of_int.IntCollection;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Created on 2025/6/25.
 */
@CodeHistory(date = "2025/6/25")
public interface IntSet extends IntCollection {

    @NotNull
    @Override
    IntSet duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @Override
    int intLength();

    @Override
    boolean isEmpty();

    @Override
    boolean isFull();

    @Override
    boolean contains(int t);

    boolean add(int t);

    boolean remove(int t);

    void removeIf(@NotNull IntPredicate filter);

    int getArbitrary(@Nullable RandomContext rc) throws NoSuchElementException;

    int removeArbitrary(@Nullable RandomContext rc) throws NoSuchElementException;

    void clear();

    @NotNull
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
