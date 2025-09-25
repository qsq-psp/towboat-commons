package mujica.ds.generic.heap;

import mujica.ds.ConsistencyException;
import mujica.ds.ReferenceException;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Created on 2025/6/8.
 */
@CodeHistory(date = "2025/6/8")
public class LinkedPriorityQueue<E> extends AbstractCollection<E> implements PriorityQueue<E> {

    private static final long serialVersionUID = 0xab7597ef4271c691L;

    @NotNull
    private final PriorityQueue<LinkedNode<E>> priorityQueue;

    private LinkedNode<E> head;

    private LinkedNode<E> tail;

    public LinkedPriorityQueue(@NotNull PriorityQueue<LinkedNode<E>> priorityQueue) {
        super();
        this.priorityQueue = priorityQueue;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NotNull
    @Override
    public LinkedPriorityQueue<E> clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public LinkedPriorityQueue<E> duplicate() {
        return duplicate(UnaryOperator.identity());
    }

    @NotNull
    @Override
    public LinkedPriorityQueue<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final LinkedPriorityQueue<E> that = new LinkedPriorityQueue<>(priorityQueue.duplicate(thisNode -> {
            LinkedNode<E> thatNode = new LinkedNode<>(operator.apply(thisNode.element));
            thisNode.previous = thatNode;
            return thatNode;
        }));
        LinkedNode<E> thisNode = this.head;
        if (thisNode != null) {
            LinkedNode<E> thatNode = thisNode.previous;
            assert thatNode != null;
            that.head = thatNode;
            thisNode.previous = null;
            while (thisNode.next != null) {
                thatNode.next = thisNode.next.previous; // java.lang.NullPointerException
                thisNode.next.previous = thisNode;
                thatNode.next.previous = thatNode;
                thisNode = thisNode.next;
                thatNode = thatNode.next;
            }
            this.tail = thisNode;
            thisNode.next = null;
            that.tail = thatNode;
            thatNode.next = null;
        }
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        priorityQueue.checkHealth(consumer);
        final HashSet<LinkedNode<E>> set = new HashSet<>();
        LinkedNode<E> previous = null;
        LinkedNode<E> current = head;
        while (current != null) {
            if (!set.add(current)) {
                consumer.accept(new ReferenceException("revisit link node " + current));
                return;
            }
            if (previous != current.previous) {
                consumer.accept(new ReferenceException("back link from " + current + " to " + previous));
            }
            previous = current;
            current = current.next;
        }
        if (previous != tail) {
            consumer.accept(new ReferenceException("tail link expected " + previous + " actual " + tail));
        }
        if (set.size() != priorityQueue.size()) {
            consumer.accept(new ConsistencyException("size", set.size(), priorityQueue.size()));
        }
    }

    @NotNull
    public PriorityQueue<LinkedNode<E>> getPriorityQueue() {
        return priorityQueue;
    }

    @NotNull
    @Override
    public Comparator<E> getComparator() {
        final Comparator<LinkedNode<E>> comparator = priorityQueue.getComparator();
        if (comparator instanceof LinkedComparator) {
            return ((LinkedComparator<E>) comparator).comparator;
        } else {
            return Comparator.comparing(LinkedNode::new, comparator);
        }
    }

    @Override
    public int size() {
        return priorityQueue.size();
    }

    @Override
    public long sumOfDepth() {
        return priorityQueue.sumOfDepth();
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    private boolean offerNode(LinkedNode<E> node) {
        if (priorityQueue.offer(node)) {
            if (tail != null) {
                tail.next = node;
                node.previous = tail;
            } else {
                assert head == null;
                head = node;
            }
            tail = node;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean offer(E element) {
        return offerNode(new LinkedNode<>(element));
    }

    private void removeNode(@NotNull LinkedNode<E> node) {
        if (node.previous != null) {
            node.previous.next = node.next;
        } else {
            assert head == node;
            head = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        } else {
            assert tail == node;
            tail = node.previous;
        }
        node.previous = null;
        node.next = null;
    }

    @NotNull
    private LinkedNode<E> removeNode() {
        final LinkedNode<E> node = priorityQueue.remove();
        removeNode(node);
        return node;
    }

    @Override
    public E remove() {
        return removeNode().element;
    }

    @Override
    public E poll() {
        final LinkedNode<E> node = priorityQueue.poll();
        if (node != null) {
            removeNode(node);
            return node.element;
        } else {
            return null;
        }
    }

    @Override
    public E element() {
        return priorityQueue.element().element;
    }

    @Override
    public E peek() {
        final LinkedNode<E> node = priorityQueue.peek();
        if (node != null) {
            return node.element;
        } else {
            return null;
        }
    }

    @Override
    public void removeAndTransfer(@NotNull Collection<E> collection) {
        if (collection instanceof LinkedPriorityQueue) {
            ((LinkedPriorityQueue<E>) collection).offerNode(this.removeNode());
            return;
        }
        collection.add(remove());
    }

    @Override
    public void removeAllAndTransfer(@NotNull Collection<E> collection) {
        collection.addAll(this);
        clear();
    }

    private class ModCountIterator implements Iterator<E> {

        LinkedNode<E> node = head;

        final int expectedModCount = ((AbstractPriorityQueue<?>) priorityQueue).modCount;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            if (expectedModCount != ((AbstractPriorityQueue<?>) priorityQueue).modCount) {
                throw new ConcurrentModificationException();
            }
            final E element = node.element;
            node = node.next;
            return element;
        }
    }

    private class UnsafeIterator implements Iterator<E> {

        LinkedNode<E> node = head;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            final E element = node.element;
            node = node.next;
            return element;
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        if (priorityQueue instanceof AbstractPriorityQueue) {
            return new ModCountIterator();
        } else {
            return new UnsafeIterator();
        }
    }

    @Override
    public boolean contains(Object object) {
        LinkedNode<E> node = head;
        while (node != null) {
            if (Objects.equals(node.element, object)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @NotNull
    @Override
    public Object[] toArray() {
        final Object[] array = new Object[priorityQueue.size()];
        int index = 0;
        LinkedNode<E> node = head;
        while (node != null) {
            array[index++] = node.element;
            node = node.next;
        }
        assert index == array.length;
        return array;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return " <" + priorityQueue.getClass().getSimpleName() + " = " + priorityQueue.summaryToString() + "> ";
    }

    @NotNull
    @Override
    public String detailToString() {
        final Quote quote = Quote.DEFAULT;
        final StringBuilder sb = new StringBuilder();
        sb.append("[links = [");
        {
            boolean subsequent = false;
            LinkedNode<E> node = head;
            while (node != null) {
                if (subsequent) {
                    sb.append(", ");
                }
                quote.append(node.element, sb);
                node = node.next;
                subsequent = true;
            }
        }
        if (priorityQueue instanceof AbstractPriorityQueue) {
            sb.append("], heap = [");
            ((AbstractPriorityQueue<?>) priorityQueue).stringifyDetail(sb);
            sb.append("]");
        } else {
            sb.append("], heap = ").append(priorityQueue.detailToString());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + summaryToString() + detailToString();
    }
}
