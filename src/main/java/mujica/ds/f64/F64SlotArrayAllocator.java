package mujica.ds.f64;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/24", name = "SlotArrayAllocatorF64")
@CodeHistory(date = "2026/8/12")
public class F64SlotArrayAllocator implements SlotArrayAllocator<F64Slot, double[]> {

    private static final long serialVersionUID = 0x83c146a13b925ff7L;

    public static final F64SlotArrayAllocator INSTANCE = new F64SlotArrayAllocator();

    public static final double[] EMPTY_ARRAY = new double[0];

    @NotNull
    @Override
    public F64Slot newSlot() {
        return new F64();
    }

    @Override
    public void move(@NotNull F64Slot src, @NotNull F64Slot dst) {
        dst.setF64(src.getF64());
    }

    @NotNull
    @Override
    public F64Slot cloneSlot(@NotNull F64Slot original) {
        return new F64(original.getF64());
    }

    @Override
    public void exchange(@NotNull F64Slot first, @NotNull F64Slot second) {
        first.setF64(second.updateF64(first.getF64()));
    }

    @NotNull
    @Override
    public double[] newArray(int length) {
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        return new double[length];
    }

    @Override
    public int length(@NotNull double[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull double[] array, int index, @NotNull F64Slot slot) {
        slot.setF64(array[index]);
    }

    @Override
    public void store(@NotNull double[] array, int index, @NotNull F64Slot slot) {
        array[index] = slot.getF64();
    }

    @Override
    public void exchange(@NotNull double[] array, int index, @NotNull F64Slot slot) {
        array[index] = slot.updateF64(array[index]);
    }

    @Override
    public void exchange(@NotNull double[] array, int firstIndex, int secondIndex) {
        final double temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    @Override
    public void reverse(@NotNull double[] array, int startIndex, int endIndex) {
        endIndex--;
        while (startIndex < endIndex) {
            exchange(array, startIndex, endIndex);
            startIndex++;
            endIndex--;
        }
    }

    @Override
    public void copyWithin(@NotNull double[] array, int srcPosition, int dstPosition, int length) {
        System.arraycopy(array, srcPosition, array, dstPosition, length);
    }

    @Override
    public void copy(@NotNull double[] src, int srcPosition, @NotNull double[] dst, int dstPosition, int length) {
        System.arraycopy(src, srcPosition, dst, dstPosition, length);
    }

    @Override
    public double[] cloneArray(@NotNull double[] original) {
        if (original.length == 0) {
            return EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    @NotNull
    public String toString() {
        return "[dataType = f64, slotType = F64Slot, arrayType = double[]]";
    }

    @CodeHistory(date = "2026/8/11")
    public static class F64PolicySAA extends F64SlotArrayAllocator implements HashSAA<F64Slot, double[]> {

        private static final long serialVersionUID = 0xc99f6fc4c72a8fa4L;

        @NotNull
        final CapacityPolicy capacityPolicy;

        public F64PolicySAA(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public boolean slotEquals(@NotNull F64Slot left, @NotNull F64Slot right) {
            return Double.doubleToLongBits(left.getF64()) == Double.doubleToLongBits(right.getF64());
        }

        @Override
        public boolean arrayEquals(@NotNull double[] leftArray, @NotNull double[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }

        @Override
        public int slotHash(@NotNull F64Slot slot) {
            return Double.hashCode(slot.getF64());
        }

        @Override
        public int arrayHash(@NotNull double[] array) {
            return Arrays.hashCode(array);
        }

        @Override
        @NotNull
        public String toString() {
            return "[dataType = f64, slotType = F64Slot, arrayType = double[], capacityPolicy = " + capacityPolicy + "]";
        }
    }
}
