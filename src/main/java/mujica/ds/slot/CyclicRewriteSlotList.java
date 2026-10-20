package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/3", name = "CyclicLookBackMemory")
@CodeHistory(date = "2025/11/26", name = "CyclicRunBuffer")
@CodeHistory(date = "2025/11/30", name = "CyclicArrayRunBuffer")
@CodeHistory(date = "2026/7/12")
@Name(value = "循环覆盖列表", language = "zh")
public class CyclicRewriteSlotList<S, A> extends CyclicSlotList<S, A> {

    public CyclicRewriteSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator) {
        super(allocator);
    }

    CyclicRewriteSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator, @NotNull A array, int startIndex, int endIndex) {
        super(allocator, array, startIndex, endIndex);
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        //
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        //
    }

    @Override
    public void insertLast(@NotNull A thatArray, int thatStartIndex, int thatEndIndex) {
        //
    }
}
