package mujica.ds.f32;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/15")
public class SlotArrayAllocatorF32 implements SlotArrayAllocator<F32Slot, float[]> {

    public static final float[] EMPTY_ARRAY = new float[0];

    public SlotArrayAllocatorF32() {
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
    public void exchange(@NotNull F32Slot a, @NotNull F32Slot b) {
        a.setF32(b.updateF32(a.getF32()));
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
}
