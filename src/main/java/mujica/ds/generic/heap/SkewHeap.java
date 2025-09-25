package mujica.ds.generic.heap;

import mujica.ds.ConsistencyException;
import mujica.ds.ReferenceException;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/6/1", project = "Ultramarine")
@CodeHistory(date = "2025/6/3")
@Name(value = "斜堆", language = "zh")
public class SkewHeap<E> extends BiasedPriorityQueue<E> {

    public SkewHeap(@Nullable Comparator<E> comparator) {
        super(comparator);
    }

    @NotNull
    @Override
    public SkewHeap<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final SkewHeap<E> that = new SkewHeap<>(comparator);
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
            expectedSize = root.checkSkewHealth(comparator, consumer);
        }
        if (expectedSize != size) {
            consumer.accept(new ConsistencyException("size", expectedSize, size));
        }
    }

    private BiasedNode<E> merge(@Nullable BiasedNode<E> t0, @Nullable BiasedNode<E> t1) {
        if (t0 == null) {
            return t1;
        }
        if (t1 == null) {
            return t0;
        }
        if (comparator.compare(t0.element, t1.element) > 0) {
            BiasedNode<E> t2 = t0;
            t0 = t1;
            t1 = t2;
        }
        final BiasedNode<E> child = merge(t0.shallowChild, t1);
        t0.shallowChild = t0.deepChild;
        t0.deepChild = child;
        return t0;
    }

    @Override
    void offerTree(@NotNull BiasedNode<E> tree, int treeSize) {
        root = merge(root, tree);
        modCount++;
        size += treeSize;
    }

    @Override
    BiasedNode<E> removeNode(@NotNull BiasedNode<E> node, BiasedNode<E> parent) {
        if (parent != null) {
            boolean shallow;
            if (parent.shallowChild == node) {
                shallow = true;
            } else if (parent.deepChild == node) {
                shallow = false;
            } else {
                throw new ReferenceException();
            }
            node = merge(node.deepChild, node.shallowChild);
            if (shallow) {
                parent.shallowChild = node;
            } else {
                parent.deepChild = node;
            }
        } else {
            node = merge(node.deepChild, node.shallowChild);
            root = node;
        }
        modCount++;
        size--;
        return node;
    }

    @Override
    public void removeAllAndTransfer(@NotNull Collection<E> collection) {
        if (root == null) {
            return;
        }
        if (collection instanceof BiasedPriorityQueue) {
            BiasedPriorityQueue<E> that = (BiasedPriorityQueue<E>) collection;
            if (this.comparator.equals(that.comparator)) {
                BiasedNode<E> removed = root;
                int removedSize = size;
                clear();
                if (that instanceof LeftListTree) {
                    removed.balanceTree();
                }
                that.offerTree(removed, removedSize);
                return;
            }
        }
        super.removeAllAndTransfer(collection);
    }
}
