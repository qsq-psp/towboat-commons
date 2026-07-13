package mujica.ds.sort;

import mujica.ds.slot.Arithmetic;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;

@CodeHistory(date = "2026/5/14", name = "SelectLongRangeCenterAsPivot")
@CodeHistory(date = "2026/7/13")
public class SelectRangeCenterAsPivot<S, A> implements SelectPivot<S, A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    @NotNull
    final Arithmetic<S> arithmetic;

    @NotNull
    final RoundingMode roundingMode;

    @NotNull
    final S slot, min, max;

    public SelectRangeCenterAsPivot(@NotNull SlotArrayComparator<S, A> comparator, @NotNull Arithmetic<S> arithmetic, @NotNull RoundingMode roundingMode) {
        super();
        this.comparator = comparator;
        this.arithmetic = arithmetic;
        this.roundingMode = roundingMode;
        this.slot = comparator.newSlot();
        this.min = comparator.newSlot();
        this.max = comparator.newSlot();
    }

    public SelectRangeCenterAsPivot(@NotNull SlotArrayComparator<S, A> comparator, @NotNull Arithmetic<S> arithmetic) {
        this(comparator, arithmetic, RoundingMode.DOWN);
    }

    @Override
    public void selectPivot(@NotNull A target, int startIndex, int endIndex, @NotNull S out) {
        assert startIndex < endIndex;
        comparator.load(target, startIndex, min);
        comparator.move(min, max);
        for (int index = startIndex + 1; index < endIndex; index++) {
            comparator.load(target, startIndex, slot);
            if (comparator.compareSlot(slot, min) < 0) {
                comparator.move(slot, min);
            }
            if (comparator.compareSlot(slot, max) > 0) {
                comparator.move(slot, max);
            }
        }
        arithmetic.mean(min, max, out, roundingMode);
    }
}
