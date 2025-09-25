package mujica.ds.generic.heap;

import mujica.ds.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/6/28")
public class Median<E> extends AbstractCollection<E> implements DataStructure {

    private static final long serialVersionUID = 0x60b4b61a91ae4471L;

    @NotNull
    final PriorityQueue<E> low;

    @NotNull
    final PriorityQueue<E> high;

    Median(@NotNull PriorityQueue<E> low, @NotNull PriorityQueue<E> high) {
        super();
        this.low = low;
        this.high = high;
    }

    @NotNull
    @Override
    public Median<E> duplicate() {
        return duplicate(UnaryOperator.identity());
    }

    @NotNull
    public Median<E> duplicate(@NotNull UnaryOperator<E> operator) {
        return new Median<>(low.duplicate(operator), high.duplicate(operator));
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        low.checkHealth(consumer);
        high.checkHealth(consumer);
    }

    @Override
    public int size() {
        return low.size() + high.size();
    }

    @Override
    public boolean isEmpty() {
        return low.isEmpty();
    }

    @Override
    public boolean contains(Object object) {
        return low.contains(object) || high.contains(object);
    }

    @Override
    public boolean add(E element) {
        if (low.size() > high.size()) {
            if (low.getComparator().compare(low.element(), element) > 0) {
                low.add(element);
                low.removeAndTransfer(high);
            } else {
                high.add(element);
            }
        } else if (low.isEmpty()) {
            low.add(element);
        } else {
            if (low.getComparator().compare(low.element(), element) >= 0) {
                low.add(element);
            } else {
                high.add(element);
                high.removeAndTransfer(low);
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return low.removeAll(collection) | high.removeAll(collection);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return low.removeIf(filter) | high.removeIf(filter);
    }

    @Override
    public void clear() {
        low.clear();
        high.clear();
    }

    public E getFloorMedian() throws NoSuchElementException {
        return low.element();
    }

    public E getCeilMedian() throws NoSuchElementException {
        if (low.size() == high.size()) {
            return high.element();
        } else {
            assert low.size() == high.size() + 1;
            return low.element();
        }
    }

    public E getMedian(@NotNull BinaryOperator<E> operator) throws NoSuchElementException {
        if (low.size() == high.size()) {
            return operator.apply(low.element(), high.element());
        } else {
            assert low.size() == high.size() + 1;
            return low.element();
        }
    }

    private class IteratorForTwo implements Iterator<E> {

        @NotNull
        private Iterator<E> it;

        private boolean isHigh;

        IteratorForTwo() {
            super();
            it = low.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public E next() {
            final E element = it.next();
            if (!it.hasNext() && !isHigh) {
                it = high.iterator();
                isHigh = true;
            }
            return element;
        }
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        return new IteratorForTwo();
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<low = " + low.summaryToString() + ", high = " + high.summaryToString() + ">";
    }

    @NotNull
    @Override
    public String detailToString() {
        return "[low = " + low.detailToString() + ", high = " + high.detailToString() + "]";
    }
}
