package mujica.ds.generic.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.set.CollectionConstant;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@CodeHistory(date = "2026/3/4")
@ReferencePage(title = "锦标赛排序", href = "https://oi-wiki.org/basic/tournament-sort/")
public class ListTournamentSort<T> extends SortingAlgorithm<List<T>> implements Comparator<Object> {

    @NotNull
    private transient Object[] auxiliary = new Object[0];

    @NotNull
    private final Comparator<T> comparator;

    public ListTournamentSort(@NotNull Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    @NotNull
    private Object[] getAuxiliary(int minLength) {
        Object[] auxiliary = this.auxiliary;
        if (auxiliary.length < minLength) {
            auxiliary = new Object[minLength];
            this.auxiliary = auxiliary;
        }
        return auxiliary;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object a, Object b) {
        if (a == CollectionConstant.REMOVED) {
            if (b == CollectionConstant.REMOVED) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (b == CollectionConstant.REMOVED) {
                return -1;
            } else {
                return comparator.compare((T) a, (T) b);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Object min(Object a, Object b) {
        if (a == CollectionConstant.REMOVED) {
            return b;
        } else if (b == CollectionConstant.REMOVED) {
            return a;
        } else if (comparator.compare((T) a, (T) b) > 0) {
            return b;
        } else {
            return a;
        }
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull List<T> target, int startIndex, int endIndex) {
        return sortPart(target, startIndex, endIndex, endIndex);
    }

    @SuppressWarnings("unchecked")
    @Override
    public long sortPart(@NotNull List<T> target, int startIndex, int midIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        if (length >= 0x20000000) {
            throw new IndexOutOfBoundsException();
        }
        final int height = Integer.SIZE - Integer.numberOfLeadingZeros(length);
        final int offset = 1 << height;
        final int doubleOffset = offset << 1;
        final Object[] arena = getAuxiliary(doubleOffset);
        Arrays.fill(arena, 0, doubleOffset, CollectionConstant.REMOVED);
        for (int index = 0; index < length; index++) {
            arena[offset + index] = target.get(startIndex + index);
        }
        for (int leafIndex = offset + length; leafIndex >= offset; leafIndex--) {
            Object value = arena[leafIndex];
            int currentIndex = leafIndex;
            while (currentIndex > 1) {
                int parentIndex = currentIndex >> 1;
                if (compare(value, arena[parentIndex]) > 0) {
                    break;
                }
                arena[parentIndex] = value;
                currentIndex = parentIndex;
            }
        }
        int width = 0;
        for (int targetIndex = startIndex; targetIndex < midIndex; targetIndex++) {
            int currentIndex = 1;
            Object value = arena[currentIndex];
            target.set(targetIndex, (T) value);
            width++;
            while (currentIndex < offset) {
                currentIndex <<= 1;
                if (value != arena[currentIndex]) {
                    currentIndex++;
                }
            }
            arena[currentIndex] = CollectionConstant.REMOVED;
            while (currentIndex > 1) {
                int parentIndex = currentIndex >> 1;
                if ((currentIndex & 1) == 0) {
                    arena[parentIndex] = min(arena[currentIndex], arena[currentIndex + 1]);
                } else {
                    arena[parentIndex] = min(arena[currentIndex], arena[currentIndex - 1]);
                }
                currentIndex = parentIndex;
            }
        }
        return ((long) width) * height;
    }
}
