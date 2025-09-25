package mujica.ds.generic.heap;

import mujica.ds.of_boolean.PublicBooleanSlot;
import mujica.ds.of_int.PublicIntSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@CodeHistory(date = "2025/6/2")
public abstract class BiasedPriorityQueue<E> extends AbstractPriorityQueue<E> {

    private static final long serialVersionUID = 0x39e726abbb9a1b07L;

    @Nullable
    BiasedNode<E> root;

    int size;

    protected BiasedPriorityQueue(@Nullable Comparator<E> comparator) {
        super(comparator);
    }

    @NotNull
    @Override
    public BiasedPriorityQueue<E> clone() {
        try {
            BiasedPriorityQueue<E> that = (BiasedPriorityQueue<E>) super.clone();
            if (this.root != null) {
                that.root = this.root.duplicate();
            }
            that.modCount = 0;
            return that;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public long sumOfDepth() {
        if (root == null) {
            return 0L;
        }
        return root.sumOfDepth(0);
    }

    void offerNode(@NotNull BiasedNode<E> node) {
        offerTree(node, 1);
    }

    abstract void offerTree(@NotNull BiasedNode<E> tree, int treeSize); // size and modCount update inside

    @Nullable
    abstract BiasedNode<E> removeNode(@NotNull BiasedNode<E> node, @Nullable BiasedNode<E> parent); // size and modCount update inside

    @Override
    public boolean offer(E element) {
        offerNode(new BiasedNode<>(element));
        return true;
    }

    @Override
    public E remove() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        final E removed = root.element;
        removeNode(root, null);
        return removed;
    }

    @Override
    public E poll() {
        if (root == null) {
            return null;
        }
        final E removed = root.element;
        removeNode(root, null);
        return removed;
    }

    @Override
    public E element() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        return root.element;
    }

    @Override
    public E peek() {
        if (root == null) {
            return null;
        }
        return root.element;
    }

    @Override
    public void removeAndTransfer(@NotNull Collection<E> collection) {
        if (root == null) {
            throw new NoSuchElementException();
        }
        if (collection instanceof BiasedPriorityQueue) {
            BiasedNode<E> removed = root;
            removeNode(root, null);
            removed.deepChild = null;
            removed.shallowChild = null;
            removed.distance = 0;
            ((BiasedPriorityQueue<E>) collection).offerNode(removed);
            return;
        }
        collection.add(remove());
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    class DefaultIterator implements Iterator<E> {

        BiasedNode<E>.Frame current, previous;

        int expectedModCount = modCount;

        DefaultIterator() {
            super();
            if (root != null) {
                current = root.new Frame(null);
            }
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (current == null) {
                throw new NoSuchElementException();
            }
            previous = current;
            current = previous.next();
            return previous.node().element;
        }

        @Override
        public void remove() {
            if (previous == null) {
                throw new IllegalStateException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            final BiasedNode<E> node = removeNode(previous.node(), previous.parent != null ? previous.parent.node() : null);
            if (node != null) {
                current = node.new Frame(previous.parent);
            }
            previous = null;
            expectedModCount = modCount;
        }
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        return new DefaultIterator();
    }

    @Override
    public boolean contains(Object object) {
        final PublicBooleanSlot result = new PublicBooleanSlot();
        if (root != null) {
            root.forEach(element -> {
                if (Objects.equals(element, object)) {
                    result.value = true;
                }
            });
        }
        return result.value;
    }

    @NotNull
    @Override
    public Object[] toArray() {
        final Object[] array = new Object[size];
        final PublicIntSlot pointer = new PublicIntSlot();
        if (root != null) {
            root.forEach(element -> array[pointer.value++] = element);
        }
        assert pointer.value == array.length;
        return array;
    }
}
