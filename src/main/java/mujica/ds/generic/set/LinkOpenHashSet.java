package mujica.ds.generic.set;

import mujica.ds.ConsistencyException;
import mujica.ds.InvariantException;
import mujica.ds.generic.list.TruncateList;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

@CodeHistory(date = "2025/6/8")
@ReferencePage(title = "Open Hashing", href = "https://www.cs.usfca.edu/~galles/visualization/OpenHash.html")
public class LinkOpenHashSet<E> extends AbstractHashSet<E> {

    private static final long serialVersionUID = 0xba6c85823572e18fL;

    private static class Node<E> implements Serializable {

        private static final long serialVersionUID = 0x0337ceefd81cfca3L;

        final E element;

        Node<E> next;

        Node(E element, Node<E> next) {
            super();
            this.element = element;
            this.next = next;
        }

        @Override
        public String toString() {
            return Objects.toString(element);
        }
    }

    @NotNull
    final TruncateList<Node<E>> list;

    public LinkOpenHashSet(@Nullable ResizePolicy policy) {
        super(policy);
        list = new TruncateList<>();
        list.padTail(this.policy.initialCapacity(), null);
    }

    LinkOpenHashSet(@NotNull ResizePolicy policy, @NotNull TruncateList<Node<E>> list) {
        super(policy);
        this.list = list;
    }

    @NotNull
    @Override
    public LinkOpenHashSet<E> duplicate() {
        final LinkOpenHashSet<E> that = new LinkOpenHashSet<>(policy, list);
        that.size = this.size;
        return that;
    }

    private int index(Object object, int mod) {
        int hash;
        if (object != null) {
            hash = Integer.MAX_VALUE & object.hashCode();
        } else {
            hash = 0;
        }
        return hash % mod;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int mod = list.size();
        final HashSet<Object> set = new HashSet<>();
        for (int j = 0; j < mod; j++) {
            Node<E> node = list.get(j);
            while (node != null) {
                int i = index(node.element, mod);
                if (i != j) {
                    consumer.accept(new InvariantException("misplaced item " + node.element + " from " + i + " to " + j));
                }
                if (!set.add(node.element)) {
                    consumer.accept(new InvariantException("identical item " + node.element));
                    break; // prevent infinite loop
                }
                node = node.next;
            }
        }
        if (set.size() != size) {
            consumer.accept(new ConsistencyException("size", set.size(), size));
        }
    }

    public int usedSlotCount() {
        int count = 0;
        for (Node<E> node : list) {
            if (node != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean contains(Object element) {
        Node<E> node = list.get(index(element, list.size()));
        while (node != null) {
            if (Objects.equals(node.element, element)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean add(E element) {
        final int i = index(element, list.size());
        Node<E> node = list.get(i);
        while (node != null) {
            if (Objects.equals(node.element, element)) {
                return false;
            }
            node = node.next;
        }
        list.set(i, new Node<>(element, list.get(i)));
        size++;
        modCount++;
        return true;
    }

    @Override
    public boolean remove(Object element) {
        final int i = index(element, list.size());
        Node<E> previous = null;
        Node<E> node = list.get(i);
        while (node != null) {
            if (Objects.equals(node.element, element)) {
                if (previous == null) {
                    list.set(i, node.next);
                } else {
                    previous.next = node.next;
                    node.next = null;
                }
                size--;
                modCount++;
                return true;
            }
            previous = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = list.size() - 1; i >= 0; i--) {
            list.set(i, null);
        }
        size = 0;
        modCount++;
    }

    private class SafeIterator implements Iterator<E> {

        int expectedModCount;

        int nextIndex;

        int currentIndex = -1;

        Node<E> nextNode;

        Node<E> currentNode;

        Node<E> previousNode;

        SafeIterator() {
            super();
            final int listSize = list.size();
            while (nextIndex < listSize) {
                nextNode = list.get(nextIndex);
                if (nextNode != null) {
                    break;
                }
                nextIndex++;
            }
            expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public E next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            currentIndex = nextIndex;
            if (currentNode != null) {
                previousNode = currentNode;
            }
            currentNode = nextNode;
            nextNode = nextNode.next;
            if (nextNode == null) {
                nextIndex++;
                int listSize = list.size();
                while (nextIndex < listSize) {
                    nextNode = list.get(nextIndex);
                    if (nextNode != null) {
                        break;
                    }
                    nextIndex++;
                }
            }
            return currentNode.element;
        }

        @Override
        public void remove() {
            if (currentNode == null) {
                throw new IllegalStateException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (previousNode != null && previousNode.next == currentNode) {
                previousNode.next = currentNode.next;
            } else {
                Node<E> node = list.set(currentIndex, currentNode.next);
                assert node == currentNode : toString();
            }
            currentNode = null;
            size--;
            expectedModCount = ++modCount;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("SafeIterator{");
            sb.append("nextIndex=").append(nextIndex);
            sb.append(", currentIndex=").append(currentIndex);
            sb.append(", nextNode=").append(nextNode);
            sb.append(", currentNode=").append(currentNode);
            sb.append(", previousNode=").append(previousNode);
            sb.append(", expectedModCount=").append(expectedModCount);
            sb.append('}');
            return sb.toString();
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new SafeIterator();
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + size + ", modification = " + modCount + ", used = " + usedSlotCount() + ">";
    }

    @Override
    public void stringifyDetail(@NotNull StringBuilder sb) {
        sb.append("[");
        final int length = list.size();
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("[");
            Node<E> node = list.get(i);
            if (node != null) {
                while (true) {
                    Quote.DEFAULT.append(node.element, sb);
                    node = node.next;
                    if (node == null) {
                        break;
                    }
                    sb.append(", ");
                }
            }
            sb.append("]");
        }
        sb.append("]");
    }
}
