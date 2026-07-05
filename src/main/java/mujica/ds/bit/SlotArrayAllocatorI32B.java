package mujica.ds.bit;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/26")
public class SlotArrayAllocatorI32B implements SlotArrayAllocator<BitSlot, int[]> {

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
    public int[] newArray(int length) {
        if (length < 0) {
            throw new NegativeArraySizeException();
        }
        length = (length + Integer.SIZE - 1) >>> 5;
        return new int[length + 1];
    }

    @Override
    public int length(@NotNull int[] array) {
        final int lastPosition = array.length - 1;
        if (lastPosition <= 0) {
            return 0;
        }
        final int lastBits = array[lastPosition];
        assert 0 < lastBits && lastBits <= Integer.SIZE;
        return (lastPosition << 5) - Integer.SIZE + lastBits;
    }

    @Override
    public void load(@NotNull int[] array, int index, @NotNull BitSlot slot) {
        final int secondLastPosition = array.length - 2;
        if (secondLastPosition < 0) {
            throw new IndexOutOfBoundsException();
        }
        final int arrayIndex = index >>> 5;
        index &= 0x1f; // now it's bitIndex
        if (arrayIndex < secondLastPosition || arrayIndex == secondLastPosition && index < array[secondLastPosition + 1]) {
            slot.setBit((array[arrayIndex] & (1 << index)) != 0);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void store(@NotNull int[] array, int index, @NotNull BitSlot slot) {
        final int secondLastPosition = array.length - 2;
        if (secondLastPosition < 0) {
            throw new IndexOutOfBoundsException();
        }
        final int arrayIndex = index >>> 5;
        index &= 0x1f; // now it's bitIndex
        if (arrayIndex < secondLastPosition || arrayIndex == secondLastPosition && index < array[secondLastPosition + 1]) {
            if (slot.getBit()) {
                array[arrayIndex] |= 1 << index;
            } else {
                array[arrayIndex] &= ~(1 << index);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void copy(@NotNull int[] src, int srcIndex, @NotNull int[] dst, int dstIndex, int length) {
        final BitSlot slot = newSlot();
        for (int index = 0; index < length; index++) {
            load(src, srcIndex++, slot);
            store(dst, dstIndex++, slot);
        }
    }
}
