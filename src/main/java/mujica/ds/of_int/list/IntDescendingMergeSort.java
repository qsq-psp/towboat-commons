package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/4/4", name = "MergeSort")
@CodeHistory(date = "2025/11/24")
@ReferencePage(title = "归并排序", href = "https://oi-wiki.org/basic/merge-sort/")
@ReferencePage(title = "Comparison Sorting Visualization", href = "https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html")
public class IntDescendingMergeSort extends SortingAlgorithm<int[]> {

    private transient int[] internal;

    public IntDescendingMergeSort(boolean hasInternal) {
        super();
        if (hasInternal) {
            internal = PublicIntList.EMPTY.array;
        }
    }


    @NotNull
    private int[] getInternal(int minLength) {
        int[] array = internal;
        if (array != null) {
            if (array.length < minLength) {
                array = new int[minLength];
                internal = array;
            }
            return array;
        }
        return new int[minLength];
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

    private long sort(@NotNull int[] target, @Index(of = "target") int targetOffset,
                      @NotNull int[] temp, @Index(of = "temp") int tempOffset, int length) {
        if (length < 2) {
            return 0L;
        }
        final int leftLength = length >>> 1;
        long reverseOrderNumber = sort(target, targetOffset, temp, tempOffset, leftLength)
                + sort(target, targetOffset + leftLength, temp, tempOffset + leftLength, length - leftLength);
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

    @Override
    public long sort(@NotNull int[] target, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        return sort(target, startIndex, getInternal(length), 0, length);
    }

    private long sortPart(@NotNull int[] target, @Index(of = "target") int targetOffset,
                          @NotNull int[] temp, int inLength, int midIndex) {
        if (inLength < 2) {
            return 0L;
        }
        final int leftLength = inLength >>> 1;
        long reverseOrderNumber = sort(target, targetOffset, temp, 0, leftLength)
                + sort(target, targetOffset + leftLength, temp, leftLength, inLength - leftLength);
        System.arraycopy(target, targetOffset, temp, 0, inLength);
        int leftIndex = 0;
        int rightIndex = leftLength;
        while (leftIndex < leftLength && rightIndex < inLength && targetOffset < midIndex) {
            if (temp[leftIndex] > temp[rightIndex]) {
                target[targetOffset++] = temp[leftIndex++];
                reverseOrderNumber += leftLength - leftIndex;
            } else {
                target[targetOffset++] = temp[rightIndex++];
            }
        }
        while (leftIndex < leftLength && targetOffset < midIndex) {
            target[targetOffset++] = temp[leftIndex++];
        }
        while (rightIndex < inLength && targetOffset < midIndex) {
            target[targetOffset++] = temp[rightIndex++];
        }
        return reverseOrderNumber;
    }

    @Override
    public long sortPart(@NotNull int[] target, int startIndex, int midIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        return sortPart(target, startIndex, getInternal(length), length, midIndex);
    }

    private long sortUnique(@NotNull int[] target, @Index(of = "target") int targetOffset,
                            @NotNull int[] temp, int length, @NotNull IntSlot endSlot) {
        if (length < 2) {
            return 0L;
        }
        final int leftLength = length >>> 1;
        long reverseOrderNumber = sort(target, targetOffset, temp, 0, leftLength)
                + sort(target, targetOffset + leftLength, temp, leftLength, length - leftLength);
        System.arraycopy(target, targetOffset, temp, 0, length);
        int leftIndex = 0;
        int rightIndex = leftLength;
        int previousValue = Integer.MIN_VALUE;
        while (leftIndex < leftLength && rightIndex < length) {
            int currentValue;
            if (temp[leftIndex] > temp[rightIndex]) {
                currentValue = temp[leftIndex++];
                reverseOrderNumber += leftLength - leftIndex;
            } else {
                currentValue = temp[rightIndex++];
            }
            if (previousValue != currentValue) {
                target[targetOffset++] = currentValue;
                previousValue = currentValue;
            }
        }
        while (leftIndex < leftLength) {
            int currentValue = temp[leftIndex++];
            if (previousValue != currentValue) {
                target[targetOffset++] = currentValue;
                previousValue = currentValue;
            }
        }
        while (rightIndex < length) {
            int currentValue = temp[rightIndex++];
            if (previousValue != currentValue) {
                target[targetOffset++] = currentValue;
                previousValue = currentValue;
            }
        }
        endSlot.setInt(targetOffset);
        return reverseOrderNumber;
    }

    @Override
    public long sortUnique(@NotNull int[] target, int startIndex, @NotNull IntSlot endSlot) {
        final int length = endSlot.getInt() - startIndex;
        if (length < 2) {
            return 0L;
        }
        final long reverseOrderNumber = sortUnique(target, startIndex, getInternal(length), length, endSlot);
        if (startIndex == endSlot.getInt()) {
            target[startIndex++] = Integer.MIN_VALUE;
            endSlot.setInt(startIndex);
        }
        return reverseOrderNumber;
    }
}
