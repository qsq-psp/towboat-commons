package mujica.ds.generic.heap;

import mujica.ds.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Queue;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/6/12")
public interface PriorityQueue<E> extends Queue<E>, DataStructure {

    @NotNull
    PriorityQueue<E> clone() throws CloneNotSupportedException;

    @NotNull
    @Override
    PriorityQueue<E> duplicate();

    @NotNull
    PriorityQueue<E> duplicate(@NotNull UnaryOperator<E> operator);

    @NotNull
    Comparator<E> getComparator();

    long sumOfDepth();

    void removeAndTransfer(@NotNull Collection<E> collection);

    void removeAllAndTransfer(@NotNull Collection<E> collection);
}
