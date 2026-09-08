package mujica.ds.i64;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/14", name = "SlotArrayAllocatorI64ForTest")
@CodeHistory(date = "2026/8/30")
public class I64SlotArrayAllocatorForTest implements SlotArrayAllocator<I64Slot, long[]> {

    static final long CANARY = 0xcdcdcdcdcdcdcdcdL;

    @NotNull
    @Override
    public I64Slot newSlot() {
        return new I64(CANARY);
    }

    @Override
    public void releaseSlot(@NotNull I64Slot slot) {
        slot.setI64(CANARY);
    }

    @Override
    public void move(@NotNull I64Slot src, @NotNull I64Slot dst) {
        dst.setI64(src.getI64());
    }

    @NotNull
    @Override
    public I64Slot cloneSlot(@NotNull I64Slot original) {
        return new I64(original.getI64());
    }

    @Override
    public void exchange(@NotNull I64Slot first, @NotNull I64Slot second) {
        first.setI64(second.updateI64(first.getI64()));
    }

    @NotNull
    @Override
    public long[] newArray(int length) {
        final long[] array = new long[length];
        Arrays.fill(array, CANARY);
        return array;
    }

    @Override
    public void releaseArray(@NotNull long[] array) {
        Arrays.fill(array, CANARY);
    }

    @Override
    public void load(@NotNull long[] array, int index, @NotNull I64Slot slot) {
        slot.setI64(array[index]);
    }

    @Override
    public void store(@NotNull long[] array, int index, @NotNull I64Slot slot) {
        array[index] = slot.getI64();
    }

    @Override
    public void releaseReference(@NotNull long[] array, int index) {
        array[index] = CANARY;
    }

    @Override
    public void releaseReference(@NotNull long[] array, int startIndex, int endIndex) {
        Arrays.fill(array, startIndex, endIndex, CANARY);
    }

    @Override
    public void releaseReference(@NotNull long[] array) {
        Arrays.fill(array, CANARY);
    }

    @Override
    public void exchange(@NotNull long[] array, int index, @NotNull I64Slot slot) {
        array[index] = slot.updateI64(array[index]);
    }

    @Override
    public void exchange(@NotNull long[] array, int i, int j) {
        final long temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public long[] cloneArray(@NotNull long[] original) {
        return Arrays.copyOf(original, original.length);
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = i64, slotType = I64Slot, arrayType = long[]]";
    }

    @CodeHistory(date = "2026/8/5")
    public static class I64HashSAA extends I64SlotArrayAllocatorForTest implements HashSAA<I64Slot, long[]> {

        @NotNull
        final CapacityPolicy capacityPolicy;

        public I64HashSAA(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @Override
        @NotNull
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public boolean slotEquals(@NotNull I64Slot left, @NotNull I64Slot right) {
            return left.getI64() == right.getI64();
        }

        @Override
        public boolean arrayEquals(@NotNull long[] leftArray, @NotNull long[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }

        @Override
        public int slotHash(@NotNull I64Slot slot) {
            return Long.hashCode(slot.getI64());
        }

        @Override
        public int arrayHash(@NotNull long[] array) {
            return Arrays.hashCode(array);
        }

        @Override
        @NotNull
        public String toString() {
            return "SlotTool[dataType = i64, slotType = I64Slot, arrayType = long[], capacityPolicy = " + capacityPolicy + "]";
        }
    }
}
