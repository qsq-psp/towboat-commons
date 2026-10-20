package mujica.ds.i64;

import mujica.ds.slot.AbstractArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2025/3/3", name = "ModuloI64")
@CodeHistory(date = "2026/6/28")
public class S64ModuloArithmetic extends AbstractArithmetic<S64> {

    @NotNull
    @Override
    public S64 newSlot() {
        return new S64();
    }

    @Override
    public void zero(@NotNull S64 out) {
        out.value = 0L;
    }

    @Override
    public void one(@NotNull S64 out) {
        out.value = 1L;
    }

    @Override
    public int compareSlot(@NotNull S64 left, @NotNull S64 right) {
        return Long.compare(left.value, right.value);
    }

    @Override
    public void move(@NotNull S64 in, @NotNull S64 out) {
        out.value = in.value;
    }

    @Override
    public void negate(@NotNull S64 in, @NotNull S64 out) {
        out.value = -in.value;
    }

    @Override
    public boolean isInvertible(@NotNull S64 variable) {
        return Math.abs(variable.value) == 1;
    }

    @Override
    public void invert(@NotNull S64 in, @NotNull S64 out) {
        if (Math.abs(in.value) == 1) {
            out.value = in.value;
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void add(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        out.value = left.value + right.value;
    }

    @Override
    public void subtract(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        out.value = left.value - right.value;
    }

    @Override
    public void multiply(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        out.value = left.value * right.value;
    }

    @Override
    public void divide(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result, @NotNull RoundingMode mode) {
        // todo
    }

    @Override
    public void divide(@NotNull S64 left, @NotNull S64 right, @Nullable S64 quotient, @Nullable S64 remainder) {
        // todo
    }
}
