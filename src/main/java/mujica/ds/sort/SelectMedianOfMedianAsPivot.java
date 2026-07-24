package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/19", name = "SelectLongMedianOfMedianAsPivot")
@CodeHistory(date = "2026/7/13")
public class SelectMedianOfMedianAsPivot<S, A> implements SelectPivot<S, A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    public SelectMedianOfMedianAsPivot(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
    }

    @Override
    public void selectPivot(@NotNull A target, int startIndex, int endIndex, @NotNull S out) {
        final int intervalLength = endIndex - startIndex;
        if (intervalLength < 3) {
            comparator.load(target, startIndex, out);
            return;
        }
        final S first = comparator.newSlot();
        final S second = comparator.newSlot();
        final S third = comparator.newSlot();
        if (intervalLength < 6) {
            // 3 = 1 + 1 + 1
            // 4 = 1 + 1 + 2
            // 5 = 1 + 1 + 3
            comparator.load(target, startIndex, first);
            comparator.load(target, startIndex + 1, second);
            selectPivot(target, startIndex + 2, endIndex, third);
        } else if (intervalLength < 8) {
            // 6 = 1 + 2 + 3
            // 7 = 1 + 3 + 3
            comparator.load(target, startIndex, first);
            selectPivot(target, startIndex + 1, endIndex - 3, second);
            selectPivot(target, endIndex - 3, endIndex, third);
        } else {
            // 8 = 2 + 3 + 3
            // 8 = 3 + 3 + 3
            int leftIndex = startIndex + intervalLength / 3;
            int rightIndex = startIndex + intervalLength * 2 / 3;
            selectPivot(target, startIndex, leftIndex, first);
            selectPivot(target, leftIndex, rightIndex, second);
            selectPivot(target, rightIndex, endIndex, third);
        }
        comparator.median(first, second, third, out);
        comparator.releaseSlot(first);
        comparator.releaseSlot(second);
        comparator.releaseSlot(third);
    }
}
