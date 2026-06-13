package mujica.ds.of_long.list;

import mujica.ds.generic.list.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;

@CodeHistory(date = "2025/4/4", name = "MergeSort")
@CodeHistory(date = "2025/11/25")
@ReferencePage(title = "归并排序", href = "https://oi-wiki.org/basic/merge-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class LongDescendingMergeSort extends SortingAlgorithm<long[]> {

    private transient long[] shared;

    public LongDescendingMergeSort(boolean hasShared) {
        super();
        if (hasShared) {
            shared = new long[0]; // PublicLongList.EMPTY.array;
        }
    }

    @NotNull
    private long[] getShared(int minLength) {
        long[] array = shared;
        if (array != null) {
            if (array.length < minLength) {
                array = new long[minLength];
                shared = array;
            }
            return array;
        }
        return new long[minLength];
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.DESCENDING;
    }

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public boolean returnsReverseOrderNumber() {
        return true;
    }

    @Override
    public long sort(@NotNull long[] target, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        return apply(target, startIndex, getShared(length), 0, length);
    }

    private long apply(@NotNull long[] target, @Index(of = "target") int targetOffset,
                       @NotNull long[] temp, @Index(of = "temp") int tempOffset, int length) {
        if (length < 2) {
            return 0L;
        }
        final int leftLength = length >>> 1;
        long reverseOrderNumber = apply(target, targetOffset, temp, tempOffset, leftLength)
                + apply(target, targetOffset + leftLength, temp, tempOffset + leftLength, length - leftLength);
        System.arraycopy(target, targetOffset, temp, tempOffset, length);
        int leftIndex = tempOffset;
        final int leftLimit = tempOffset + leftLength;
        int rightIndex = leftLimit;
        final int rightLimit = tempOffset + length;
        while (leftIndex < leftLimit && rightIndex < rightLimit) {
            if (temp[leftIndex] > temp[rightIndex]) {
                target[targetOffset++] = temp[leftIndex++];
                reverseOrderNumber += leftLimit - leftIndex;
            } else {
                target[targetOffset++] = temp[rightIndex++];
            }
        }
        while (leftIndex < leftLimit) {
            target[targetOffset++] = temp[leftIndex++];
        }
        while (rightIndex < rightLimit) {
            target[targetOffset++] = temp[rightIndex++];
        }
        return reverseOrderNumber;
    }
}
