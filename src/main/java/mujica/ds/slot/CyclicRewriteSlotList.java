package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/3", name = "CyclicLookBackMemory")
@CodeHistory(date = "2025/11/26", name = "CyclicRunBuffer")
@CodeHistory(date = "2025/11/30", name = "CyclicArrayRunBuffer")
@CodeHistory(date = "2026/7/12")
public class CyclicRewriteSlotList<S, A> extends CyclicSlotList<S, A> {

    public CyclicRewriteSlotList(@NotNull SlotArrayAllocator.WithPolicy<S, A> allocator) {
        super(allocator);
    }

    CyclicRewriteSlotList(@NotNull SlotArrayAllocator.WithPolicy<S, A> allocator, @NotNull A array, int startIndex, int endIndex) {
        super(allocator, array, startIndex, endIndex);
    }
}
