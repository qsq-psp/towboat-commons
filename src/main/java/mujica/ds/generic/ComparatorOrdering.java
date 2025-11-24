package mujica.ds.generic;

import mujica.ds.Ordering;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

@CodeHistory(date = "2025/11/20")
public class ComparatorOrdering<E, T extends List<E>> extends Ordering<T> {

    // no singleton instance provided because this class is generic

    @NotNull
    private final Comparator<E> comparator;

    public ComparatorOrdering(@NotNull Comparator<E> comparator) {
        super();
        this.comparator = comparator;
    }

    @Override
    public int orderingFlags(@NotNull T list) {
        final int size = list.size();
        if (size < 2) {
            return 0;
        }
        int flags = 0;
        E e1 = list.get(0);
        for (int index = 1; index < size; index++) {
            E e2 = list.get(index);
            int result = comparator.compare(e1, e2);
            if (result < 0) {
                flags |= STRICT_ASCENDING;
            } else if (result > 0) {
                flags |= STRICT_DESCENDING;
            } else {
                flags |= EQUAL;
            }
            e1 = e2;
        }
        return flags;
    }
}
