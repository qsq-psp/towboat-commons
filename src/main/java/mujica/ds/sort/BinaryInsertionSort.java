package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/9/26")
@Name(value = "二分插入排序", language = "zh")
public class BinaryInsertionSort<S, A> extends InsertionSort<S, A> {

    BinaryInsertionSort(@NotNull SlotArrayComparator<S, A> comparator, @NotNull S keySlot, @NotNull S scanSlot) {
        super(comparator, keySlot, scanSlot);
    }

    public BinaryInsertionSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super(comparator);
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        long operationCount = 0L;
        int readIndex = startIndex + 1;
        while (readIndex < endIndex) {
            comparator.load(target, readIndex, keySlot);
            int lowIndex = startIndex;
            int highIndex = readIndex - 1;
            while (lowIndex <= highIndex) {
                operationCount++;
                int midIndex = (lowIndex + highIndex) >>> 1;
                comparator.load(target, midIndex, scanSlot);
                int sign = comparator.compareSlot(keySlot, scanSlot);
                if (sign > 0) {
                    lowIndex = midIndex + 1;
                } else if (sign < 0) {
                    highIndex = midIndex - 1;
                } else {
                    lowIndex = midIndex + 1;
                    break;
                }
            }
            switch (readIndex - lowIndex) { // optimization for comparator.copyWithin()
                case 0:
                    break;
                case 1:
                    comparator.load(target, lowIndex, scanSlot);
                    comparator.store(target, readIndex, scanSlot);
                    comparator.store(target, lowIndex, keySlot);
                    break;
                case 2:
                    comparator.load(target, lowIndex + 1, scanSlot);
                    comparator.store(target, readIndex, scanSlot);
                    comparator.load(target, lowIndex, scanSlot);
                    comparator.store(target, lowIndex + 1, scanSlot);
                    comparator.store(target, lowIndex, keySlot);
                    break;
                default:
                    comparator.copyWithin(target, lowIndex, lowIndex + 1, readIndex - lowIndex);
                    comparator.store(target, lowIndex, keySlot);
                    break;
            }
            readIndex++;
        }
        return operationCount;
    }
}
