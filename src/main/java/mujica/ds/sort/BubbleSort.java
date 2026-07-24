package mujica.ds.sort;

import mujica.ds.i32.I32Slot;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/16", name = "ListBubbleSort")
@CodeHistory(date = "2026/7/16")
@Name(value = "冒泡排序", language = "zh")
public class BubbleSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    final S leftSlot, rightSlot;

    public BubbleSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
        this.leftSlot = comparator.newSlot();
        this.rightSlot = comparator.newSlot();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < endIndex) {
            for (int index = startIndex + 1; index < endIndex; index++) {
                comparator.load(target, index - 1, leftSlot);
                comparator.load(target, index, rightSlot);
                if (comparator.compareSlot(leftSlot, rightSlot) > 0) {
                    comparator.store(target, index, leftSlot);
                    comparator.store(target, index - 1, rightSlot);
                    operationCount++;
                }
            }
            endIndex--;
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull A target) {
        return sort(target, 0, comparator.length(target));
    }

    @Override
    public long sortPart(@NotNull A target, int startIndex, int midIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < midIndex) {
            for (int index = endIndex - 1; index > startIndex; index--) {
                comparator.load(target, index - 1, leftSlot);
                comparator.load(target, index, rightSlot);
                if (comparator.compareSlot(leftSlot, rightSlot) > 0) {
                    comparator.store(target, index, leftSlot);
                    comparator.store(target, index - 1, rightSlot);
                    operationCount++;
                }
            }
            startIndex++;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull A target, int startIndex, @NotNull I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
        long operationCount = 0L;
        int writeIndex = startIndex;
        int divIndex = startIndex;
        while (divIndex < endIndex) {
            for (int index = endIndex - 1; index > divIndex; index--) {
                comparator.load(target, index - 1, leftSlot);
                comparator.load(target, index, rightSlot);
                if (comparator.compareSlot(leftSlot, rightSlot) > 0) {
                    comparator.store(target, index, leftSlot);
                    comparator.store(target, index - 1, rightSlot);
                    operationCount++;
                }
            }
            comparator.load(target, divIndex, rightSlot);
            if (writeIndex == startIndex) {
                comparator.store(target, writeIndex++, rightSlot);
            } else {
                comparator.load(target, writeIndex - 1, leftSlot);
                if (comparator.compareSlot(leftSlot, rightSlot) != 0) {
                    comparator.store(target, writeIndex++, rightSlot);
                }
            }
            divIndex++;
        }
        endSlot.setI32(writeIndex);
        return operationCount;
    }

    @NotNull
    @Override
    public String toString() {
        return "BubbleSort[comparator = " + comparator + "]";
    }
}
