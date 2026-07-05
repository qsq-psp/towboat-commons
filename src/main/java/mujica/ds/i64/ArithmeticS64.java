package mujica.ds.i64;

import mujica.ds.slot.AbstractArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/6/15")
public class ArithmeticS64 extends AbstractArithmetic<I64Slot> {

    public ArithmeticS64() {
        super();
    }

    @NotNull
    @Override
    public I64Slot newSlot() {
        return new S64();
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
        return Long.compare(a.getI64(), b.getI64());
    }

    @Override
    public int sign(@NotNull I64Slot variable) {
        return Long.signum(variable.getI64());
    }

    @Override
    public void move(@NotNull I64Slot src, @NotNull I64Slot dst) {
        dst.setI64(src.getI64());
    }

    @Override
    public void negate(@NotNull I64Slot variable, @NotNull I64Slot result) {
        result.setI64(Math.negateExact(variable.getI64()));
    }

    @Override
    public void absolute(@NotNull I64Slot variable, @NotNull I64Slot result) {
        final long value = variable.getI64();
        if (value >= 0L) {
            result.setI64(value);
        } else if (value != Long.MIN_VALUE) {
            result.setI64(-value);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public boolean isInvertible(@NotNull I64Slot variable) {
        final long value = variable.getI64();
        return value == 1L || value == -1L;
    }

    @Override
    public void invert(@NotNull I64Slot variable, @NotNull I64Slot result) {
        if (isInvertible(variable)) {
            result.setI64(variable.getI64());
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull I64Slot variable, @NotNull I64Slot result) {
        result.setI64(Math.incrementExact(variable.getI64()));
    }

    @Override
    public void decrease(@NotNull I64Slot variable, @NotNull I64Slot result) {
        result.setI64(Math.decrementExact(variable.getI64()));
    }

    @Override
    public void add(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.addExact(left.getI64(), right.getI64()));
    }

    @Override
    public void subtract(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.subtractExact(left.getI64(), right.getI64()));
    }

    @Override
    public void multiply(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.multiplyExact(left.getI64(), right.getI64()));
    }

    @Override
    public void square(@NotNull I64Slot variable, @NotNull I64Slot result) {
        final long value = variable.getI64();
        result.setI64(Math.multiplyExact(value, value));
    }

    @Override
    public void triangle(@NotNull I64Slot variable, @NotNull I64Slot result) {
        long left = variable.getI64();
        long right = Math.incrementExact(left);
        if ((left & 1L) == 0) {
            left >>= 1;
        } else {
            right >>= 1;
        }
        result.setI64(Math.multiplyExact(left, right));
    }

    @Override
    public void divide(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        // todo
    }

    @Override
    public void divide(@NotNull I64Slot left, @NotNull I64Slot right, @Nullable I64Slot quotient, @Nullable I64Slot remainder) {
        final long leftValue = left.getI64();
        final long rightValue = right.getI64();
        if (quotient != null) {
            quotient.setI64(leftValue / rightValue);
        }
        if (remainder != null) {
            remainder.setI64(leftValue % rightValue);
        }
    }
}
