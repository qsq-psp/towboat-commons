package mujica.ds.generic.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CodeHistory(date = "2026/2/22")
@ReferencePage(title = "归并排序", href = "https://oi-wiki.org/basic/merge-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class ListMergeSort<T> extends SortingAlgorithm<List<T>> {

    @NotNull
    private final List<T> auxiliary = new ArrayList<>();

    @NotNull
    private final Comparator<T> comparator;

    public ListMergeSort(@NotNull Comparator<T> comparator) {
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
    public boolean returnsReverseOrderNumber() {
        return true;
    }

    private long sort(@NotNull List<T> target, @Index(of = "target") int targetOffset,
                      @NotNull List<T> temp, @Index(of = "temp") int tempOffset, int length) {
        if (length < 2) {
            return 0L;
        }
        final int leftLength = length >>> 1;
        long reverseOrderNumber = sort(target, targetOffset, temp, tempOffset, leftLength)
                + sort(target, targetOffset + leftLength, temp, tempOffset + leftLength, length - leftLength);
        for (int index = 0; index < length; index++) {
            temp.set(tempOffset + index, target.get(targetOffset + index));
        }
        int leftIndex = tempOffset;
        final int leftLimit = tempOffset + leftLength;
        int rightIndex = leftLimit;
        final int rightLimit = tempOffset + length;
        while (leftIndex < leftLimit && rightIndex < rightLimit) {
            if (comparator.compare(temp.get(rightIndex), temp.get(leftIndex)) > 0) {
                target.set(targetOffset++, temp.get(leftIndex++));
                reverseOrderNumber += leftLimit - leftIndex;
            } else {
                target.set(targetOffset++, temp.get(rightIndex++));
            }
        }
        while (leftIndex < leftLimit) {
            target.set(targetOffset++, temp.get(leftIndex++));
        }
        while (rightIndex < rightLimit) {
            target.set(targetOffset++, temp.get(rightIndex++));
        }
        return reverseOrderNumber;
    }

    @Override
    public long sort(@NotNull List<T> target, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        auxiliary.addAll(new ElementRepeatList<>(length));
        try {
            return sort(target, startIndex, auxiliary, 0, length);
        } finally {
            auxiliary.clear();
        }
    }

    @Override
    public long sort(@NotNull List<T> list) {
        return sort(list, 0, list.size());
    }
}
