package mujica.ds.i32;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/14", name = "SlotArrayAllocatorI32ForTest")
@CodeHistory(date = "2026/8/18")
public class I32SlotArrayAllocatorForTest implements SlotArrayAllocator<I32Slot, int[]> {

    public static final int CANARY = 0xcdcdcdcd; // -842150451

    public I32SlotArrayAllocatorForTest() {
        super();
    }

    @NotNull
    @Override
    public I32Slot newSlot() {
        return new I32(CANARY);
    }

    @Override
    public void releaseSlot(@NotNull I32Slot slot) {
        slot.setI32(CANARY);
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
    public void exchange(@NotNull I32Slot first, @NotNull I32Slot second) {
        first.setI32(second.updateI32(first.getI32()));
    }

    @NotNull
    @Override
    public int[] newArray(int length) {
        final int[] array = new int[length];
        Arrays.fill(array, CANARY);
        return array;
    }

    @Override
    public void releaseArray(@NotNull int[] array) {
        Arrays.fill(array, CANARY);
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
    public void releaseReference(@NotNull int[] array, int index) {
        array[index] = CANARY;
    }

    @Override
    public void releaseReference(@NotNull int[] array, int startIndex, int endIndex) {
        Arrays.fill(array, startIndex, endIndex, CANARY);
    }

    @Override
    public void releaseReference(@NotNull int[] array) {
        Arrays.fill(array, CANARY);
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

    @Override
    public int[] cloneArray(@NotNull int[] original) {
        return Arrays.copyOf(original, original.length);
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = i32, slotType = I32Slot, arrayType = int[]]";
    }

    @CodeHistory(date = "2026/7/27")
    public static class I32HashSAA extends I32SlotArrayAllocatorForTest implements HashSAA<I32Slot, int[]> {

        @NotNull
        final CapacityPolicy capacityPolicy;

        public I32HashSAA(@NotNull CapacityPolicy capacityPolicy) {
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
