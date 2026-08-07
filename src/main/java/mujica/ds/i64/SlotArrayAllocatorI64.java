package mujica.ds.i64;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorI64 implements SlotArrayAllocator<I64Slot, long[]> {

    public static final SlotArrayAllocatorI64 INSTANCE = new SlotArrayAllocatorI64();

    public static final long[] EMPTY_ARRAY = new long[0];

    public SlotArrayAllocatorI64() {
        super();
    }

    @NotNull
    @Override
    public I64Slot newSlot() {
        return new I64();
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
    public void exchange(@NotNull I64Slot a, @NotNull I64Slot b) {
        a.setI64(b.updateI64(a.getI64()));
    }

    @NotNull
    @Override
    public long[] newArray(int length) {
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        return new long[length];
    }

    @Override
    public int length(@NotNull long[] array) {
        return array.length;
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
    public void exchange(@NotNull long[] array, int index, @NotNull I64Slot slot) {
        array[index] = slot.updateI64(array[index]);
    }

    @Override
    public void exchange(@NotNull long[] array, int firstIndex, int secondIndex) {
        final long temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    @Override
    public void reverse(@NotNull long[] array, int startIndex, int endIndex) {
        endIndex--;
        while (startIndex < endIndex) {
            exchange(array, startIndex, endIndex);
            startIndex++;
            endIndex--;
        }
    }

    @Override
    public long[] cloneArray(@NotNull long[] original) {
        if (original.length == 0) {
            return EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = i64, slotType = I64Slot, arrayType = long[]]";
    }

    @CodeHistory(date = "2026/7/23")
    public static class WithHashI64 extends SlotArrayAllocatorI64 implements HashSAA<I64Slot, long[]> {

        public static final WithHashI64 TWICE = new WithHashI64(TwiceCapacityPolicy.INSTANCE);

        @NotNull
        final CapacityPolicy capacityPolicy;

        public WithHashI64(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
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
