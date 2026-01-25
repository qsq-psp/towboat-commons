package mujica.ds.of_int.set;

import mujica.ds.generic.set.CollectionConstant;
import mujica.ds.of_int.list.QuadraticProbingList;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

@CodeHistory(date = "2025/7/3")
public class QuadraticClosedHashIntSet extends AbstractHashIntSet {

    private static final long serialVersionUID = 0x456bae8f76784505L;

    @NotNull
    int[] array;

    static final int EMPTY_MARK = 0;

    boolean containsEmptyMark;

    static final int REMOVED_MARK = 1;

    boolean containsRemovedMark;

    @NotNull
    transient QuadraticProbingList probingList;

    public QuadraticClosedHashIntSet(@Nullable ResizePolicy policy) {
        super(ResizePolicy.checkQuadraticFull(policy));
        array = new int[this.policy.initialCapacity()];
        probingList = new QuadraticProbingList(array.length);
    }

    QuadraticClosedHashIntSet(@NotNull ResizePolicy policy, @NotNull int[] array) {
        super(policy);
        this.array = array;
        this.probingList = new QuadraticProbingList(array.length);
    }

    @NotNull
    @Override
    public QuadraticClosedHashIntSet duplicate() {
        final QuadraticClosedHashIntSet that = new QuadraticClosedHashIntSet(policy, array.clone());
        that.containsEmptyMark = this.containsEmptyMark;
        that.containsRemovedMark = this.containsRemovedMark;
        that.size = this.size;
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        //
    }

    public int emptySlotCount() {
        int c = 0;
        for (int v : array) {
            if (v == EMPTY_MARK) {
                c++;
            }
        }
        return c;
    }

    public int removedSlotCount() {
        int c = 0;
        for (int v : array) {
            if (v == REMOVED_MARK) {
                c++;
            }
        }
        return c;
    }

    /*
    @Override
    public boolean isEmpty() {
        if (containsEmptyMark || containsRemovedMark) {
            return false;
        }
        for (int v : array) {
            if (v == EMPTY_MARK || v == REMOVED_MARK) {
                continue;
            }
            return false;
        }
        return true;
    }
     */

    @Override
    public boolean isFull() {
        final int capacity = array.length;
        return size == capacity && policy.nextCapacity(capacity) == capacity;
    }

    @Override
    public boolean contains(int t) {
        if (t == EMPTY_MARK) {
            return containsEmptyMark;
        }
        if (t == REMOVED_MARK) {
            return containsRemovedMark;
        }
        for (int i : probingList.setBase(t)) {
            int v = array[i];
            if (v == EMPTY_MARK) {
                return false;
            }
            if (t == v) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if success
     */
    private boolean rehash() {
        final int m0 = array.length;
        final int m1 = policy.nextCapacity(m0);
        if (m1 <= m0) {
            return false;
        }
        probingList.setModulo(m1);
        final int[] newArray = new int[m1];
        OA:
        for (int t : array) {
            if (t == EMPTY_MARK || t == REMOVED_MARK) {
                continue;
            }
            for (int i : probingList.setBase(t)) {
                int v = newArray[i];
                if (v == EMPTY_MARK) { // only empty, no removed
                    newArray[i] = t;
                    continue OA;
                }
            }
            probingList.setModulo(m0);
            return false;
        }
        array = newArray;
        modCount++;
        return true;
    }

    @Override
    public boolean add(int t) {
        if (t == EMPTY_MARK) {
            if (containsEmptyMark) {
                return false;
            } else {
                containsEmptyMark = true;
                size++;
                modCount++;
                return true;
            }
        }
        if (t == REMOVED_MARK) {
            if (containsRemovedMark) {
                return false;
            } else {
                containsRemovedMark = true;
                size++;
                modCount++;
                return true;
            }
        }
        do {
            int m = array.length;
            if (policy.testLoadedSize(size + 1, m)) {
                continue;
            }
            int j = -1;
            int k = 0;
            for (int i : probingList.setBase(t)) {
                int v = array[i];
                if (v == EMPTY_MARK || v == REMOVED_MARK) {
                    if (j == -1) {
                        j = i;
                        if (policy.testLinkLength(k, m)) {
                            break;
                        }
                    }
                    if (v == EMPTY_MARK) {
                        array[i] = t;
                        size++;
                        modCount++;
                        return true;
                    }
                }
                if (t == v) {
                    return false;
                }
                k++;
            }
        } while (rehash());
        throw new RuntimeException();
    }

    @Override
    public boolean remove(int t) {
        if (t == EMPTY_MARK) {
            if (containsEmptyMark) {
                containsEmptyMark = false;
                size--;
                modCount++;
                return true;
            } else {
                return false;
            }
        }
        if (t == REMOVED_MARK) {
            if (containsRemovedMark) {
                containsRemovedMark = false;
                size--;
                modCount++;
                return true;
            } else {
                return false;
            }
        }
        for (int i : probingList.setBase(t)) {
            int v = array[i];
            if (v == EMPTY_MARK) {
                return false;
            }
            if (t == v) {
                array[i] = REMOVED_MARK;
                size--;
                modCount++;
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        containsEmptyMark = false;
        containsRemovedMark = false;
        Arrays.fill(array, EMPTY_MARK);
        size = 0;
        modCount++;
    }

    private class SafeIterator implements PrimitiveIterator.OfInt {

        int currentIndex = 0;

        int previousIndex = -1;

        int expectedModCount = modCount;

        SafeIterator() {
            super();
            while (currentIndex < array.length) {
                int v = array[currentIndex];
                if (v == EMPTY_MARK || v == REMOVED_MARK) {
                    currentIndex++;
                } else {
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return currentIndex < array.length;
        }

        @Override
        public int nextInt() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            final int t = array[currentIndex];
            previousIndex = currentIndex;
            while (true) {
                if (++currentIndex == array.length) {
                    break;
                }
                int v = array[currentIndex];
                if (v != EMPTY_MARK && v != REMOVED_MARK) {
                    break;
                }
            }
            return t; // never return EMPTY_MARK and REMOVED_MARK ?
        }

        @Override
        public void remove() {
            if (previousIndex == -1) {
                throw new IllegalStateException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            {
                int v = array[previousIndex];
                assert v != EMPTY_MARK && v != REMOVED_MARK;
            }
            array[previousIndex] = REMOVED_MARK;
            size--;
            expectedModCount = ++modCount;
            previousIndex = -1;
        }
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new SafeIterator();
    }

    @NotNull
    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(iterator(), size, Spliterator.DISTINCT | Spliterator.SIZED);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + size + ", modCount = " + modCount + ", modulo = " + array.length + ", empty = " + emptySlotCount() + ", removed = " + removedSlotCount() + ">";
    }

    @Override
    public void stringifyDetail(@NotNull StringBuilder sb) {
        sb.append("[");
        if (containsEmptyMark) {
            sb.append(EMPTY_MARK);
        }
        if (containsRemovedMark) {
            if (containsEmptyMark) {
                sb.append(", ");
            }
            sb.append(REMOVED_MARK);
        }
        if (containsEmptyMark || containsRemovedMark) {
            sb.append("; ");
        }
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            int v = array[i];
            if (v == EMPTY_MARK) {
                sb.append(CollectionConstant.EMPTY);
            } else if (v == REMOVED_MARK) {
                sb.append(CollectionConstant.REMOVED);
            } else {
                sb.append(v);
            }
        }
        sb.append("]");
    }
}
