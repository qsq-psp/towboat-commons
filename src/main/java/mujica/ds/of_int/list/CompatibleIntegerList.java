package mujica.ds.of_int.list;

import mujica.ds.of_boolean.PublicBooleanSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@CodeHistory(date = "2025/6/1")
public class CompatibleIntegerList implements List<Integer> {

    @NotNull
    private final IntList intList;

    public CompatibleIntegerList(@NotNull IntList intList) {
        super();
        this.intList = intList;
    }

    @Override
    public int size() {
        return intList.intLength();
    }

    @Override
    public boolean isEmpty() {
        return intList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Integer && intList.contains((Integer) o);
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return intList.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        final int n = intList.intLength();
        final Object[] array = new Object[n];
        for (int i = 0; i < n; i++) {
            array[i] = intList.getInt(i);
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] array) {
        final int n = intList.intLength();
        if (array.length < n) {
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), n);
        }
        for (int i = 0; i < n; i++) {
            array[i] = (T) (Integer) intList.getInt(i);
        }
        return array;
    }

    @Override
    public boolean add(Integer t) {
        return intList.offerLast(t);
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Integer)) {
            return false;
        }
        final int i = intList.firstIndexOf((Integer) o);
        if (i == -1) {
            return false;
        }
        intList.removeAt(i);
        return true;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            if (o instanceof Integer && intList.firstIndexOf((Integer) o) != -1) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Integer> c) {
        return addAll(0, c);
    }

    @Override
    public boolean addAll(int i, @NotNull Collection<? extends Integer> c) {
        boolean modified = false;
        for (Object o : c) {
            if (o instanceof Integer && intList.offerAt(i, (Integer) o)) {
                i++;
                modified = true;
            } else {
                break;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        final PublicBooleanSlot result = new PublicBooleanSlot();
        intList.removeIf(t -> {
            if (c.contains(t)) {
                result.value = true;
                return true;
            } else {
                return false;
            }
        });
        return result.value;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        final PublicBooleanSlot result = new PublicBooleanSlot();
        intList.removeIf(t -> {
            if (c.contains(t)) {
                return false;
            } else {
                result.value = true;
                return true;
            }
        });
        return result.value;
    }

    @Override
    public void clear() {
        intList.clear();
    }

    @Override
    public Integer get(int i) {
        return intList.getInt(i);
    }

    @Override
    public Integer set(int i, Integer t) {
        return intList.setInt(i, t);
    }

    @Override
    public void add(int i, Integer t) {
        intList.offerAt(i, t);
    }

    @Override
    public Integer remove(int i) {
        return intList.removeAt(i);
    }

    @Override
    public int indexOf(Object o) {
        return o instanceof Integer ? intList.firstIndexOf((Integer) o) : -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return o instanceof Integer ? intList.lastIndexOf((Integer) o) : -1;
    }

    @NotNull
    @Override
    public ListIterator<Integer> listIterator() {
        return intList.listIterator(0);
    }

    @NotNull
    @Override
    public ListIterator<Integer> listIterator(int i) {
        return intList.listIterator(i);
    }

    @NotNull
    @Override
    public List<Integer> subList(int si, int di) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return intList.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof CompatibleIntegerList && this.intList.equals(((CompatibleIntegerList) o).intList);
    }
}
