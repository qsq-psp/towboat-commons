package mujica.ds.bit;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i8.I8SlotArrayAllocator;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/6/26")
public class SlotArrayAllocatorI8B implements SlotArrayAllocator<BitSlot, byte[]> {

    public static final SlotArrayAllocatorI8B INSTANCE = new SlotArrayAllocatorI8B();

    public static final byte[] EMPTY_ARRAY = I8SlotArrayAllocator.EMPTY_ARRAY;

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
    public void exchange(@NotNull BitSlot first, @NotNull BitSlot second) {
        first.setBit(second.updateBit(first.getBit()));
    }

    @NotNull
    @Override
    public byte[] newArray(int bitCount) {
        if (bitCount <= 0) {
            if (bitCount == 0) {
                return EMPTY_ARRAY;
            }
            throw new NegativeArraySizeException();
        }
        final int byteCount = (bitCount + Byte.SIZE - 1) >>> 3;
        final int lastBits = bitCount - ((byteCount - 1) << 3);
        assert 0 < lastBits && lastBits <= Byte.SIZE;
        final byte[] array = new byte[byteCount + 1];
        array[byteCount] = (byte) lastBits;
        return array;
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
    public void copyWithin(@NotNull byte[] array, int srcPosition, int dstPosition, int length) {
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
    public void copy(@NotNull byte[] src, int srcPosition, @NotNull byte[] dst, int dstPosition, int length) {
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

    @CodeHistory(date = "2026/7/29")
    public static class WithHashI8B extends SlotArrayAllocatorI8B implements HashSAA<BitSlot, byte[]> {

        @NotNull
        final CapacityPolicy capacityPolicy;

        public WithHashI8B(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public boolean slotEquals(@NotNull BitSlot left, @NotNull BitSlot right) {
            return left.getBit() == right.getBit();
        }

        @Override
        public boolean arrayEquals(@NotNull byte[] leftArray, @NotNull byte[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }

        @Override
        public int slotHash(@NotNull BitSlot slot) {
            return Boolean.hashCode(slot.getBit());
        }

        @Override
        @NotNull
        public String toString() {
            return "SlotTool[dataType = boolean, slotType = BitSlot, arrayType = byte[], capacityPolicy = " + capacityPolicy + "]";
        }
    }
}
