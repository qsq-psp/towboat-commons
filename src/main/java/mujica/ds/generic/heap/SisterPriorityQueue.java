package mujica.ds.generic.heap;

import mujica.ds.of_boolean.PublicBooleanSlot;
import mujica.ds.of_int.PublicIntSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@CodeHistory(date = "2025/6/4")
public abstract class SisterPriorityQueue<E> extends AbstractPriorityQueue<E> {

    private static final long serialVersionUID = 0x2a88baf27bc3dad9L;

    @Nullable
    SisterNode<E> root;

    int size;

    protected SisterPriorityQueue(@Nullable Comparator<E> comparator) {
        super(comparator);
    }

    @Override
    @NotNull
    public SisterPriorityQueue<E> clone() {
        try {
            SisterPriorityQueue<E> that = (SisterPriorityQueue<E>) super.clone();
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

    void offerNode(@NotNull SisterNode<E> node) {
        offerTree(node, 1);
    }

    abstract void offerTree(@NotNull SisterNode<E> tree, int treeSize); // size and modCount update inside

    abstract void removeRoot(); // size and modCount update inside

    @Override
    public boolean offer(E element) {
        offerNode(new SisterNode<>(element));
        return true;
    }

    @Override
    public E remove() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        final E removed = root.element;
        removeRoot();
        return removed;
    }

    @Override
    public E poll() {
        if (root == null) {
            return null;
        }
        final E removed = root.element;
        removeRoot();
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
    public void removeAllAndTransfer(@NotNull Collection<E> collection) {
        if (root == null) {
            throw new NoSuchElementException();
        }
        if (collection instanceof SisterPriorityQueue) {
            SisterNode<E> removed = root;
            removeRoot();
            removed.nextSibling = null;
            removed.firstChild = null;
            removed.order = 0;
            ((SisterPriorityQueue<E>) collection).offerNode(removed);
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

        @Nullable
        SisterNode<E>.Frame frame;

        final int expectedModCount = modCount;

        DefaultIterator() {
            super();
            if (root != null) {
                frame = root.new Frame(null);
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
            return element;
        }
    }

    @NotNull
    @Override
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
