package mujica.ds.generic.heap;

import mujica.ds.ConsistencyException;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/5/24", project = "Ultramarine")
@CodeHistory(date = "2025/6/5")
@Name(value = "斐波那契堆", language = "zh")
public class FibonacciHeap<E> extends SisterPriorityQueue<E> {

    private static final long serialVersionUID = 0x61af941e05aab750L;

    public FibonacciHeap(@Nullable Comparator<E> comparator) {
        super(comparator);
    }

    @NotNull
    @Override
    public FibonacciHeap<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final FibonacciHeap<E> that = new FibonacciHeap<>(comparator);
        if (this.root != null) {
            that.root = this.root.duplicate(operator);
        }
        that.size = this.size;
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        int expectedSize = 0;
        if (root != null) {
            expectedSize = root.checkFibonacciHeapHealth(comparator, consumer);
        }
        if (expectedSize != size) {
            consumer.accept(new ConsistencyException("size", expectedSize, size));
        }
    }

    @SuppressWarnings("unchecked")
    private void add(@NotNull Object[] array, @NotNull SisterNode<E> thatTree) {
        while (true) {
            SisterNode<E> thisTree = (SisterNode<E>) array[thatTree.order];
            if (thisTree == null) {
                array[thatTree.order] = thatTree;
                break;
            }
            assert thisTree.order == thatTree.order;
            array[thatTree.order] = null;
            if (comparator.compare(thisTree.element, thatTree.element) < 0) {
                SisterNode<E> tree = thisTree;
                thisTree = thatTree;
                thatTree = tree;
            }
            assert thisTree.nextSibling == null;
            assert thatTree.nextSibling == null;
            thisTree.nextSibling = thatTree.firstChild;
            thatTree.firstChild = thisTree;
            thatTree.order++;
        }
    }

    /**
     * @param link0 linked binomial tree with unique order
     * @param link1 linked binomial tree with repeated order
     */
    @SuppressWarnings("unchecked")
    private SisterNode<E> merge(@Nullable SisterNode<E> link0, SisterNode<E> link1, int totalSize) {
        final Object[] array = new Object[Integer.SIZE - Integer.numberOfLeadingZeros(totalSize)];
        while (link0 != null) {
            SisterNode<E> next = link0.nextSibling;
            link0.nextSibling = null;
            assert array[link0.order] == null;
            array[link0.order] = link0;
            link0 = next;
        }
        while (link1 != null) {
            SisterNode<E> next = link1.nextSibling;
            link1.nextSibling = null;
            add(array, link1);
            link1 = next;
        }
        E minElement = null;
        int minIndex = -1;
        for (int index = array.length - 1; index >= 0; index--) {
            Object object = array[index];
            if (object == null) {
                continue;
            }
            SisterNode<E> tree = (SisterNode<E>) object;
            if (minIndex == -1 || comparator.compare(tree.element, minElement) < 0) {
                minElement = tree.element;
                minIndex = index;
            }
        }
        if (minIndex == -1) {
            return null;
        }
        final SisterNode<E> head = (SisterNode<E>) array[minIndex];
        SisterNode<E> tail = head;
        array[minIndex] = null;
        for (Object object : array) {
            if (object == null) {
                continue;
            }
            SisterNode<E> tree = (SisterNode<E>) object;
            tail.nextSibling = tree;
            tail = tree;
            assert tail.nextSibling == null;
        }
        return head;
    }

    @Override
    void offerTree(@NotNull SisterNode<E> tree, int treeSize) {
        assert tree.nextSibling == null;
        if (root != null && comparator.compare(root.element, tree.element) < 0) {
            // insert link after root
            tree.nextSibling = root.nextSibling;
            root.nextSibling = tree;
        } else {
            // insert link before root
            tree.nextSibling = root;
            root = tree;
        }
        size += treeSize;
        modCount++;
    }

    @Override
    void removeRoot() {
        assert root != null;
        size--;
        root = merge(root.firstChild, root.nextSibling, size);
        modCount++;
    }

    @Override
    public void removeAllAndTransfer(@NotNull Collection<E> collection) {
        if (root == null) {
            return;
        }
        if (collection instanceof SisterPriorityQueue) {
            SisterPriorityQueue<E> that = (SisterPriorityQueue<E>) collection;
            if (this.comparator.equals(that.comparator)) {
                SisterNode<E> removed = root;
                int removedSize = size;
                clear();
                that.offerTree(removed, removedSize);
                return;
            }
        }
        super.removeAllAndTransfer(collection);
    }
}
