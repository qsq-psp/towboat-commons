package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/18", name = "SelectionSort")
@CodeHistory(date = "2026/2/10", name = "ListSelectionSort")
@CodeHistory(date = "2026/7/22")
@Name(value = "选择排序", language = "zh")
@ReferencePage(title = "选择排序", href = "https://oi-wiki.org/basic/selection-sort/")
public class SelectionSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    final S selectedSlot, scanSlot;

    public SelectionSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
        this.selectedSlot = comparator.newSlot();
        this.scanSlot = comparator.newSlot();
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < endIndex) {
            int selectedIndex = startIndex;
            comparator.load(target, startIndex, selectedSlot);
            for (int index = startIndex + 1; index < endIndex; index++) {
                comparator.load(target, index, scanSlot);
                if (comparator.compareSlot(selectedSlot, scanSlot) < 0) {
                    selectedIndex = index;
                    comparator.move(scanSlot, selectedSlot); // select max
                    operationCount++;
                }
            }
            endIndex--;
            if (selectedIndex != endIndex) {
                comparator.load(target, endIndex, scanSlot);
                comparator.store(target, selectedIndex, scanSlot);
                comparator.store(target, endIndex, selectedSlot);
            }
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull A target) {
        return sort(target, 0, comparator.length(target));
    }
}
