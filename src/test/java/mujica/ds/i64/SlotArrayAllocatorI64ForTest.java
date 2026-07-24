package mujica.ds.i64;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/14")
public class SlotArrayAllocatorI64ForTest implements SlotArrayAllocator<I64Slot, long[]> {

    static final long CANARY = 0xcdcdcdcdcdcdcdcdL;

    @NotNull
    @Override
    public I64Slot newSlot() {
        return new S64(CANARY);
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
        return new S64(original.getI64());
    }

    @Override
    public void exchange(@NotNull I64Slot a, @NotNull I64Slot b) {
        a.setI64(b.updateI64(a.getI64()));
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
}
