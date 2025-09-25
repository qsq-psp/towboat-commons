package mujica.ds.generic.set;

import mujica.math.algebra.random.RandomContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Created on 2025/6/5.
 */
public abstract class AbstractAxiomSet<E> extends AbstractCollection<E> implements AxiomSet<E> {

    private static final long serialVersionUID = 0x59e4957b619171bbL;

    @NotNull
    @Override
    public abstract AbstractAxiomSet<E> duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @Override
    public int size() {
        int count = 0;
        for (E ignored : this) {
            count++;
        }
        return count;
    }

    @Override
    public E getArbitrary(@Nullable RandomContext rc) throws NoSuchElementException {
        final Iterator<E> it = iterator();
        E selected;
        if (it.hasNext()) {
            selected = it.next();
        } else {
            throw new NoSuchElementException();
        }
        if (rc != null) {
            int count = 1;
            while (it.hasNext()) {
                E element = it.next();
                if (rc.nextInt(++count) == 0) {
                    selected = element;
                }
            }
        }
        return selected;
    }

    @Override
    public E removeArbitrary(@Nullable RandomContext rc) throws NoSuchElementException {
        final Iterator<E> it = iterator();
        if (rc == null) {
            if (it.hasNext()) {
                E element = it.next();
                it.remove();
                return element;
            } else {
                throw new NoSuchElementException();
            }
        } else {
            int index = size();
            if (index == 0) {
                throw new NoSuchElementException();
            }
            index = rc.nextInt(index);
            while (index-- > 0) {
                it.next();
            }
            E element = it.next();
            it.remove();
            return element;
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        return super.containsAll(collection); // override to mark not null
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> collection) {
        return super.addAll(collection); // override to mark not null
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        return super.retainAll(collection); // override to mark not null
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        return super.removeAll(collection); // override to mark not null
    }

    @Override
    @NotNull
    public abstract Iterator<E> iterator();

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + size() + ">";
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder();
        stringifyDetail(sb);
        return sb.toString();
    }

    public void stringifyDetail(@NotNull StringBuilder sb) {
        sb.append("[");
        boolean subsequent = false;
        for (E element : this) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(element);
            subsequent = true;
        }
        sb.append("]");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + summaryToString() + " " + detailToString();
    }
}
