package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/11", name = "ListHeapSort")
@CodeHistory(date = "2026/7/25")
@Name(value = "堆排序", language = "zh")
public class HeapSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    @NotNull
    final S currentSlot, parentSlot, leftSlot, rightSlot;

    public HeapSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
        this.currentSlot = comparator.newSlot();
        this.parentSlot = comparator.newSlot();
        this.leftSlot = comparator.newSlot();
        this.rightSlot = comparator.newSlot();
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        long operationCount = 0L;
        for (int divIndex = startIndex + 1; divIndex < endIndex; divIndex++) {
            int index = divIndex;
            comparator.load(target, index, currentSlot);
            while (index != startIndex) {
                int parentIndex = startIndex + ((index - startIndex - 1) >> 1);
                comparator.load(target, parentIndex, parentSlot);
                if (comparator.compareSlot(currentSlot, parentSlot) <= 0) {
                    break;
                }
                // swap
                comparator.store(target, index, parentSlot);
                comparator.store(target, parentIndex, currentSlot);
                index = parentIndex;
                operationCount++;
            }
        }
        for (int divIndex = endIndex - 1; divIndex > startIndex; divIndex--) {
            comparator.load(target, startIndex, parentSlot);
            comparator.load(target, divIndex, currentSlot);
            int index = startIndex;
            while (true) {
                int leftIndex = startIndex + 1 + ((index - startIndex) << 1);
                int rightIndex = leftIndex + 1;
                if (leftIndex < divIndex) {
                    comparator.load(target, leftIndex, leftSlot);
                    int largerIndex = leftIndex;
                    if (rightIndex < divIndex) {
                        comparator.load(target, rightIndex, rightSlot);
                        if (comparator.compareSlot(leftSlot, rightSlot) < 0) {
                            largerIndex = rightIndex;
                            comparator.move(rightSlot, leftSlot);
                        }
                    }
                    // now leftSlot is the larger slot
                    if (comparator.compareSlot(currentSlot, leftSlot) < 0) {
                        comparator.store(target, index, leftSlot);
                        index = largerIndex;
                        operationCount++;
                        continue;
                    }
                }
                break;
            }
            comparator.store(target, index, currentSlot);
            comparator.store(target, divIndex, parentSlot);
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
        return "HeapSort[comparator = " + comparator + "]";
    }
}
