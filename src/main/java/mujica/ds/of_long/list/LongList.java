package mujica.ds.of_long.list;

import mujica.ds.of_long.LongCollection;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

/**
 * Created on 2026/5/10.
 */
public interface LongList extends LongCollection, LongSequence {

    @NotNull
    @Override
    LongList duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @Override
    int longLength();

    @Override
    boolean isEmpty();

    @Override
    boolean isFull();

    @Override
    boolean contains(long t);

    @Override
    void forEach(@NotNull LongConsumer action);

    @NotNull
    @Override
    Iterator<Long> iterator();

    @NotNull
    @Override
    long[] toLongArray();

    void getAll(@NotNull long[] dst, int dstOffset);

    void getRange(int srcOffset, @NotNull long[] dst, int dstOffset, int length);

    @Override
    long getLong(int index);

    long getFirst();

    long getFirst(int k);

    long getLast();

    long getLast(int k);

    boolean offerAt(int i, long t);

    boolean offerFirst(long t);

    boolean offerLast(long t);

    long removeAt(int i);

    long removeFirst();

    long removeLast();

    void removeRange(@Index(of = "this") int startIndex, @Index(of = "this", inclusive = false) int endIndex);

    void removeIf(@NotNull LongPredicate filter);

    long setLong(int i, long t);

    void swap(int i, int j);

    int firstIndexOf(long t);

    int firstIndexOf(int i, long t);

    int lastIndexOf(long t);

    int lastIndexOf(int i, long t);

    void clear();

    boolean equals(@NotNull LongList that);

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
