package mujica.ds.generic.heap;

import mujica.ds.ConsistencyException;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/6/1", project = "Ultramarine")
@CodeHistory(date = "2025/6/5")
@Name(value = "配对堆", language = "zh")
@ReferencePage(title = "配对堆 OI Wiki", href = "https://oi-wiki.org/ds/pairing-heap/")
public class PairingHeap<E> extends SisterPriorityQueue<E> {

    private static final long serialVersionUID = 0x213186f1f0f32d9dL;

    public PairingHeap(@Nullable Comparator<E> comparator) {
        super(comparator);
    }

    @NotNull
    @Override
    public PairingHeap<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final PairingHeap<E> that = new PairingHeap<>(comparator);
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
            expectedSize = root.checkPairingHeapHealth(comparator, consumer);
        }
        if (expectedSize != size) {
            consumer.accept(new ConsistencyException("size", expectedSize, size));
        }
    }

    private SisterNode<E> meld(@Nullable SisterNode<E> t0, @Nullable SisterNode<E> t1) {
        if (t0 == null) {
            return t1;
        }
        if (t1 == null) {
            return t0;
        }
        assert t0.nextSibling == null;
        assert t1.nextSibling == null;
        if (comparator.compare(t0.element, t1.element) > 0) {
            SisterNode<E> t2 = t0;
            t0 = t1;
            t1 = t2;
        }
        t1.nextSibling = t0.firstChild;
        t0.firstChild = t1;
        return t0;
    }

    @Override
    void offerTree(@NotNull SisterNode<E> tree, int treeSize) {
        root = meld(root, tree);
        size += treeSize;
        modCount++;
    }

    @Override
    void removeRoot() {
        assert root != null;
        assert root.nextSibling == null;
        SisterNode<E> head = null;
        SisterNode<E> t0 = null;
        SisterNode<E> t1 = null;
        for (SisterNode<E> child = root.firstChild; child != null; child = child.nextSibling) {
            if (t0 == null) {
                t0 = child;
            } else if (t1 == null) {
                t1 = child;
            } else {
                t0.nextSibling = null;
                t1.nextSibling = null;
                SisterNode<E> t2 = meld(t0, t1);
                t2.nextSibling = head;
                head = t2;
                t0 = child;
                t1 = null;
            }
        }
        if (t0 != null) {
            if (t1 != null) {
                t0.nextSibling = null;
                t1.nextSibling = null;
                SisterNode<E> t2 = meld(t0, t1);
                t2.nextSibling = head;
                head = t2;
            } else {
                t0.nextSibling = head;
                head = t0;
            }
        }
        if (head != null) {
            while (head.nextSibling != null) {
                t0 = head;
                t1 = head.nextSibling;
                head = t1.nextSibling;
                t0.nextSibling = null;
                t1.nextSibling = null;
                SisterNode<E> t2 = meld(t0, t1);
                t2.nextSibling = head;
                head = t2;
            }
        }
        root = head;
        size--;
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
                if (that instanceof FibonacciHeap) {
                    removed.calculateOrder();
                }
                that.offerTree(removed, removedSize);
                return;
            }
        }
        super.removeAllAndTransfer(collection);
    }
}
