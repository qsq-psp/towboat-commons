package mujica.ds.i8;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

// there is no ArithmeticS8, only SlotArrayComparatorS8
@CodeHistory(date = "2026/7/24")
public class SlotArrayComparatorS8 implements SlotArrayComparator<S8, byte[]> {

    public SlotArrayComparatorS8() {
        super();
    }

    @NotNull
    @Override
    public S8 newSlot() {
        return new S8();
    }

    @Override
    public void move(@NotNull S8 src, @NotNull S8 dst) {
        dst.value = src.value;
    }

    @NotNull
    @Override
    public S8 cloneSlot(@NotNull S8 original) {
        return new S8(original.value);
    }

    @Override
    public void exchange(@NotNull S8 first, @NotNull S8 second) {
        final byte temp = first.value;
        first.value = second.value;
        second.value = temp;
    }

    @NotNull
    @Override
    public byte[] newArray(int length) {
        if (length == 0) {
            return SlotArrayAllocatorI8.EMPTY_ARRAY;
        }
        return new byte[length];
    }

    @Override
    public int length(@NotNull byte[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull byte[] array, int index, @NotNull S8 slot) {
        slot.value = array[index];
    }

    @Override
    public void store(@NotNull byte[] array, int index, @NotNull S8 slot) {
        array[index] = slot.value;
    }

    @Override
    public void exchange(@NotNull byte[] array, int index, @NotNull S8 slot) {
        array[index] = slot.updateI8(array[index]);
    }

    @Override
    public void exchange(@NotNull byte[] array, int i, int j) {
        final byte temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public byte[] cloneArray(@NotNull byte[] original) {
        if (original.length == 0) {
            return SlotArrayAllocatorI8.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int compareSlot(@NotNull S8 left, @NotNull S8 right) {
        return left.value - right.value;
    }

    @Override
    public int compareArray(@NotNull byte[] left, @NotNull byte[] right) {
        return Arrays.compare(left, right);
    }

    @Override
    public void min(@NotNull S8 result) {
        result.value = Byte.MIN_VALUE;
    }

    @Override
    public void max(@NotNull S8 result) {
        result.value = Byte.MAX_VALUE;
    }

    @Override
    public void min(@NotNull S8 left, @NotNull S8 right, @NotNull S8 result) {
        result.value = (byte) Math.min(left.value, right.value);
    }

    @Override
    public void max(@NotNull S8 left, @NotNull S8 right, @NotNull S8 result) {
        result.value = (byte) Math.max(left.value, right.value);
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = s8, slotType = S8Slot, arrayType = byte[]]";
    }
}
