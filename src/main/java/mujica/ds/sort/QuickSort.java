package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/23", name = "ListQuickSort")
@CodeHistory(date = "2026/7/15")
@Name(value = "快速排序", language = "zh")
@ReferencePage(title = "快速排序", href = "https://oi-wiki.org/basic/quick-sort/")
public class QuickSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    @NotNull
    final SelectPivot<S, A> selectPivot;

    @NotNull
    final S pivotSlot, scanSlot, tempSlot;

    public QuickSort(@NotNull SlotArrayComparator<S, A> comparator, @NotNull SelectPivot<S, A> selectPivot) {
        super();
        this.comparator = comparator;
        this.selectPivot = selectPivot;
        this.pivotSlot = comparator.newSlot();
        this.scanSlot = comparator.newSlot();
        this.tempSlot = comparator.newSlot();
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        int startWriteIndex = startIndex;
        int endWriteIndex = endIndex;
        { // end of recursion
            int length = endWriteIndex - startWriteIndex;
            if (length <= 2) {
                if (length == 2) {
                    endWriteIndex--;
                    comparator.load(target, startWriteIndex, pivotSlot);
                    comparator.load(target, endWriteIndex, scanSlot);
                    if (comparator.compareSlot(pivotSlot, scanSlot) > 0) {
                        comparator.exchange(target, startWriteIndex, endWriteIndex);
                    }
                    return 1L;
                } else {
                    return 0L;
                }
            }
        }
        selectPivot.selectPivot(target, startIndex, endIndex, pivotSlot);
        int startReadIndex = startWriteIndex;
        int endReadIndex = endWriteIndex;
        long operationCount = 0L;
        LABEL:
        while (true) {
            while (true) { // skip from start to end
                comparator.load(target, startReadIndex, scanSlot);
                int sign = comparator.compareSlot(pivotSlot, scanSlot);
                if (sign >= 0) {
                    if (sign > 0) {
                        comparator.store(target, startWriteIndex++, scanSlot);
                    }
                    if (++startReadIndex >= endReadIndex) {
                        break LABEL;
                    }
                } else {
                    break;
                }
            }
            while (true) { // skip from end to start
                comparator.load(target, --endReadIndex, scanSlot);
                int sign = comparator.compareSlot(scanSlot, pivotSlot);
                if (sign >= 0) {
                    if (sign > 0) {
                        comparator.store(target, --endWriteIndex, scanSlot);
                    }
                    if (startReadIndex >= endReadIndex) {
                        break LABEL;
                    }
                } else {
                    ++endReadIndex;
                    break;
                }
            }
            { // possibly a swap
                comparator.load(target, startReadIndex++, scanSlot);
                comparator.load(target, --endReadIndex, tempSlot);
                comparator.store(target, startWriteIndex++, scanSlot);
                comparator.store(target, --endWriteIndex, tempSlot);
            }
            operationCount++;
            if (startReadIndex >= endReadIndex) {
                break;
            }
        }
        operationCount += sort(target, startIndex, startWriteIndex);
        operationCount += sort(target, endWriteIndex, endIndex);
        while (startWriteIndex < endWriteIndex) {
            comparator.store(target, startWriteIndex++, pivotSlot);
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull A target) {
        return sort(target, 0, comparator.length(target));
    }

    @NotNull
    @Override
    public String toString() {
        return "QuickSort[comparator = " + comparator + ", selectPivot = " + selectPivot + "]";
    }
}
