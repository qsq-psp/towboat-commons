package mujica.ds.i8;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorI8 implements SlotArrayAllocator<I8Slot, byte[]> {

    @NotNull
    @Override
    public I8Slot newSlot() {
        return new I8();
    }

    @Override
    public void move(@NotNull I8Slot src, @NotNull I8Slot dst) {
        dst.setI8(src.getI8());
    }

    @NotNull
    @Override
    public I8Slot cloneSlot(@NotNull I8Slot original) {
        return new I8(original.getI8());
    }

    @Override
    public void exchange(@NotNull I8Slot a, @NotNull I8Slot b) {
        a.setI8(b.updateI8(a.getI8()));
    }

    @NotNull
    @Override
    public byte[] newArray(int length) {
        return new byte[length];
    }

    @Override
    public int length(@NotNull byte[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull byte[] array, int index, @NotNull I8Slot slot) {
        slot.setI8(array[index]);
    }

    @Override
    public void store(@NotNull byte[] array, int index, @NotNull I8Slot slot) {
        array[index] = slot.getI8();
    }

    @Override
    public void exchange(@NotNull byte[] array, int index, @NotNull I8Slot slot) {
        array[index] = slot.updateI8(array[index]);
    }

    @Override
    public void exchange(@NotNull byte[] array, int i, int j) {
        final byte temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
