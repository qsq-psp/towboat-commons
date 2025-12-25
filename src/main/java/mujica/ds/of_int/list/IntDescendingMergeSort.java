package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;

@CodeHistory(date = "2025/4/4", name = "MergeSort")
@CodeHistory(date = "2025/11/24")
@ReferencePage(title = "归并排序", href = "https://oi-wiki.org/basic/merge-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntDescendingMergeSort implements SortingAlgorithm<int[]> {

    private transient SoftReference<int[]> reference;

    @NotNull
    public int[] getTempArray(int minLength) {
        {
            SoftReference<int[]> reference = this.reference;
            if (reference != null) {
                int[] referent = reference.get();
                if (referent != null && minLength <= referent.length) {
                    return referent;
                }
            }
        }
        final int[] array = new int[minLength];
        this.reference = new SoftReference<>(array);
        return array;
    }

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public int orderingComposition() {
        return COMPOSITION_DESCENDING;
    }

    @Override
    public long apply(@NotNull int[] target, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        return apply(target, startIndex, getTempArray(length), 0, length);
    }

    private long apply(@NotNull int[] target, @Index(of = "target") int targetOffset,
                       @NotNull int[] temp, @Index(of = "temp") int tempOffset, int length) {
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
