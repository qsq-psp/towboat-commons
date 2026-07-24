package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/22", name = "ListMergeSort")
@CodeHistory(date = "2026/7/21")
@Name(value = "归并排序", language = "zh")
@ReferencePage(title = "归并排序", href = "https://oi-wiki.org/basic/merge-sort/")
public class MergeSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    final S leftSlot, rightSlot;

    transient A auxiliaryArray;

    public MergeSort(@NotNull SlotArrayComparator<S, A> comparator, boolean shareAuxiliary) {
        super();
        this.comparator = comparator;
        this.leftSlot = comparator.newSlot();
        this.rightSlot = comparator.newSlot();
        if (shareAuxiliary) {
            auxiliaryArray = comparator.newArray(0);
        }
    }

    @NotNull
    A getAuxiliaryArray(int minLength) {
        A array = auxiliaryArray;
        if (array != null) {
            if (comparator.length(array) < minLength) {
                array = comparator.newArray(minLength);
                auxiliaryArray = array;
            }
            return array;
        }
        return comparator.newArray(minLength);
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public boolean returnsReverseOrderNumber() {
        return true;
    }

    private long sort(@NotNull A target, @Index(of = "target") int targetIndex,
                      @NotNull A auxiliary, @Index(of = "auxiliary") int auxiliaryIndex, int length) {
        if (length < 2) {
            return 0L;
        }
        final int leftLength = length >>> 1;
        long reverseOrderNumber = sort(target, targetIndex, auxiliary, auxiliaryIndex, leftLength)
                + sort(target, targetIndex + leftLength, auxiliary, auxiliaryIndex + leftLength, length - leftLength);
        comparator.copy(target, 0, auxiliary, 0, length);
        int leftIndex = targetIndex;
        final int leftEndIndex = targetIndex + leftLength;
        int rightIndex = leftEndIndex;
        final int rightEndIndex = targetIndex + length;
        while (leftIndex < leftEndIndex && rightIndex < rightEndIndex) {
            comparator.load(auxiliary, leftIndex, leftSlot);
            comparator.load(auxiliary, rightIndex, rightSlot);
            if (comparator.compareSlot(leftSlot, rightSlot) < 0) {
                comparator.store(target, targetIndex++, leftSlot);
                reverseOrderNumber += leftEndIndex - leftIndex;
                leftIndex++;
            } else {
                comparator.store(target, targetIndex++, rightSlot);
                rightIndex++;
            }
        }
        while (leftIndex < leftEndIndex) {
            comparator.load(auxiliary, leftIndex++, leftSlot);
            comparator.store(target, targetIndex++, leftSlot);
        }
        while (rightIndex < rightEndIndex) {
            comparator.load(auxiliary, rightIndex++, rightSlot);
            comparator.store(target, targetIndex++, rightSlot);
        }
        return reverseOrderNumber;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        return sort(target, startIndex, getAuxiliaryArray(length), 0, length);
    }

    @Override
    public long sort(@NotNull A target) {
        return sort(target, 0, comparator.length(target));
    }
}
