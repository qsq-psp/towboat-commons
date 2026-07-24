package mujica.ds.bit;

import mujica.ds.i8.SlotArrayAllocatorI8;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/26")
public class SlotArrayAllocatorI8B implements SlotArrayAllocator<BitSlot, byte[]> {

    public static final byte[] EMPTY_ARRAY = SlotArrayAllocatorI8.EMPTY_ARRAY;

    public SlotArrayAllocatorI8B() {
        super();
    }

    @NotNull
    @Override
    public BitSlot newSlot() {
        return new Bit();
    }

    @Override
    public void move(@NotNull BitSlot src, @NotNull BitSlot dst) {
        dst.setBit(src.getBit());
    }

    @NotNull
    @Override
    public BitSlot cloneSlot(@NotNull BitSlot original) {
        return new Bit(original.getBit());
    }

    @Override
    public void exchange(@NotNull BitSlot a, @NotNull BitSlot b) {
        a.setBit(b.updateBit(a.getBit()));
    }

    @NotNull
    @Override
    public byte[] newArray(int length) {
        if (length <= 0) {
            if (length == 0) {
                return EMPTY_ARRAY;
            }
            throw new NegativeArraySizeException();
        }
        length = (length + Byte.SIZE - 1) >>> 3;
        return new byte[length + 1];
    }

    @Override
    public int length(@NotNull byte[] array) {
        final int lastPosition = array.length - 1;
        if (lastPosition <= 0) {
            return 0;
        }
        final int lastBits = array[lastPosition];
        assert 0 < lastBits && lastBits <= Byte.SIZE;
        return (lastPosition << 3) - Byte.SIZE + lastBits;
    }

    @Override
    public void load(@NotNull byte[] array, int index, @NotNull BitSlot slot) {
        final int secondLastPosition = array.length - 2;
        if (secondLastPosition < 0) {
            throw new IndexOutOfBoundsException();
        }
        final int arrayIndex = index >>> 3;
        index &= 0x7; // now it's bitIndex
        if (arrayIndex < secondLastPosition || arrayIndex == secondLastPosition && index < array[secondLastPosition + 1]) {
            slot.setBit((array[arrayIndex] & (1 << index)) != 0);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void store(@NotNull byte[] array, int index, @NotNull BitSlot slot) {
        final int secondLastPosition = array.length - 2;
        if (secondLastPosition < 0) {
            throw new IndexOutOfBoundsException();
        }
        final int arrayIndex = index >>> 3;
        index &= 0x7; // now it's bitIndex
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
    public void copy(@NotNull byte[] src, int srcIndex, @NotNull byte[] dst, int dstIndex, int length) {
        final BitSlot slot = newSlot();
        for (int index = 0; index < length; index++) {
            load(src, srcIndex++, slot);
            store(dst, dstIndex++, slot);
        }
    }

    @Override
    public byte[] cloneArray(@NotNull byte[] original) {
        if (original.length == 0) {
            return EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = boolean, slotType = BitSlot, arrayType = byte[]]";
    }
}
