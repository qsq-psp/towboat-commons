package mujica.ds.of_int.list;

import mujica.ds.of_int.IntCollection;
import mujica.reflect.function.IntEntryConsumer;
import mujica.reflect.function.IntEntryPredicate;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

@CodeHistory(date = "2020/5/18", project = "coo", name = "IntegerArrays")
@CodeHistory(date = "2022/11/10", project = "Ultramarine", name = "IntegerArrays")
@CodeHistory(date = "2024/1/17", project = "Ultramarine", name = "IntArray")
@CodeHistory(date = "2025/3/12", name = "IntArray")
@CodeHistory(date = "2025/5/30")
public interface IntList extends IntCollection, IntSequence {

    @Override
    @NotNull
    IntList duplicate();

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

    @Override
    void forEach(@NotNull IntConsumer action);

    void forEach(@NotNull IntEntryConsumer action);

    @Override
    @NotNull
    Iterator<Integer> iterator();

    @Override
    @NotNull
    Spliterator<Integer> spliterator();

    @NotNull
    ListIterator<Integer> listIterator(int i);

    @NotNull
    int[] toArray();

    void getAll(@NotNull int[] dst, int dstOffset);

    void getRange(int srcOffset, @NotNull int[] dst, int dstOffset, int length);

    @Override
    int getInt(int i);

    int getFirst();

    int getFirst(int k);

    int getLast();

    int getLast(int k);

    boolean offerAt(int i, int t);

    boolean offerFirst(int t);

    boolean offerLast(int t);

    int removeAt(int i);

    int removeFirst();

    int removeLast();

    void removeRange(@Index(of = "this") int fi, @Index(of = "this", inclusive = false) int ti);

    void removeIf(@NotNull IntPredicate filter);

    void removeIf(@NotNull IntEntryPredicate filter);

    int setInt(int i, int t);

    void swap(int i, int j);

    int firstIndexOf(int t);

    int firstIndexOf(int i, int t);

    int lastIndexOf(int t);

    int lastIndexOf(int i, int t);

    void clear();

    void reverse();

    void rotate(int d);

    void sort(boolean descending);

    int min();

    int max();

    int sum();

    int xor();

    void bitwiseNot();

    void bitwiseAnd(int t);

    void bitwiseOr(int t);

    void bitwiseXor(int t);

    boolean equals(@NotNull IntList that);

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
