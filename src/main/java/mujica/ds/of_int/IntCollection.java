package mujica.ds.of_int;

import mujica.ds.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2025/4/2")
@CodeHistory(date = "2025/5/30")
public interface IntCollection extends DataStructure, Iterable<Integer> {

    int INITIAL_CAPACITY = 8; // todo: remove it after using ResizePolicy

    @NotNull
    @Override
    IntCollection duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    int intLength();

    boolean isEmpty();

    boolean isFull();

    boolean contains(int t);

    @NotNull
    int[] toArray();

    void forEach(@NotNull IntConsumer action);

    @NotNull
    @Override
    Iterator<Integer> iterator();

    @Override
    @NotNull
    Spliterator<Integer> spliterator();

    boolean equals(Object object);

    int hashCode();

    @NotNull
    @Override
    String summaryToString();

    @NotNull
    @Override
    String detailToString();
}
