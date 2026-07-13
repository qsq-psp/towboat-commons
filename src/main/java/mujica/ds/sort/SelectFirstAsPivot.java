package mujica.ds.sort;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/19", name = "SelectFirstIntAsPivot")
@CodeHistory(date = "2025/12/24", name = "SelectFirstLongAsPivot")
@CodeHistory(date = "2026/7/11")
public class SelectFirstAsPivot<S, A> implements SelectPivot<S, A> {

    @NotNull
    final SlotArrayAllocator<S, A> allocator;

    public SelectFirstAsPivot(@NotNull SlotArrayAllocator<S, A> allocator) {
        super();
        this.allocator = allocator;
    }

    @Override
    public void selectPivot(@NotNull A target, int startIndex, int endIndex, @NotNull S out) {
        assert startIndex < endIndex;
        allocator.load(target, startIndex, out);
    }
}
