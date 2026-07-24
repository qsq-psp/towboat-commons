package mujica.ds.slot;

import mujica.algebra.random.RandomContext;
import mujica.ds.base.DataStructure;
import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

@CodeHistory(date = "2026/7/6")
@DirectSubclass({RandomAccessBasedSlotCollection.class, GapIteratorBasedSlotCollection.class})
public interface SlotCollection<S, A> extends DataStructure {

    @Override
    @NotNull
    SlotCollection<S, A> duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    int size();

    boolean isEmpty();

    boolean isFull();

    boolean contains(@NotNull S in); // bring your own buffer design

    @Name(value = "indexOf", language = "en")
    int firstIndexOf(@NotNull S in);

    int lastIndexOf(@NotNull S in);

    @Name(value = "get", language = "en")
    void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException;

    @Name(value = "set", language = "en")
    void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException;

    @Name(value = "swap", language = "en")
    void exchangeItemAt(int index, @NotNull S slot) throws IndexOutOfBoundsException;

    @Name(value = "remove", language = "en")
    void removeItemAt(int index, @Nullable S slot) throws IndexOutOfBoundsException;

    @Name(value = "add", language = "en")
    @Name(value = "insert", language = "en")
    void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException;

    void getFirstItem(@NotNull S out) throws NoSuchElementException;

    @Name(value = "peek", language = "en")
    void getLastItem(@NotNull S out) throws NoSuchElementException;

    void getArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException;

    @Name(value = "shift", language = "en")
    void removeFirstItem(@Nullable S out) throws NoSuchElementException;

    @Name(value = "pop", language = "en")
    void removeLastItem(@Nullable S out) throws NoSuchElementException;

    void removeArbitraryItem(@NotNull S out, @Nullable RandomContext rc) throws NoSuchElementException;

    @Name(value = "addFirst", language = "en")
    @Name(value = "unshift", language = "en")
    void insertFirstItem(@NotNull S in);

    @Name(value = "add", language = "en")
    @Name(value = "addLast", language = "en")
    @Name(value = "push", language = "en")
    @Name(value = "enqueue", language = "en")
    void insertLastItem(@NotNull S in);

    void insertLast(@NotNull A thatArray);

    void insertLast(@NotNull A thatArray, @Index(of = "thatArray") int startIndex, @Index(of = "thatArray", inclusive = false) int endIndex);

    void insertLastSelf(@Index(of = "this") int startIndex, @Index(of = "this", inclusive = false) int endIndex);

    void appendTo(@NotNull SlotCollection<S, A> that);

    void removeRange(@Index(of = "this") int startIndex, @Index(of = "this", inclusive = false) int endIndex) throws IndexOutOfBoundsException;

    void truncate(int newEndIndex) throws IndexOutOfBoundsException;

    @Name(value = "removeAll", language = "en")
    void clear();

    void reverse();

    void rotate(int distance);

    void sort(boolean descending);

    long sort(@NotNull Sort<A> sort);

    long sortPart(@NotNull Sort<A> sort, int newEndIndex);

    long sortUnique(@NotNull Sort<A> sort);

    void getRange(@Index(of = "this") int srcIndex, @NotNull A dst, @Index(of = "dst") int dstIndex, int length);

    @NotNull
    A rangeToArray(@Index(of = "this") int startIndex, @Index(of = "this", inclusive = false) int endIndex);

    void getAll(@NotNull A dst, @Index(of = "dst") int dstIndex);

    @NotNull
    @Name(value = "toArray", language = "en")
    A allToArray();

    boolean internItem(@NotNull S slot);

    boolean tryGetFirstItem(@NotNull S out);

    boolean tryGetLastItem(@NotNull S out);

    boolean tryRemoveFirstItem(@Nullable S out);

    boolean tryRemoveLastItem(@Nullable S out);

    boolean tryGetArbitraryItem(@NotNull S out, @Nullable RandomContext rc);

    boolean tryRemoveArbitraryItem(@NotNull S out, @Nullable RandomContext rc);

    boolean tryInternItem(@NotNull S slot);

    <T extends S> void forEach(@NotNull Consumer<T> consumer, @NotNull T tempSlot);

    <T extends S> boolean some(@NotNull Predicate<T> predicate, @NotNull T tempSlot);

    <T extends S> boolean every(@NotNull Predicate<T> predicate, @NotNull T tempSlot);

    <T extends S> void map(@NotNull Consumer<T> consumer, @NotNull T tempSlot);

    <T extends S> void removeIf(@NotNull Predicate<T> filter, @NotNull T tempSlot);

    @CodeHistory(date = "2026/7/8")
    interface GapIterator<S> {

        boolean previousGap() throws ConcurrentModificationException;

        boolean nextGap() throws ConcurrentModificationException;

        void getPrevious(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException;

        void getNext(@NotNull S out) throws NoSuchElementException, ConcurrentModificationException;

        void setPrevious(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException;

        void setNext(@NotNull S in) throws NoSuchElementException, ConcurrentModificationException;

        void removePrevious(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException;

        void removeNext(@Nullable S out) throws NoSuchElementException, ConcurrentModificationException;

        void insertPrevious(@NotNull S in) throws ConcurrentModificationException;

        void insertNext(@NotNull S in) throws ConcurrentModificationException;
    }

    @NotNull
    GapIterator<S> iterator(@Index(canBeNegative = true) int startGapIndex) throws IndexOutOfBoundsException;

    @NotNull
    @Override
    String summaryToString();

    @NotNull
    @Override
    String detailToString();
}
