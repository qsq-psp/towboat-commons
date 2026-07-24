package mujica.ds.i32;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/14")
public class SlotArrayAllocatorI32ForTest implements SlotArrayAllocator<I32Slot, int[]> {

    public static final int CANARY = 0xcdcdcdcd; // -842150451

    public SlotArrayAllocatorI32ForTest() {
        super();
    }

    @NotNull
    @Override
    public I32Slot newSlot() {
        return new S32(CANARY);
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
        return new S32(original.getI32());
    }

    @Override
    public void exchange(@NotNull I32Slot a, @NotNull I32Slot b) {
        a.setI32(b.updateI32(a.getI32()));
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
}
