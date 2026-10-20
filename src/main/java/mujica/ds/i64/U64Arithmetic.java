package mujica.ds.i64;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2026/6/16", name = "ArithmeticU64")
@CodeHistory(date = "2026/8/8")
public class U64Arithmetic extends PrimitiveArithmetic<U64, long[]> {

    public static final U64Arithmetic INSTANCE = new U64Arithmetic();

    public U64Arithmetic() {
        super();
    }

    @NotNull
    @Override
    public U64 newSlot() {
        return new U64();
    }

    @NotNull
    @Override
    public U64 cloneSlot(@NotNull U64 original) {
        return new U64(original.getI64());
    }

    @NotNull
    @Override
    public long[] newArray(int length) {
        if (length == 0) {
            return I64SlotArrayAllocator.EMPTY_ARRAY;
        }
        return new long[length];
    }

    @Override
    public long[] cloneArray(@NotNull long[] original) {
        if (original.length == 0) {
            return I64SlotArrayAllocator.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int length(@NotNull long[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull long[] array, int index, @NotNull U64 slot) {
        slot.value = array[index];
    }

    @Override
    public void store(@NotNull long[] array, int index, @NotNull U64 slot) {
        array[index] = slot.value;
    }

    @Override
    public void zero(@NotNull U64 out) {
        out.value = 0L;
    }

    @Override
    public void one(@NotNull U64 out) {
        out.value = 1L;
    }

    @Override
    public void two(@NotNull U64 out) {
        out.value = 2L;
    }

    @Override
    public void min(@NotNull U64 result) {
        result.value = 0L;
    }

    @Override
    public void max(@NotNull U64 result) {
        result.value = -1L;
    }

    @Override
    public void min(@NotNull U64 left, @NotNull U64 right, @NotNull U64 result) {
        result.value = Math.min(left.value + Long.MIN_VALUE, right.value + Long.MIN_VALUE) - Long.MIN_VALUE;
    }

    @Override
    public void max(@NotNull U64 left, @NotNull U64 right, @NotNull U64 result) {
        result.value = Math.max(left.value + Long.MIN_VALUE, right.value + Long.MIN_VALUE) - Long.MIN_VALUE;
    }

    @Override
    public int compareSlot(@NotNull U64 left, @NotNull U64 right) {
        return Long.compareUnsigned(left.value, right.value);
    }

    @Override
    public int sign(@NotNull U64 in) {
        return in.value == 0L ? 0 : 1;
    }

    @Override
    public void move(@NotNull U64 in, @NotNull U64 out) {
        out.value = in.value;
    }

    @Override
    public void exchange(@NotNull U64 first, @NotNull U64 second) {
        final long temp = first.value;
        first.value = second.value;
        second.value = temp;
    }

    @Override
    public void negate(@NotNull U64 in, @NotNull U64 out) {
        if (in.value == 0L) {
            out.value = 0L;
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void absolute(@NotNull U64 in, @NotNull U64 out) {
        // same as move
        out.value = in.value;
    }

    @Override
    public boolean isInvertible(@NotNull U64 variable) {
        return variable.getI64() == 1L;
    }

    @Override
    public void invert(@NotNull U64 in, @NotNull U64 out) {
        if (in.getI64() == 1L) {
            out.setI64(1L);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull U64 in, @NotNull U64 out) {
        final long value = in.getI64();
        if (value == -1L) {
            throw new ArithmeticException();
        }
        out.setI64(value + 1L);
    }

    @Override
    public void decrease(@NotNull U64 in, @NotNull U64 out) {
        final long value = in.getI64();
        if (value == 0L) {
            throw new ArithmeticException();
        }
        out.setI64(value - 1L);
    }

    @Override
    public void add(@NotNull U64 left, @NotNull U64 right, @NotNull U64 out) {
        // todo
    }

    @Override
    public void subtract(@NotNull U64 left, @NotNull U64 right, @NotNull U64 out) {
        // todo
    }

    @Override
    public void multiply(@NotNull U64 left, @NotNull U64 right, @NotNull U64 out) {
        // todo
    }

    @Override
    public void divide(@NotNull U64 left, @NotNull U64 right, @NotNull U64 result, @NotNull RoundingMode mode) {
        // todo
    }

    @Override
    public void divide(@NotNull U64 left, @NotNull U64 right, @Nullable U64 quotient, @Nullable U64 remainder) {
        // todo
    }
}
