package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/9/20")
public class GallopMergeSort<S, A> extends MergeSort<S, A> {

    @NotNull
    final Sort<A> next;

    final int minMerge;

    final int minGallop;

    public GallopMergeSort(@NotNull SlotArrayComparator<S, A> comparator, boolean shareAuxiliary, @NotNull Sort<A> next, int minMerge, int minGallop) {
        super(comparator, shareAuxiliary);
        if (this.isDescending() != next.isDescending()) {
            throw new IllegalArgumentException();
        }
        if (minMerge < 1 || minGallop < 1) {
            throw new IllegalArgumentException();
        }
        this.next = next;
        this.minMerge = minMerge;
        this.minGallop = minGallop;
    }

    public GallopMergeSort(@NotNull SlotArrayComparator<S, A> comparator, boolean shareAuxiliary, int minMerge, int minGallop) {
        super(comparator, shareAuxiliary);
        if (minMerge < 1 || minGallop < 1) {
            throw new IllegalArgumentException();
        }
        this.next = new BinaryInsertionSort<>(comparator, leftSlot, rightSlot);
        this.minMerge = minMerge;
        this.minGallop = minGallop;
    }

    public GallopMergeSort(@NotNull SlotArrayComparator<S, A> comparator, boolean shareAuxiliary) {
        this(comparator, shareAuxiliary, 32, 7);
    }

    @Override
    public boolean returnsReverseOrderNumber() {
        return false;
    }

    private long sort(@NotNull A target, @Index(of = "target") int targetIndex,
                      @NotNull A auxiliary, @Index(of = "auxiliary") int auxiliaryIndex,
                      int leftLength, int rightLength) {
        long operationCount = 2L;
        if (leftLength >= minMerge) {
            int halfLeftLength = leftLength >>> 1;
            operationCount += sort(
                    target, targetIndex - halfLeftLength,
                    auxiliary, auxiliaryIndex - halfLeftLength,
                    leftLength - halfLeftLength, halfLeftLength
            );
        } else {
            operationCount += next.sort(target, targetIndex - leftLength, targetIndex);
        }
        if (rightLength >= minMerge) {
            int halfRightLength = rightLength >>> 1;
            operationCount += sort(
                    target, targetIndex + halfRightLength,
                    auxiliary, auxiliaryIndex + halfRightLength,
                    halfRightLength, rightLength - halfRightLength
            );
        } else {
            operationCount += next.sort(target, targetIndex, targetIndex + rightLength);
        }
        assert leftLength > 0;
        assert rightLength > 0;
        comparator.load(target, targetIndex, rightSlot);
        while (true) { // gallop (from left to) right
            comparator.load(target, targetIndex - leftLength, leftSlot);
            if (comparator.compareSlot(leftSlot, rightSlot) <= 0) {
                if (--leftLength <= 0) {
                    return operationCount;
                }
            } else {
                break;
            }
        }
        comparator.load(target, targetIndex - 1, leftSlot);
        while (true) { // gallop (from right to) left
            comparator.load(target, targetIndex + rightLength - 1, rightSlot);
            if (comparator.compareSlot(leftSlot, rightSlot) <= 0) {
                if (--rightLength <= 0) {
                    return operationCount;
                }
            } else {
                break;
            }
        }
        int leftIndex = auxiliaryIndex - leftLength;
        // final int leftEndIndex = auxiliaryIndex;
        int rightIndex = auxiliaryIndex;
        final int rightEndIndex = auxiliaryIndex + rightLength;
        {
            int copyLength = leftLength + rightLength;
            targetIndex -= leftLength;
            comparator.copy(target, targetIndex, auxiliary, leftIndex, copyLength);
            operationCount += copyLength;
        }
        while (true) {
            comparator.load(auxiliary, leftIndex, leftSlot);
            comparator.load(auxiliary, rightIndex, rightSlot);
            if (comparator.compareSlot(leftSlot, rightSlot) < 0) {
                comparator.store(target, targetIndex++, leftSlot);
                leftIndex++;
                assert leftIndex < auxiliaryIndex;
            } else {
                comparator.store(target, targetIndex++, rightSlot);
                rightIndex++;
                if (rightIndex >= rightEndIndex) {
                    break;
                }
            }
        }
        if (leftIndex < auxiliaryIndex) {
            comparator.copy(auxiliary, leftIndex, target, targetIndex, auxiliaryIndex - leftIndex);
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length <= 1) {
            return 0L;
        }
        final int leftLength = length >>> 1;
        return sort(
                target, startIndex + leftLength,
                getAuxiliaryArray(length), leftLength,
                leftLength, length - leftLength
        );
    }
}
