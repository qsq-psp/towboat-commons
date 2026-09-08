package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/9/2")
@Name(value = "希尔排序", language = "zh")
public class ShellSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    final S slot1, slot2;

    public ShellSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
        this.slot1 = comparator.newSlot();
        this.slot2 = comparator.newSlot();
    }

    private long sort(@NotNull A target, int startIndex, int endIndex, int step) {
        if (startIndex + step >= endIndex) {
            return 0L;
        }
        long operationCount = sort(target, startIndex, endIndex, step * 3 + 1);
        for (int index0 = startIndex + step; index0 < endIndex; index0++) {
            int index1 = index0;
            comparator.load(target, index1, slot1);
            while (true) {
                int index2 = index1 - step;
                if (index2 < startIndex) {
                    break;
                }
                comparator.load(target, index2, slot2);
                if (comparator.compareSlot(slot1, slot2) >= 0) {
                    break;
                }
                comparator.store(target, index1, slot2);
                index1 = index2;
                operationCount++;
            }
            comparator.store(target, index1, slot1);
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        return sort(target, startIndex, endIndex, 1);
    }

    @Override
    public long sort(@NotNull A target) {
        return sort(target, 0, comparator.length(target));
    }

    @NotNull
    @Override
    public String toString() {
        return "ShellSort[comparator = " + comparator + "]";
    }
}
