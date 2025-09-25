package mujica.ds.generic.heap;

import mujica.ds.generic.ComparableComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;

@CodeHistory(date = "2025/6/7")
public class LinkedComparator<E> implements Comparator<LinkedNode<E>>, Serializable {

    private static final long serialVersionUID = 0x8cd6ea77fb758861L;

    @NotNull
    final Comparator<E> comparator;

    public LinkedComparator(@Nullable Comparator<E> comparator) {
        super();
        if (comparator == null) {
            comparator = new ComparableComparator<>();
        }
        this.comparator = comparator;
    }

    @Override
    public int compare(LinkedNode<E> a, LinkedNode<E> b) {
        return comparator.compare(a.element, b.element);
    }

    @Override
    public int hashCode() {
        return comparator.hashCode() + 1;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof LinkedComparator && this.comparator.equals(((LinkedComparator<?>) object).comparator);
    }
}
