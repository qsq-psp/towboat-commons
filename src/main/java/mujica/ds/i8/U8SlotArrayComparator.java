package mujica.ds.i8;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

// there is no U8Arithmetic, only U8SlotArrayComparator
@CodeHistory(date = "2026/7/23", name = "SlotArrayComparatorU8")
@CodeHistory(date = "2026/8/27")
public class U8SlotArrayComparator implements SlotArrayComparator<U8, byte[]> {

    public static final U8SlotArrayComparator INSTANCE = new U8SlotArrayComparator();

    public U8SlotArrayComparator() {
        super();
    }

    @NotNull
    @Override
    public U8 newSlot() {
        return new U8();
    }

    @Override
    public void move(@NotNull U8 src, @NotNull U8 dst) {
        dst.value = src.value;
    }

    @NotNull
    @Override
    public U8 cloneSlot(@NotNull U8 original) {
        return new U8(original.value);
    }

    @Override
    public void exchange(@NotNull U8 first, @NotNull U8 second) {
        final byte temp = first.value;
        first.value = second.value;
        second.value = temp;
    }

    @NotNull
    @Override
    public byte[] newArray(int length) {
        if (length == 0) {
            return I8SlotArrayAllocator.EMPTY_ARRAY;
        }
        return new byte[length];
    }

    @Override
    public int length(@NotNull byte[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull byte[] array, int index, @NotNull U8 slot) {
        slot.value = array[index];
    }

    @Override
    public void store(@NotNull byte[] array, int index, @NotNull U8 slot) {
        array[index] = slot.value;
    }

    @Override
    public void exchange(@NotNull byte[] array, int firstIndex, int secondIndex) {
        final byte temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    @Override
    public byte[] cloneArray(@NotNull byte[] original) {
        if (original.length == 0) {
            return I8SlotArrayAllocator.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int compareSlot(@NotNull U8 left, @NotNull U8 right) {
        return (0xff & left.value) - (0xff & right.value);
    }

    @Override
    public int compareArray(@NotNull byte[] left, @NotNull byte[] right) {
        return Arrays.compareUnsigned(left, right);
    }

    @Override
    public void min(@NotNull U8 result) {
        result.value = 0;
    }

    @Override
    public void max(@NotNull U8 result) {
        result.value = (byte) 0xff;
    }

    @Override
    public void min(@NotNull U8 left, @NotNull U8 right, @NotNull U8 result) {
        result.value = (byte) Math.min((0xff & left.value), (0xff & right.value));
    }

    @Override
    public void max(@NotNull U8 left, @NotNull U8 right, @NotNull U8 result) {
        result.value = (byte) Math.max((0xff & left.value), (0xff & right.value));
    }

    @Override
    @NotNull
    public String toString() {
        return "[dataType = u8, slotType = U8Slot, arrayType = byte[]]";
    }
}
