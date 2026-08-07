package mujica.ds.i8;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorI8 implements SlotArrayAllocator<I8Slot, byte[]> {

    public static final SlotArrayAllocatorI8 INSTANCE = new SlotArrayAllocatorI8();

    public static final byte[] EMPTY_ARRAY = new byte[0];

    public SlotArrayAllocatorI8() {
        super();
    }

    @NotNull
    @Override
    public I8Slot newSlot() {
        return new I8();
    }

    @Override
    public void move(@NotNull I8Slot src, @NotNull I8Slot dst) {
        dst.setI8(src.getI8());
    }

    @NotNull
    @Override
    public I8Slot cloneSlot(@NotNull I8Slot original) {
        return new I8(original.getI8());
    }

    @Override
    public void exchange(@NotNull I8Slot a, @NotNull I8Slot b) {
        a.setI8(b.updateI8(a.getI8()));
    }

    @NotNull
    @Override
    public byte[] newArray(int length) {
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        return new byte[length];
    }

    @Override
    public int length(@NotNull byte[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull byte[] array, int index, @NotNull I8Slot slot) {
        slot.setI8(array[index]);
    }

    @Override
    public void store(@NotNull byte[] array, int index, @NotNull I8Slot slot) {
        array[index] = slot.getI8();
    }

    @Override
    public void exchange(@NotNull byte[] array, int index, @NotNull I8Slot slot) {
        array[index] = slot.updateI8(array[index]);
    }

    @Override
    public void exchange(@NotNull byte[] array, int firstIndex, int secondIndex) {
        final byte tempValue = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = tempValue;
    }

    @Override
    public void reverse(@NotNull byte[] array, int startIndex, int endIndex) {
        endIndex--;
        while (startIndex < endIndex) {
            exchange(array, startIndex, endIndex);
            startIndex++;
            endIndex--;
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
        return "SlotTool[dataType = i8, slotType = I8Slot, arrayType = byte[]]";
    }

    @CodeHistory(date = "2026/7/26")
    public static class WithHashI8 extends SlotArrayAllocatorI8 implements HashSAA<I8Slot, byte[]> {

        public static final WithHashI8 TWICE = new WithHashI8(TwiceCapacityPolicy.INSTANCE);

        @NotNull
        final CapacityPolicy capacityPolicy;

        public WithHashI8(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public boolean slotEquals(@NotNull I8Slot left, @NotNull I8Slot right) {
            return left.getI8() == right.getI8();
        }

        @Override
        public int slotHash(@NotNull I8Slot slot) {
            return slot.getI8();
        }
    }
}
