package mujica.ds.generic.set;

import mujica.ds.generic.list.TruncateList;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created on 2026/3/1.
 */
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
        return null;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        //
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
}
