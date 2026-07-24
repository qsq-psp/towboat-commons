package mujica.ds.i8;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

// there is no ArithmeticU8, only SlotArrayComparatorU8
@CodeHistory(date = "2026/7/23")
public class SlotArrayComparatorU8 extends SlotArrayAllocatorI8 implements SlotArrayComparator<I8Slot, byte[]> {

    public SlotArrayComparatorU8() {
        super();
    }

    @Override
    public int compareSlot(@NotNull I8Slot left, @NotNull I8Slot right) {
        return (0xff & left.getI8()) - (0xff & right.getI8());
    }

    @Override
    public int compareArray(@NotNull byte[] left, @NotNull byte[] right) {
        return Arrays.compareUnsigned(left, right);
    }

    @Override
    public void min(@NotNull I8Slot result) {
        result.setI8((byte) 0);
    }

    @Override
    public void max(@NotNull I8Slot result) {
        result.setI8((byte) 0xff);
    }

    @Override
    public void min(@NotNull I8Slot left, @NotNull I8Slot right, @NotNull I8Slot result) {
        result.setI8((byte) Math.min(0xff & left.getI8(), 0xff & right.getI8()));
    }

    @Override
    public void max(@NotNull I8Slot left, @NotNull I8Slot right, @NotNull I8Slot result) {
        result.setI8((byte) Math.max(0xff & left.getI8(), 0xff & right.getI8()));
    }
}
