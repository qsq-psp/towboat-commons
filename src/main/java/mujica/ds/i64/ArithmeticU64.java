package mujica.ds.i64;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2026/6/16")
public class ArithmeticU64 extends PrimitiveArithmetic<I64Slot, long[]> {

    public ArithmeticU64() {
        super();
    }

    @NotNull
    @Override
    public I64Slot newSlot() {
        return new U64();
    }

    @NotNull
    @Override
    public I64Slot cloneSlot(@NotNull I64Slot original) {
        return new U64(original.getI64());
    }

    @NotNull
    @Override
    public long[] newArray(int length) {
        if (length == 0) {
            return SlotArrayAllocatorI64.EMPTY_ARRAY;
        }
        return new long[length];
    }

    @Override
    public long[] cloneArray(@NotNull long[] original) {
        if (original.length == 0) {
            return SlotArrayAllocatorI64.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int length(@NotNull long[] array) {
        return array.length;
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
    public void zero(@NotNull I64Slot result) {
        result.setI64(0L);
    }

    @Override
    public void one(@NotNull I64Slot result) {
        result.setI64(1L);
    }

    @Override
    public void two(@NotNull I64Slot result) {
        result.setI64(2L);
    }

    @Override
    public void min(@NotNull I64Slot result) {
        result.setI64(0L);
    }

    @Override
    public void max(@NotNull I64Slot result) {
        result.setI64(-1L);
    }

    @Override
    public void min(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.min(left.getI64() + Long.MIN_VALUE, right.getI64() + Long.MIN_VALUE) - Long.MIN_VALUE);
    }

    @Override
    public void max(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.max(left.getI64() + Long.MIN_VALUE, right.getI64() + Long.MIN_VALUE) - Long.MIN_VALUE);
    }

    @Override
    public int compareSlot(@NotNull I64Slot a, @NotNull I64Slot b) {
        return Long.compareUnsigned(a.getI64(), b.getI64());
    }

    @Override
    public int sign(@NotNull I64Slot variable) {
        return variable.getI64() == 0L ? 0 : 1;
    }

    @Override
    public void move(@NotNull I64Slot src, @NotNull I64Slot dst) {
        dst.setI64(src.getI64());
    }

    @Override
    public void exchange(@NotNull I64Slot a, @NotNull I64Slot b) {
        a.setI64(b.updateI64(a.getI64()));
    }

    @Override
    public void negate(@NotNull I64Slot variable, @NotNull I64Slot result) {
        if (variable.getI64() == 0L) {
            result.setI64(0L);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void absolute(@NotNull I64Slot src, @NotNull I64Slot dst) {
        // same as move
        dst.setI64(src.getI64());
    }

    @Override
    public boolean isInvertible(@NotNull I64Slot variable) {
        return variable.getI64() == 1L;
    }

    @Override
    public void invert(@NotNull I64Slot variable, @NotNull I64Slot result) {
        if (variable.getI64() == 1L) {
            result.setI64(1L);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull I64Slot variable, @NotNull I64Slot result) {
        final long value = variable.getI64();
        if (value == -1L) {
            throw new ArithmeticException();
        }
        result.setI64(value + 1L);
    }

    @Override
    public void decrease(@NotNull I64Slot variable, @NotNull I64Slot result) {
        final long value = variable.getI64();
        if (value == 0L) {
            throw new ArithmeticException();
        }
        result.setI64(value - 1L);
    }

    @Override
    public void add(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        // todo
    }

    @Override
    public void subtract(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        // todo
    }

    @Override
    public void multiply(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        // todo
    }

    @Override
    public void divide(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result, @NotNull RoundingMode mode) {
        // todo
    }

    @Override
    public void divide(@NotNull I64Slot left, @NotNull I64Slot right, @Nullable I64Slot quotient, @Nullable I64Slot remainder) {
        // todo
    }
}
