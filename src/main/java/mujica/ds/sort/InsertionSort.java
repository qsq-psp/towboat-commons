package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/15", name = "ListInsertionSort")
@CodeHistory(date = "2026/7/20")
@Name(value = "插入排序", language = "zh")
@ReferencePage(title = "插入排序", href = "https://oi-wiki.org/basic/insertion-sort/")
public class InsertionSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    @NotNull
    final S keySlot, scanSlot;

    public InsertionSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
        this.keySlot = comparator.newSlot();
        this.scanSlot = comparator.newSlot();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        long operationCount = 0L;
        int readIndex = startIndex;
        while (readIndex < endIndex) {
            comparator.load(target, readIndex, keySlot);
            int writeIndex = readIndex - 1;
            while (writeIndex >= startIndex) {
                comparator.load(target, writeIndex, scanSlot);
                if (comparator.compareSlot(keySlot, scanSlot) >= 0) {
                    break;
                }
                comparator.store(target, writeIndex + 1, scanSlot);
                writeIndex--;
                operationCount++;
            }
            comparator.store(target, writeIndex + 1, keySlot);
            readIndex++;
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull A target) {
        return sort(target, 0, comparator.length(target));
    }
}
