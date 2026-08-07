package mujica.ds.bit;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorB implements SlotArrayAllocator<BitSlot, boolean[]> {

    public static final SlotArrayAllocatorB INSTANCE = new SlotArrayAllocatorB();

    public static final boolean[] EMPTY_ARRAY = new boolean[0];

    public SlotArrayAllocatorB() {
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
    public boolean[] newArray(int length) {
        if (length == 0) {
            return EMPTY_ARRAY;
        }
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

    @Override
    public boolean[] cloneArray(@NotNull boolean[] original) {
        if (original.length == 0) {
            return EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = boolean, slotType = BitSlot, arrayType = boolean[]]";
    }

    @CodeHistory(date = "2026/7/23")
    public static class WithHashB extends SlotArrayAllocatorB implements HashSAA<BitSlot, boolean[]> {

        @NotNull
        final CapacityPolicy capacityPolicy;

        public WithHashB(@NotNull CapacityPolicy capacityPolicy) {
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
        public boolean arrayEquals(@NotNull boolean[] leftArray, @NotNull boolean[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }

        @Override
        public int slotHash(@NotNull BitSlot slot) {
            return Boolean.hashCode(slot.getBit());
        }

        @Override
        public int arrayHash(@NotNull boolean[] array) {
            return Arrays.hashCode(array);
        }

        @Override
        @NotNull
        public String toString() {
            return "SlotTool[dataType = boolean, slotType = BitSlot, arrayType = boolean[], capacityPolicy = " + capacityPolicy + "]";
        }
    }
}
