package mujica.ds.i32;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.slot.PrimitiveArithmetic;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.Arrays;

@CodeHistory(date = "2026/6/29", name = "ArithmeticU32")
@CodeHistory(date = "2026/8/7")
public class U32Arithmetic extends PrimitiveArithmetic<U32, int[]> {

    private static final long serialVersionUID = 0xC6F6D6F8785AE47CL;

    public U32Arithmetic() {
        super();
    }

    @NotNull
    @Override
    public U32 newSlot() {
        return new U32();
    }

    @NotNull
    @Override
    public U32 cloneSlot(@NotNull U32 original) {
        return new U32(original.value);
    }

    @NotNull
    @Override
    public int[] newArray(int length) {
        if (length == 0) {
            return I32SlotArrayAllocator.EMPTY_ARRAY;
        }
        return new int[length];
    }

    @Override
    public int[] cloneArray(@NotNull int[] original) {
        if (original.length == 0) {
            return I32SlotArrayAllocator.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int compareArray(@NotNull int[] left, @NotNull int[] right) {
        return Arrays.compareUnsigned(left, right);
    }

    @Override
    public int length(@NotNull int[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull int[] array, int index, @NotNull U32 slot) {
        slot.value = array[index];
    }

    @Override
    public void store(@NotNull int[] array, int index, @NotNull U32 slot) {
        array[index] = slot.value;
    }

    @Override
    public void zero(@NotNull U32 out) {
        out.value = 0;
    }

    @Override
    public void one(@NotNull U32 out) {
        out.value = 1;
    }

    @Override
    public void two(@NotNull U32 out) {
        out.value = 2;
    }

    @Override
    public void min(@NotNull U32 result) {
        result.value = 0;
    }

    @Override
    public void max(@NotNull U32 result) {
        result.value = -1;
    }

    @Override
    public void min(@NotNull U32 left, @NotNull U32 right, @NotNull U32 result) {
        result.setI32(Math.min(left.getI32() + Integer.MIN_VALUE, right.getI32() + Integer.MIN_VALUE) - Integer.MIN_VALUE);
    }

    @Override
    public void max(@NotNull U32 left, @NotNull U32 right, @NotNull U32 result) {
        result.setI32(Math.max(left.getI32() + Integer.MIN_VALUE, right.getI32() + Integer.MIN_VALUE) - Integer.MIN_VALUE);
    }

    @Override
    public int compareSlot(@NotNull U32 left, @NotNull U32 right) {
        return Integer.compareUnsigned(left.value, right.value);
    }

    @Override
    public int sign(@NotNull U32 in) {
        return in.getI32() == 0 ? 0 : 1;
    }

    @Override
    public void move(@NotNull U32 in, @NotNull U32 out) {
        out.setI32(in.getI32());
    }

    @Override
    public void exchange(@NotNull U32 first, @NotNull U32 second) {
        first.setI32(second.updateI32(first.getI32()));
    }

    @Override
    public void negate(@NotNull U32 in, @NotNull U32 out) {
        if (in.getI32() == 0) {
            out.setI32(0);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void absolute(@NotNull U32 in, @NotNull U32 out) {
        // same as move
        out.setI32(in);
    }

    @Override
    public boolean isInvertible(@NotNull U32 variable) {
        return variable.getI32() == 1;
    }

    @Override
    public void invert(@NotNull U32 in, @NotNull U32 out) {
        if (in.getI32() == 1) {
            out.setI32(1);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull U32 in, @NotNull U32 out) {
        final int value = in.getI32();
        if (value == -1) {
            throw new ArithmeticException();
        }
        out.setI32(value + 1);
    }

    @Override
    public void decrease(@NotNull U32 in, @NotNull U32 out) {
        final int value = in.getI32();
        if (value == 0) {
            throw new ArithmeticException();
        }
        out.setI32(value - 1);
    }

    @Override
    public void add(@NotNull U32 left, @NotNull U32 right, @NotNull U32 out) {
        final long resultValue = (0xffffffffL & left.value) + (0xffffffffL & right.value);
        if (resultValue > 0xffffffffL) {
            throw new ArithmeticException();
        }
        out.value = (int) resultValue;
    }

    @Override
    public void subtract(@NotNull U32 left, @NotNull U32 right, @NotNull U32 out) {
        final long resultValue = (0xffffffffL & left.value) - (0xffffffffL & right.value);
        if (resultValue < 0L) {
            throw new ArithmeticException();
        }
        out.value = (int) resultValue;
    }

    @Override
    public void multiply(@NotNull U32 left, @NotNull U32 right, @NotNull U32 out) {
        final long resultValue = (0xffffffffL & left.value) * (0xffffffffL & right.value);
        if (resultValue < 0L || resultValue > 0xffffffffL) {
            throw new ArithmeticException();
        }
        out.value = (int) resultValue;
    }

    @Override
    public void divide(@NotNull U32 left, @NotNull U32 right, @NotNull U32 result, @NotNull RoundingMode mode) {
        // todo
    }

    @Override
    public void divide(@NotNull U32 left, @NotNull U32 right, @Nullable U32 quotient, @Nullable U32 remainder) {
        // todo
    }

    @CodeHistory(date = "2026/9/12")
    public static class U32HashSAC extends U32Arithmetic implements SlotArrayComparator.HashSAC<U32, int[]> {

        private static final long serialVersionUID = 0xA04F9F574E32C1D9L;

        @NotNull
        final CapacityPolicy capacityPolicy;

        public U32HashSAC(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public int slotHash(@NotNull U32 slot) {
            return slot.value;
        }
    }
}
