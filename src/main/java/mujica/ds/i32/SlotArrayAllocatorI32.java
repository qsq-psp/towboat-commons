package mujica.ds.i32;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorI32 implements SlotArrayAllocator<I32Slot, int[]> {

    @NotNull
    @Override
    public I32Slot newSlot() {
        return new S32();
    }

    @Override
    public void move(@NotNull I32Slot src, @NotNull I32Slot dst) {
        dst.setI32(src.getI32());
    }

    @NotNull
    @Override
    public I32Slot clone(@NotNull I32Slot original) {
        return new S32(original.getI32());
    }

    @Override
    public void exchange(@NotNull I32Slot a, @NotNull I32Slot b) {
        a.setI32(b.updateI32(a.getI32()));
    }

    @NotNull
    @Override
    public int[] newArray(int length) {
        return new int[length];
    }

    @Override
    public int length(@NotNull int[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull int[] array, int index, @NotNull I32Slot slot) {
        slot.setI32(array[index]);
    }

    @Override
    public void store(@NotNull int[] array, int index, @NotNull I32Slot slot) {
        array[index] = slot.getI32();
    }

    @Override
    public void exchange(@NotNull int[] array, int index, @NotNull I32Slot slot) {
        array[index] = slot.updateI32(array[index]);
    }

    @Override
    public void exchange(@NotNull int[] array, int i, int j) {
        final int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
