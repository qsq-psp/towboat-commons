package mujica.ds.f32;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/15", name = "SlotArrayAllocatorF32")
@CodeHistory(date = "2026/8/11")
public class F32SlotArrayAllocator implements SlotArrayAllocator<F32Slot, float[]> {

    private static final long serialVersionUID = 0x8F4B233BB897FEBBL;

    public static final float[] EMPTY_ARRAY = new float[0];

    public F32SlotArrayAllocator() {
        super();
    }

    @NotNull
    @Override
    public F32Slot newSlot() {
        return new F32();
    }

    @Override
    public void move(@NotNull F32Slot src, @NotNull F32Slot dst) {
        dst.setF32(src.getF32());
    }

    @NotNull
    @Override
    public F32Slot cloneSlot(@NotNull F32Slot original) {
        return new F32(original.getF32());
    }

    @Override
    public void exchange(@NotNull F32Slot first, @NotNull F32Slot second) {
        first.setF32(second.updateF32(first.getF32()));
    }

    @NotNull
    @Override
    public float[] newArray(int length) {
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        return new float[length];
    }

    @Override
    public int length(@NotNull float[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull float[] array, int index, @NotNull F32Slot slot) {
        slot.setF32(array[index]);
    }

    @Override
    public void store(@NotNull float[] array, int index, @NotNull F32Slot slot) {
        array[index] = slot.getF32();
    }

    @Override
    public void exchange(@NotNull float[] array, int index, @NotNull F32Slot slot) {
        array[index] = slot.updateF32(array[index]);
    }

    @Override
    public void exchange(@NotNull float[] array, int i, int j) {
        final float temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public void reverse(@NotNull float[] array, int startIndex, int endIndex) {
        endIndex--;
        while (startIndex < endIndex) {
            exchange(array, startIndex, endIndex);
            startIndex++;
            endIndex--;
        }
    }

    @Override
    public void copyWithin(@NotNull float[] array, int srcPosition, int dstPosition, int length) {
        System.arraycopy(array, srcPosition, array, dstPosition, length);
    }

    @Override
    public void copy(@NotNull float[] src, int srcPosition, @NotNull float[] dst, int dstPosition, int length) {
        System.arraycopy(src, srcPosition, dst, dstPosition, length);
    }

    @Override
    public float[] cloneArray(@NotNull float[] original) {
        if (original.length == 0) {
            return EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = f32, slotType = F32Slot, arrayType = float[]]";
    }

    @CodeHistory(date = "2026/7/26", name = "WithHashF32")
    @CodeHistory(date = "2026/8/11")
    public static class F32HashSAA extends F32SlotArrayAllocator implements HashSAA<F32Slot, float[]> {

        private static final long serialVersionUID = 0x41412F6D04D2F798L;

        @NotNull
        final CapacityPolicy capacityPolicy;

        public F32HashSAA(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public boolean slotEquals(@NotNull F32Slot left, @NotNull F32Slot right) {
            return Float.floatToIntBits(left.getF32()) == Float.floatToIntBits(right.getF32()); // raw int bits not used
        }

        @Override
        public boolean arrayEquals(@NotNull float[] leftArray, @NotNull float[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }

        @Override
        public int slotHash(@NotNull F32Slot slot) {
            return Float.floatToIntBits(slot.getF32()); // raw int bits not used, too
        }

        @Override
        public int arrayHash(@NotNull float[] array) {
            return Arrays.hashCode(array);
        }

        @Override
        @NotNull
        public String toString() {
            return "SlotTool[dataType = f32, slotType = F32Slot, arrayType = float[], capacityPolicy = " + capacityPolicy + "]";
        }
    }
}
