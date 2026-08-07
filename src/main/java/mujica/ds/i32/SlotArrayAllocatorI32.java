package mujica.ds.i32;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorI32 implements SlotArrayAllocator<I32Slot, int[]> {

    public static final SlotArrayAllocatorI32 INSTANCE = new SlotArrayAllocatorI32();

    public static final int[] EMPTY_ARRAY = new int[0];

    public SlotArrayAllocatorI32() {
        super();
    }

    @NotNull
    @Override
    public I32Slot newSlot() {
        return new I32();
    }

    @Override
    public void move(@NotNull I32Slot src, @NotNull I32Slot dst) {
        dst.setI32(src.getI32());
    }

    @NotNull
    @Override
    public I32Slot cloneSlot(@NotNull I32Slot original) {
        return new I32(original.getI32());
    }

    @Override
    public void exchange(@NotNull I32Slot a, @NotNull I32Slot b) {
        a.setI32(b.updateI32(a.getI32()));
    }

    @NotNull
    @Override
    public int[] newArray(int length) {
        if (length == 0) {
            return EMPTY_ARRAY;
        }
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
    public void exchange(@NotNull int[] array, int firstIndex, int secondIndex) {
        final int tempValue = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = tempValue;
    }

    @Override
    public void reverse(@NotNull int[] array, int startIndex, int endIndex) {
        endIndex--;
        while (startIndex < endIndex) {
            exchange(array, startIndex, endIndex);
            startIndex++;
            endIndex--;
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
        return "SlotTool[dataType = i32, slotType = I32Slot, arrayType = int[]]";
    }

    @CodeHistory(date = "2026/7/16")
    public static class WithHashI32 extends SlotArrayAllocatorI32 implements HashSAA<I32Slot, int[]> {

        public static final WithHashI32 TWICE = new WithHashI32(TwiceCapacityPolicy.INSTANCE);

        @NotNull
        final CapacityPolicy capacityPolicy;

        public WithHashI32(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public boolean slotEquals(@NotNull I32Slot left, @NotNull I32Slot right) {
            return left.getI32() == right.getI32();
        }

        @Override
        public boolean arrayEquals(@NotNull int[] leftArray, @NotNull int[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }

        @Override
        public int slotHash(@NotNull I32Slot slot) {
            return slot.getI32();
        }

        @Override
        public int arrayHash(@NotNull int[] array) {
            return Arrays.hashCode(array);
        }

        @Override
        @NotNull
        public String toString() {
            return "SlotTool[dataType = i32, slotType = I32Slot, arrayType = int[], capacityPolicy = " + capacityPolicy + "]";
        }
    }
}
