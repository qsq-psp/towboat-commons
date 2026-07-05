package mujica.ds.i64;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorI64 implements SlotArrayAllocator<I64Slot, long[]> {

    @NotNull
    @Override
    public I64Slot newSlot() {
        return new S64();
    }

    @Override
    public void move(@NotNull I64Slot src, @NotNull I64Slot dst) {
        dst.setI64(src.getI64());
    }

    @NotNull
    @Override
    public long[] newArray(int length) {
        return new long[length];
    }

    @Override
    public void load(@NotNull long[] array, int index, @NotNull I64Slot slot) {
        slot.setI64(array[index]);
    }

    @Override
    public void store(@NotNull long[] array, int index, @NotNull I64Slot slot) {
        array[index] = slot.getI64();
    }
}
