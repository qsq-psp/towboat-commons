package mujica.ds.bit;

import mujica.ds.i32.I32SlotArrayAllocator;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/26")
public class SlotArrayAllocatorI32B implements SlotArrayAllocator<BitSlot, int[]> {

    public static final SlotArrayAllocatorI32B INSTANCE = new SlotArrayAllocatorI32B();

    public static final int[] EMPTY_ARRAY = I32SlotArrayAllocator.EMPTY_ARRAY;

    public SlotArrayAllocatorI32B() {
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
    public void exchange(@NotNull BitSlot first, @NotNull BitSlot second) {
        first.setBit(second.updateBit(first.getBit()));
    }

    @NotNull
    @Override
    public int[] newArray(int bitCount) {
        if (bitCount <= 0) {
            if (bitCount == 0) {
                return EMPTY_ARRAY;
            }
            throw new NegativeArraySizeException();
        }
        final int intCount = (bitCount + Integer.SIZE - 1) >>> 5;
        final int lastBits = bitCount - ((intCount - 1) << 5);
        assert 0 < lastBits && lastBits <= Integer.SIZE;
        final int[] array = new int[intCount + 1];
        array[intCount] = lastBits;
        return array;
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
    public void copyWithin(@NotNull int[] array, int srcPosition, int dstPosition, int length) {
        final BitSlot tempSlot = newSlot();
        try {
            if (srcPosition < dstPosition) {
                for (int index = length - 1; index >= 0; index--) {
                    load(array, srcPosition + index, tempSlot);
                    store(array, dstPosition + index, tempSlot);
                }
            } else if (srcPosition > dstPosition) {
                for (int index = 0; index < length; index++) {
                    load(array, srcPosition + index, tempSlot);
                    store(array, dstPosition + index, tempSlot);
                }
            }
        } finally {
            releaseSlot(tempSlot);
        }
    }

    @Override
    public void copy(@NotNull int[] src, int srcPosition, @NotNull int[] dst, int dstPosition, int length) {
        final BitSlot tempSlot = newSlot();
        try {
            if (src == dst && srcPosition < dstPosition) {
                for (int index = length - 1; index >= 0; index--) {
                    load(src, srcPosition + index, tempSlot);
                    store(dst, dstPosition + index, tempSlot);
                }
            } else {
                for (int index = 0; index < length; index++) {
                    load(src, srcPosition + index, tempSlot);
                    store(dst, dstPosition + index, tempSlot);
                }
            }
        } finally {
            releaseSlot(tempSlot);
        }
    }

    @Override
    public int[] cloneArray(@NotNull int[] original) {
        if (original.length == 0) {
            return EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = boolean, slotType = BitSlot, arrayType = int[]]";
    }
}
