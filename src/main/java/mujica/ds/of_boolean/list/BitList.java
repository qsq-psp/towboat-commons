package mujica.ds.of_boolean.list;

import mujica.ds.DataStructure;
import mujica.reflect.function.BooleanConsumer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;

@CodeHistory(date = "2026/2/7")
public interface BitList extends DataStructure, BitSequence, Iterable<Boolean> {

    @Override
    @NotNull
    BitList duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @Override
    int bitLength();

    boolean isEmpty();

    boolean isFull();

    boolean contains(boolean t);

    void forEach(@NotNull BooleanConsumer action);

    @Override
    @NotNull
    Iterator<Boolean> iterator();

    @Override
    @NotNull
    Spliterator<Boolean> spliterator();

    @NotNull
    ListIterator<Integer> listIterator(int i);

    @NotNull
    boolean[] toBooleanArray();

    void getAll(@NotNull boolean[] dst, int dstOffset);

    void getRange(int srcOffset, @NotNull boolean[] dst, int dstOffset, int length);

    @Override
    boolean getBit(int index);

    boolean getFirst();

    boolean getFirst(boolean k);

    boolean getLast();

    boolean getLast(boolean k);

    boolean offerAt(int i, boolean t);

    boolean offerFirst(boolean t);

    boolean offerLast(boolean t);

    boolean removeAt(int i);

    boolean removeFirst();

    boolean removeLast();

    void removeRange(@Index(of = "this") int startIndex, @Index(of = "this", inclusive = false) int endIndex);

    boolean setBoolean(int i, boolean t);

    boolean flipBoolean(int i);

    void swap(int i, int j);

    void clear();

    void reverse();

    void rotate(int d);

    int count(boolean t);

    boolean equals(@NotNull BitList that);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @NotNull
    @Override
    String summaryToString();

    @NotNull
    @Override
    String detailToString();
}
