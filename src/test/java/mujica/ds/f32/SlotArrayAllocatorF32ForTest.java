package mujica.ds.f32;

import mujica.ds.i32.SlotArrayAllocatorI32ForTest;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/15")
public class SlotArrayAllocatorF32ForTest implements SlotArrayAllocator<F32Slot, float[]> {

    public static final float CANARY = Float.intBitsToFloat(SlotArrayAllocatorI32ForTest.CANARY);

    public SlotArrayAllocatorF32ForTest() {
        super();
    }

    @NotNull
    @Override
    public F32Slot newSlot() {
        return new F32(CANARY);
    }

    @Override
    public void releaseSlot(@NotNull F32Slot slot) {
        slot.setF32(CANARY);
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
        final float[] array = new float[length];
        Arrays.fill(array, CANARY);
        return array;
    }

    @Override
    public void releaseArray(@NotNull float[] array) {
        Arrays.fill(array, CANARY);
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
    public void releaseReference(@NotNull float[] array, int index) {
        array[index] = CANARY;
    }

    @Override
    public void releaseReference(@NotNull float[] array, int startIndex, int endIndex) {
        Arrays.fill(array, startIndex, endIndex, CANARY);
    }

    @Override
    public void releaseReference(@NotNull float[] array) {
        Arrays.fill(array, CANARY);
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
        return Arrays.copyOf(original, original.length);
    }
}
