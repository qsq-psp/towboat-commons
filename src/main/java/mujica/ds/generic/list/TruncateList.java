package mujica.ds.generic.list;

import mujica.ds.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

@CodeHistory(date = "2022/11/22")
@CodeHistory(date = "2025/5/26")
public class TruncateList<E> extends ArrayList<E> implements DataStructure {

    private static final long serialVersionUID = 0x585405a5643fedc2L;

    @NotNull
    public static <E> TruncateList<E> of(@Nullable E obj) {
        final TruncateList<E> list = new TruncateList<>();
        list.add(obj);
        return list;
    }

    @SuppressWarnings({"ManualArrayToCollectionCopy", "UseBulkOperation", "unchecked"})
    @NotNull
    public static <E> TruncateList<E> of(E... objects) {
        final TruncateList<E> list = new TruncateList<>();
        for (E obj : objects) {
            list.add(obj);
        }
        return list;
    }

    public TruncateList() {
        super();
    }

    public TruncateList(int initialCapacity) {
        super(initialCapacity);
    }

    public TruncateList(@NotNull Collection<? extends E> collection) {
        super(collection);
    }

    @NotNull
    @Override
    public TruncateList<E> duplicate() {
        return new TruncateList<>(this);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // pass
    }

    @Override
    public void checkHealth() throws RuntimeException {
        // pass
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    public E addOrSet(int index, E element) {
        if (index == size()) {
            add(element);
            return null;
        } else {
            return set(index, element);
        }
    }

    public E padSet(int index, E element, E pad) {
        if (index < size()) {
            return set(index, element);
        }
        for (int size = size(); size < index; size++) {
            add(pad);
        }
        add(element);
        return null;
    }

    public void padHead(int minSize, E pad) {
        final int size = Math.max(0, minSize - size());
        if (size > 0) {
            if (size == 1) {
                add(0, pad);
            } else {
                addAll(0, new AbstractList<E>() {
                    @Override
                    public E get(int index) {
                        return pad;
                    }
                    @Override
                    public int size() {
                        return size;
                    }
                });
            }
        }
    }

    public void padTail(int minSize, E pad) {
        for (int size = size(); size < minSize; size++) {
            add(pad);
        }
    }

    public void trimHead() {
        final int size = size();
        for (int index = 0; index < size; index++) {
            if (get(index) != null) {
                if (index != 0) {
                    removeRange(0, index);
                }
                return;
            }
        }
        clear();
    }

    public void trimTail() {
        final int size = size();
        for (int index = size; index > 0; index--) {
            if (get(index - 1) != null) {
                if (index != size) {
                    removeRange(index, size);
                }
            }
        }
        clear();
    }

    public void trim() {
        trimHead();
        trimTail();
    }

    public E get(int index, @Nullable E fallback) {
        if (index < size()) {
            return get(index);
        } else {
            return fallback;
        }
    }

    public E getFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return get(0);
        }
    }

    public E getFirst(@Nullable E fallback) {
        if (isEmpty()) {
            return fallback;
        } else {
            return get(0);
        }
    }

    public E getLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return get(size() - 1);
        }
    }

    public E getLast(@Nullable E fallback) {
        if (isEmpty()) {
            return fallback;
        } else {
            return get(size() - 1);
        }
    }

    public E removeFirst() {
        return remove(0);
    }

    public E removeLast() {
        return remove(size() - 1);
    }

    @Override
    public void removeRange(@Index(of = "this") int startIndex, @Index(of = "this", inclusive = false) int endIndex) {
        super.removeRange(startIndex, endIndex);
    }

    public void removeNull() {
        final int size = size();
        int writerIndex = 0;
        for (int readerIndex = 0; readerIndex < size; readerIndex++) {
            E item = get(readerIndex);
            if (item != null) {
                set(writerIndex++, item);
            }
        }
        if (writerIndex < size) {
            super.removeRange(writerIndex, size);
        }
    }

    public void markTrueNull(@NotNull Predicate<E> predicate) {
        final int size = size();
        for (int index = 0; index < size; index++) {
            if (predicate.test(get(index))) {
                set(index, null);
            }
        }
    }

    public void markFalseNull(@NotNull Predicate<E> predicate) {
        final int size = size();
        for (int index = 0; index < size; index++) {
            if (!predicate.test(get(index))) {
                set(index, null);
            }
        }
    }

    public void markDuplicateSubsequentNull() {
        final HashSet<Object> set = new HashSet<>();
        final int size = size();
        for (int index = 0; index < size; index++) {
            if (!set.add(get(index))) {
                set(index, null);
            }
        }
    }

    public void markDuplicatePrecedingNull() {
        final HashSet<Object> set = new HashSet<>();
        for (int index = size() - 1; index >= 0; index--) {
            if (!set.add(get(index))) {
                set(index, null);
            }
        }
    }

    public void truncate(int newSize) {
        super.removeRange(newSize, size());
    }

    public void reverse() {
        int low = 0;
        int high = size() - 1;
        while (low < high) {
            set(low, set(high, get(low)));
            low++;
            high--;
        }
    }

    public void map(@NotNull UnaryOperator<E> operator) {
        final int size = size();
        for (int index = 0; index < size; index++) {
            set(index, operator.apply(get(index)));
        }
    }

    public void mapNonNull(@NotNull UnaryOperator<E> operator) {
        final int size = size();
        for (int index = 0; index < size; index++) {
            E item = get(index);
            if (item == null) {
                continue;
            }
            set(index, operator.apply(item));
        }
    }

    public <R> void map(@NotNull Function<E, R> function, @NotNull Collection<R> out) {
        for (E item : this) {
            out.add(function.apply(item));
        }
    }

    public <R> void mapNonNull(@NotNull Function<E, R> function, @NotNull Collection<R> out) {
        for (E item : this) {
            if (item == null) {
                continue;
            }
            out.add(function.apply(item));
        }
    }

    /**
     * @return null if there is no element
     */
    public E reduce(@NotNull BinaryOperator<E> function) {
        final int size = size();
        if (size == 0) {
            return null;
        }
        E value = get(0);
        for (int index = 1; index < size; index++) {
            value = function.apply(value, get(index));
        }
        return value;
    }

    /**
     * @return null if there is no non-null element
     */
    public E reduceNonNull(@NotNull BinaryOperator<E> function) {
        final int size = size();
        E value = null;
        int index;
        for (index = 0; index < size; index++) {
            if ((value = get(index)) != null) {
                break;
            }
        }
        for (index++; index < size; index++) {
            value = function.apply(value, get(index));
        }
        return value;
    }

    public <V> void keysToMap(@NotNull Map<E, V> map, @NotNull Function<E, V> valueFunction) {
        for (E item : this) {
            map.put(item, valueFunction.apply(item));
        }
    }

    public <K> void valuesToMap(@NotNull Map<K, E> map, @NotNull Function<E, K> keyFunction) {
        for (E item : this) {
            map.put(keyFunction.apply(item), item);
        }
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size=" + size() + ">";
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder();
        stringifyDetail(sb);
        return sb.toString();
    }

    public void stringifyDetail(@NotNull StringBuilder sb) {
        final Quote quote = Quote.DEFAULT;
        sb.append("[");
        boolean subsequent = false;
        for (E element : this) {
            if (subsequent) {
                sb.append(", ");
            }
            quote.append(element, sb);
            subsequent = true;
        }
        sb.append("]");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + summaryToString() + detailToString();
    }
}
