package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/11", name = "ListHeapSort")
@CodeHistory(date = "2026/7/25")
public class HeapSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    public HeapSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        return 0;
    }
}
