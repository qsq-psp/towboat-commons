package mujica.ds.generic.heap;

import mujica.ds.generic.list.TruncateList;
import mujica.ds.of_boolean.PublicBooleanSlot;
import mujica.ds.of_int.PublicIntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/5/23", project = "Ultramarine")
@CodeHistory(date = "2025/6/6")
@Name(value = "二项队列", language = "zh")
public class BinomialQueue<E> extends AbstractPriorityQueue<E> {

    final TruncateList<SisterNode<E>> list = new TruncateList<>();

    public BinomialQueue(@Nullable Comparator<E> comparator) {
        super(comparator);
    }

    @NotNull
    @Override
    public AbstractPriorityQueue<E> clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public BinomialQueue<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final BinomialQueue<E> that = new BinomialQueue<>(comparator);
        for (SisterNode<E> tree : this.list) {
            if (tree != null) {
                tree = tree.duplicate(operator);
            }
            that.list.add(tree);
        }
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        Map<SisterNode<E>, String> map = null;
        final int listSize = list.size();
        for (int index = 0; index < listSize; index++) {
            SisterNode<E> tree = list.get(index);
            if (tree == null) {
                continue;
            }
            if (map == null) {
                map = new HashMap<>();
            }
            tree.checkBinomialQueueHealth(map, "on-stack[" + index + "]", "past[" + index + "]", comparator, consumer);
        }
    }

    @Override
    public int size() {
        final int listSize = list.size();
        int queueSize = 0;
        for (int index = 0; index < listSize; index++) {
            SisterNode<E> tree = list.get(index);
            if (tree == null) {
                continue;
            }
            if (index >= Integer.SIZE - 1) {
                return Integer.MAX_VALUE;
            }
            queueSize |= 1 << index;
        }
        return queueSize;
    }

    @Override
    public long sumOfDepth() {
        long sum = 0L;
        for (SisterNode<E> tree : list) {
            if (tree == null) {
                continue;
            }
            sum += tree.sumOfDepth(1);
        }
        return sum;
    }

    private void offerTree(@NotNull SisterNode<E> thatTree, int order) {
        while (true) {
            SisterNode<E> thisTree = list.get(order, null);
            if (thisTree == null) {
                list.padSet(order, thatTree, null);
                break;
            }
            list.set(order, null);
            if (comparator.compare(thisTree.element, thatTree.element) < 0) {
                SisterNode<E> tree = thisTree;
                thisTree = thatTree;
                thatTree = tree;
            }
            assert thisTree.nextSibling == null;
            assert thatTree.nextSibling == null;
            thisTree.nextSibling = thatTree.firstChild;
            thatTree.firstChild = thisTree;
            order++;
        }
    }

    private int minIndex() {
        E minElement = null;
        int minIndex = -1;
        for (int index = list.size() - 1; index >= 0; index--) {
            SisterNode<E> tree = list.get(index);
            if (tree != null && (minIndex == -1 || comparator.compare(tree.element, minElement) < 0)) {
                minElement = tree.element;
                minIndex = index;
            }
        }
        return minIndex;
    }

    private E removeAt(int index) {
        final SisterNode<E> removed = list.set(index, null);
        SisterNode<E> node = removed.firstChild;
        removed.firstChild = null;
        while (node != null) {
            SisterNode<E> next = node.nextSibling;
            node.nextSibling = null;
            offerTree(node, --index);
            node = next;
        }
        assert index == 0;
        return removed.element;
    }

    @Override
    public boolean offer(E element) {
        offerTree(new SisterNode<>(element), 0);
        return true;
    }

    @Override
    public E remove() {
        final int index = minIndex();
        if (index == -1) {
            throw new NoSuchElementException();
        }
        return removeAt(index);
    }

    @Override
    public E poll() {
        final int index = minIndex();
        if (index == -1) {
            return null;
        }
        return removeAt(index);
    }

    @Override
    public E element() {
        final int index = minIndex();
        if (index == -1) {
            throw new NoSuchElementException();
        }
        return list.get(index).element;
    }

    @Override
    public E peek() {
        final int index = minIndex();
        if (index == -1) {
            return null;
        }
        return list.get(index).element;
    }

    class DefaultIterator implements Iterator<E> {

        private int index;

        @Nullable
        private SisterNode<E>.Frame frame;

        final int expectedModCount = modCount;

        DefaultIterator() {
            super();
            final int listSize = list.size();
            while (index < listSize) {
                SisterNode<E> tree = list.get(index++);
                if (tree != null) {
                    frame = tree.new Frame(null);
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return frame != null;
        }

        @Override
        public E next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (frame == null) {
                throw new NoSuchElementException();
            }
            final E element = frame.node().element;
            frame = frame.next();
            if (frame == null) {
                int listSize = list.size();
                while (index < listSize) {
                    SisterNode<E> tree = list.get(index++);
                    if (tree != null) {
                        frame = tree.new Frame(null);
                        break;
                    }
                }
            }
            return element;
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new DefaultIterator();
    }

    void unsafeForEach(@NotNull Consumer<? super E> action) {
        for (SisterNode<E> tree : list) {
            if (tree == null) {
                continue;
            }
            tree.forEach(action);
        }
    }

    @Override
    public boolean contains(Object object) {
        final PublicBooleanSlot result = new PublicBooleanSlot();
        unsafeForEach(element -> {
            if (Objects.equals(element, object)) {
                result.value = true;
            }
        });
        return result.value;
    }

    @NotNull
    @Override
    public Object[] toArray() {
        final Object[] array = new Object[size()]; // size exact
        final PublicIntSlot pointer = new PublicIntSlot();
        unsafeForEach(element -> array[pointer.value++] = element);
        assert pointer.value == array.length;
        return array;
    }
}
