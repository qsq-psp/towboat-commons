package mujica.ds.f64;

import mujica.ds.f32.F32Slot;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/24")
public class SlotArrayAllocatorF64 implements SlotArrayAllocator<F64Slot, double[]> {

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
    public void exchange(@NotNull double[] array, int i, int j) {
        final double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
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
        return "SlotTool[dataType = f64, slotType = F64Slot, arrayType = double[]]";
    }
}
