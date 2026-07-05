package mujica.ds.i32;

import mujica.ds.slot.AbstractArithmetic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/6/29.
 */
public class ArithmeticU32 extends AbstractArithmetic<I32Slot> {

    public ArithmeticU32() {
        super();
    }

    @NotNull
    @Override
    public I32Slot newSlot() {
        return new S32();
    }

    @Override
    public void zero(@NotNull I32Slot result) {
        result.setI32(0);
    }

    @Override
    public void one(@NotNull I32Slot result) {
        result.setI32(1);
    }

    @Override
    public int compareSlot(@NotNull I32Slot a, @NotNull I32Slot b) {
        return Integer.compareUnsigned(a.getI32(), b.getI32());
    }

    @Override
    public int sign(@NotNull I32Slot variable) {
        return variable.getI32() == 0 ? 0 : 1;
    }

    @Override
    public void move(@NotNull I32Slot src, @NotNull I32Slot dst) {
        dst.setI32(src.getI32());
    }

    @Override
    public void negate(@NotNull I32Slot variable, @NotNull I32Slot result) {
        if (variable.getI32() == 0) {
            result.setI32(0);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void absolute(@NotNull I32Slot variable, @NotNull I32Slot result) {
        // same as move
        result.setI32(variable);
    }

    @Override
    public boolean isInvertible(@NotNull I32Slot variable) {
        return variable.getI32() == 1;
    }

    @Override
    public void invert(@NotNull I32Slot variable, @NotNull I32Slot result) {
        if (variable.getI32() == 1) {
            result.setI32(1);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull I32Slot variable, @NotNull I32Slot result) {
        final int value = variable.getI32();
        if (value == -1) {
            throw new ArithmeticException();
        }
        result.setI32(value + 1);
    }

    @Override
    public void decrease(@NotNull I32Slot variable, @NotNull I32Slot result) {
        final int value = variable.getI32();
        if (value == 0) {
            throw new ArithmeticException();
        }
        result.setI32(value - 1);
    }

    @Override
    public void add(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        // todo
    }

    @Override
    public void subtract(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        // todo
    }

    @Override
    public void multiply(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        // todo
    }

    @Override
    public void divide(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        // todo
    }

    @Override
    public void divide(@NotNull I32Slot left, @NotNull I32Slot right, @Nullable I32Slot quotient, @Nullable I32Slot remainder) {
        // todo
    }
}
