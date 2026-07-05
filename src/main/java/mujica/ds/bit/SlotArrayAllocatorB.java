package mujica.ds.bit;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorB implements SlotArrayAllocator<BitSlot, boolean[]> {

    @NotNull
    @Override
    public BitSlot newSlot() {
        return new PublicBitSlot();
    }

    @Override
    public void move(@NotNull BitSlot src, @NotNull BitSlot dst) {
        dst.setBit(src.getBit());
    }

    @NotNull
    @Override
    public BitSlot clone(@NotNull BitSlot original) {
        return new PublicBitSlot(original.getBit());
    }

    @Override
    public void exchange(@NotNull BitSlot a, @NotNull BitSlot b) {
        a.setBit(b.updateBit(a.getBit()));
    }

    @NotNull
    @Override
    public boolean[] newArray(int length) {
        return new boolean[length];
    }

    @Override
    public int length(@NotNull boolean[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull boolean[] array, int index, @NotNull BitSlot slot) {
        slot.setBit(array[index]);
    }

    @Override
    public void store(@NotNull boolean[] array, int index, @NotNull BitSlot slot) {
        array[index] = slot.getBit();
    }

    @Override
    public void exchange(@NotNull boolean[] array, int index, @NotNull BitSlot slot) {
        array[index] = slot.updateBit(array[index]);
    }

    @Override
    public void exchange(@NotNull boolean[] array, int i, int j) {
        final boolean temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
