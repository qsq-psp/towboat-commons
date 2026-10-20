package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/9/13")
public class ShakeSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    final S leftSlot, rightSlot;

    public ShakeSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
        this.leftSlot = comparator.newSlot();
        this.rightSlot = comparator.newSlot();
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        return 0;
    }
}
