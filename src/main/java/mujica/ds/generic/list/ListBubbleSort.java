package mujica.ds.generic.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.set.CollectionConstant;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

@CodeHistory(date = "2026/2/16")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class ListBubbleSort<T> extends SortingAlgorithm<List<T>> {

    @NotNull
    final Comparator<T> comparator;

    public ListBubbleSort(@NotNull Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public long sort(@NotNull List<T> list, int startIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < endIndex) {
            for (int index = startIndex + 1; index < endIndex; index++) {
                if (comparator.compare(list.get(index - 1), list.get(index)) > 0) {
                    list.set(index, list.set(index - 1, list.get(index)));
                    operationCount++;
                }
            }
            endIndex--;
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull List<T> list) {
        return sort(list, 0, list.size());
    }

    @Override
    public long sortPart(@NotNull List<T> list, int startIndex, int midIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < midIndex) {
            for (int index = endIndex - 1; index > startIndex; index--) {
                if (comparator.compare(list.get(index - 1), list.get(index)) > 0) {
                    list.set(index, list.set(index - 1, list.get(index)));
                    operationCount++;
                }
            }
            startIndex++;
        }
        return operationCount;
    }

    @Override
    @SuppressWarnings("unchecked")
    public long sortUnique(@NotNull List<T> list, int startIndex, @NotNull IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        long operationCount = 0L;
        int writeIndex = startIndex;
        int divIndex = startIndex;
        Object lastValue = CollectionConstant.EMPTY;
        while (divIndex < endIndex) {
            for (int index = endIndex - 1; index > divIndex; index--) {
                if (comparator.compare(list.get(index - 1), list.get(index)) > 0) {
                    list.set(index, list.set(index - 1, list.get(index)));
                    operationCount++;
                }
            }
            T currentValue = list.get(divIndex);
            if (lastValue == CollectionConstant.EMPTY || comparator.compare((T) lastValue, currentValue) != 0) {
                list.set(writeIndex++, currentValue);
                lastValue = currentValue;
            }
            divIndex++;
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
