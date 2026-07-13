package mujica.ds.sort;

import mujica.algebra.random.RandomContext;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/28", name = "SelectRandomIntAsPivot")
@CodeHistory(date = "2026/7/13")
public class SelectRandomAsPivot<S, A> implements SelectPivot<S, A> {

    @NotNull
    final SlotArrayAllocator<S, A> allocator;

    @NotNull
    final RandomContext rc;

    public SelectRandomAsPivot(@NotNull SlotArrayAllocator<S, A> allocator, @NotNull RandomContext rc) {
        super();
        this.allocator = allocator;
        this.rc = rc;
    }

    @Override
    public void selectPivot(@NotNull A target, int startIndex, int endIndex, @NotNull S out) {
        allocator.load(target, rc.nextInt(startIndex, endIndex), out);
    }
}
