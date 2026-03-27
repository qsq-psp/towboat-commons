package mujica.ds.generic.set;

import mujica.ds.ConsistencyException;
import mujica.ds.InvariantException;
import mujica.ds.generic.list.TruncateList;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Consumer;

@CodeHistory(date = "2026/3/1")
public class WeakLinkOpenHashSet<E> extends AbstractHashSet<E> {

    private static final long serialVersionUID = 0xB51B35650B328FC7L;

    @CodeHistory(date = "2026/3/3")
    private static class Node<E> extends WeakReference<E> {

        final int hash;

        Node<E> next;

        Node(E element, int hash, Node<E> next) {
            super(element);
            this.hash = hash;
            this.next = next;
        }

        @Override
        @NotNull
        public String toString() {
            final E element = get();
            if (element != null) {
                return element.toString();
            } else {
                return "@" + Integer.toHexString(hash);
            }
        }
    }

    final TruncateList<Node<E>> list;

    public WeakLinkOpenHashSet(@Nullable ResizePolicy policy) {
        super(policy);
        list = new TruncateList<>();
        list.padTail(this.policy.initialCapacity(), null);
    }

    WeakLinkOpenHashSet(@NotNull ResizePolicy policy, @NotNull TruncateList<Node<E>> list) {
        super(policy);
        this.list = list;
    }

    @NotNull
    @Override
    public WeakLinkOpenHashSet<E> duplicate() {
        final TruncateList<Node<E>> thatList = new TruncateList<>(this.list.size());
        for (Node<E> thisTail : this.list) {
            Node<E> thatTail = null;
            while (thisTail != null) {
                E element = thisTail.get();
                if (element != null) {
                    Node<E> node = new Node<>(element, thisTail.hash, null);
                    if (thatTail == null) {
                        thatList.add(node);
                    } else {
                        thatTail.next = node;
                    }
                    thatTail = node;
                }
                thisTail = thisTail.next;
            }
            if (thatTail == null) {
                thatList.add(null);
            }
        }
        final WeakLinkOpenHashSet<E> that = new WeakLinkOpenHashSet<>(policy, thatList);
        that.size = this.size;
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int mod = list.size();
        final HashSet<Object> set = new HashSet<>();
        for (int j = 0; j < mod; j++) {
            Node<E> node = list.get(j);
            while (node != null) {
                int expectedHash = node.hash;
                int i = (Integer.MAX_VALUE & expectedHash) % mod;
                if (i != j) {
                    consumer.accept(new InvariantException("misplaced node " + node + " from " + i + " to " + j));
                }
                E element = node.get();
                if (element == null) {
                    continue;
                }
                int actualHash = element.hashCode();
                if (expectedHash != actualHash) {
                    consumer.accept(new InvariantException("mismatched hash expect " + expectedHash + " actual " + actualHash));
                }
                if (!set.add(element)) {
                    consumer.accept(new InvariantException("identical element " + element));
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
        return false;
    }

    @Override
    public boolean add(E element) {
        return false;
    }

    @Override
    public boolean remove(Object element) {
        return false;
    }

    @Override
    public void clear() {

    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + size + ", modification = " + modCount + ", used = " + usedSlotCount() + ">";
    }
}
