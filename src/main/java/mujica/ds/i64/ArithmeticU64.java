package mujica.ds.i64;

import mujica.ds.slot.AbstractArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/6/16")
public class ArithmeticU64 extends AbstractArithmetic<I64Slot> {

    public ArithmeticU64() {
        super();
    }

    @NotNull
    @Override
    public I64Slot newSlot() {
        return new U64();
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
    public void divide(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        // todo
    }

    @Override
    public void divide(@NotNull I64Slot left, @NotNull I64Slot right, @Nullable I64Slot quotient, @Nullable I64Slot remainder) {
        // todo
    }
}
