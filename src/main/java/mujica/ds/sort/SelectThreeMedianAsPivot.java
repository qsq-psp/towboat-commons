package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/21", name = "SelectThreeIntMedianAsPivot")
@CodeHistory(date = "2025/12/25", name = "SelectThreeLongMedianAsPivot")
@CodeHistory(date = "2026/7/12")
public class SelectThreeMedianAsPivot<S, A> implements SelectPivot<S, A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    @NotNull
    final S first, second, third;

    public SelectThreeMedianAsPivot(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
        this.first = comparator.newSlot();
        this.second = comparator.newSlot();
        this.third = comparator.newSlot();
    }

    @Override
    public void selectPivot(@NotNull A target, int startIndex, int endIndex, @NotNull S out) {
        if (endIndex - startIndex >= 3) {
            comparator.load(target, startIndex, first);
            comparator.load(target, startIndex + 1, second);
            comparator.load(target, startIndex + 2, third);
            comparator.median(first, second, third, out);
        } else {
            assert startIndex < endIndex;
            comparator.load(target, startIndex, out);
        }
    }
}
