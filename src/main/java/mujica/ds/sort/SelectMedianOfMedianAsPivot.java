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
        // todo
    }
}
