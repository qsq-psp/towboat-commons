package mujica.ds.bit;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/26")
public class SlotArrayAllocatorI8B implements SlotArrayAllocator<BitSlot, byte[]> {

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
    public byte[] newArray(int length) {
        if (length < 0) {
            throw new NegativeArraySizeException();
        }
        length = (length + Byte.SIZE - 1) >>> 3;
        return new byte[length + 1];
    }

    @Override
    public void load(@NotNull byte[] array, int index, @NotNull BitSlot slot) {
        // todo
    }

    @Override
    public void store(@NotNull byte[] array, int index, @NotNull BitSlot slot) {
        // todo
    }
}
