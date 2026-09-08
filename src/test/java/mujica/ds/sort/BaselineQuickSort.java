package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/9/7")
public class BaselineQuickSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    @NotNull
    final SelectPivot<S, A> selectPivot;

    @NotNull
    final S pivotSlot, startSlot, endSlot, tempSlot;

    public BaselineQuickSort(@NotNull SlotArrayComparator<S, A> comparator, @NotNull SelectPivot<S, A> selectPivot) {
        super();
        this.comparator = comparator;
        this.selectPivot = selectPivot;
        this.pivotSlot = comparator.newSlot();
        this.startSlot = comparator.newSlot();
        this.endSlot = comparator.newSlot();
        this.tempSlot = comparator.newSlot();
    }

    boolean validPivot(@NotNull A target, int startIndex, int endIndex) {
        comparator.load(target, startIndex++, startSlot); // min
        comparator.move(startSlot, endSlot); // max
        while (startIndex < endIndex) {
            comparator.load(target, startIndex++, tempSlot);
            if (comparator.compareSlot(tempSlot, startSlot) < 0) {
                comparator.move(tempSlot, startSlot);
            } else if (comparator.compareSlot(tempSlot, endSlot) > 0) {
                comparator.move(tempSlot, endSlot);
            }
        }
        return comparator.compareSlot(pivotSlot, startSlot) >= 0 && comparator.compareSlot(pivotSlot, endSlot) <= 0;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        int startWriteIndex = startIndex;
        int endWriteIndex = endIndex;
        { // end of recursion
            int length = endWriteIndex - startWriteIndex;
            if (length <= 2) {
                if (length == 2) {
                    comparator.load(target, startWriteIndex, startSlot);
                    comparator.load(target, --endWriteIndex, endSlot);
                    if (comparator.compareSlot(startSlot, endSlot) > 0) {
                        comparator.exchange(target, startWriteIndex, endWriteIndex);
                    }
                    return 1L;
                } else {
                    return 0L;
                }
            }
        }
        long operationCount = 0L;
        {
            int startReadIndex = startWriteIndex;
            int endReadIndex = endWriteIndex;
            boolean stage = false;
            selectPivot.selectPivot(target, startIndex, endIndex, pivotSlot);
            assert validPivot(target, startIndex, endIndex);
            while (startReadIndex < endReadIndex) {
                if (stage) {
                    comparator.load(target, --endReadIndex, endSlot);
                    int sign = comparator.compareSlot(pivotSlot, endSlot);
                    if (sign < 0) {
                        comparator.load(target, --endWriteIndex, tempSlot);
                        comparator.store(target, endReadIndex, tempSlot);
                        comparator.store(target, endWriteIndex, endSlot);
                    } else if (sign > 0) {
                        comparator.load(target, startWriteIndex, tempSlot);
                        comparator.store(target, startReadIndex - 1, tempSlot);
                        comparator.load(target, --endWriteIndex, tempSlot);
                        comparator.store(target, endReadIndex, tempSlot);
                        comparator.store(target, startWriteIndex++, endSlot);
                        comparator.store(target, endWriteIndex, startSlot);
                        stage = false;
                    }
                } else {
                    comparator.load(target, startReadIndex++, startSlot);
                    int sign = comparator.compareSlot(pivotSlot, startSlot);
                    if (sign > 0) {
                        comparator.load(target, startWriteIndex, tempSlot);
                        comparator.store(target, startReadIndex - 1, tempSlot);
                        comparator.store(target, startWriteIndex++, startSlot);
                    } else if (sign < 0) {
                        stage = true;
                    }
                }
                operationCount++;
            }
            if (stage) {
                comparator.load(target, --endWriteIndex, tempSlot);
                comparator.store(target, startReadIndex - 1, tempSlot);
                comparator.store(target, endWriteIndex, startSlot);
            }
        }
        if (startWriteIndex >= endIndex) {
            throw new AssertionError("too large pivot");
        }
        operationCount += sort(target, startIndex, startWriteIndex);
        if (endWriteIndex <= startIndex) {
            throw new AssertionError("too small pivot");
        }
        operationCount += sort(target, endWriteIndex, endIndex);
        return operationCount;
    }
}
