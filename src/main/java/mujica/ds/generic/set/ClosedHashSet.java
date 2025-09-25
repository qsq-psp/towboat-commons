package mujica.ds.generic.set;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

@CodeHistory(date = "2025/7/4")
@ReferencePage(title = "Closed Hashing", href = "https://www.cs.usfca.edu/~galles/visualization/ClosedHash.html")
public abstract class ClosedHashSet<E> extends AbstractHashSet<E> {

    private static final long serialVersionUID = 0xa5bf061d9ea35761L;

    @NotNull
    protected Object[] array;

    protected ClosedHashSet(@Nullable ResizePolicy policy) {
        super(policy);
        array = new Object[this.policy.initialCapacity()];
        Arrays.fill(array, CollectionConstant.EMPTY);
    }

    protected ClosedHashSet(@NotNull ResizePolicy policy, @NotNull Object[] array) {
        super(policy);
        this.array = array;
    }

    public int emptySlotCount() {
        int count = 0;
        for (Object item : array) {
            if (item == CollectionConstant.EMPTY) {
                count++;
            }
        }
        return count;
    }

    public int removedSlotCount() {
        int count = 0;
        for (Object item : array) {
            if (item == CollectionConstant.REMOVED) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void clear() {
        Arrays.fill(array, CollectionConstant.EMPTY);
        size = 0;
        modCount++;
    }

    private class ClosedIterator implements Iterator<E> {

        int currentIndex = 0;

        int previousIndex = -1;

        int expectedModCount = modCount;

        ClosedIterator() {
            super();
            while (currentIndex < array.length && array[currentIndex] instanceof CollectionConstant) {
                currentIndex++;
            }
        }

        @Override
        public boolean hasNext() {
            return currentIndex < array.length;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            final E element = (E) array[currentIndex];
            previousIndex = currentIndex;
            do {
                currentIndex++;
            } while (currentIndex < array.length && array[currentIndex] instanceof CollectionConstant);
            return element;
        }

        @Override
        public void remove() {
            if (previousIndex == -1) {
                throw new IllegalStateException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            assert !(array[previousIndex] instanceof CollectionConstant);
            array[previousIndex] = CollectionConstant.REMOVED;
            size--;
            expectedModCount = ++modCount;
            previousIndex = -1;
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new ClosedIterator();
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + size + ", modification = " + modCount + ", modulo = " + array.length + ", empty = " + emptySlotCount() + ", removed = " + removedSlotCount() + ">";
    }

    @Override
    public void stringifyDetail(@NotNull StringBuilder sb) {
        Quote.DEFAULT.append(array, sb);
    }
}
