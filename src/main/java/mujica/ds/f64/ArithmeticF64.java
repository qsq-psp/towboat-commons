package mujica.ds.f64;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2026/7/17")
public class ArithmeticF64 extends PrimitiveArithmetic<F64Slot, double[]> {

    public ArithmeticF64() {
        super();
    }

    @NotNull
    @Override
    public F64Slot newSlot() {
        return new F64();
    }

    @NotNull
    @Override
    public F64Slot cloneSlot(@NotNull F64Slot original) {
        return new F64(original.getF64());
    }

    @NotNull
    @Override
    public double[] newArray(int length) {
        if (length == 0) {
            return SlotArrayAllocatorF64.EMPTY_ARRAY;
        }
        return new double[length];
    }

    @Override
    public double[] cloneArray(@NotNull double[] original) {
        if (original.length == 0) {
            return SlotArrayAllocatorF64.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int length(@NotNull double[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull double[] array, int index, @NotNull F64Slot slot) {
        slot.setF64(array[index]);
    }

    @Override
    public void store(@NotNull double[] array, int index, @NotNull F64Slot slot) {
        array[index] = slot.getF64();
    }

    @Override
    public void zero(@NotNull F64Slot result) {
        result.setF64(0.0);
    }

    @Override
    public void one(@NotNull F64Slot result) {
        result.setF64(1.0);
    }

    @Override
    public void two(@NotNull F64Slot result) {
        result.setF64(2.0);
    }

    @Override
    public void min(@NotNull F64Slot result) {
        result.setF64(Double.NEGATIVE_INFINITY);
    }

    @Override
    public void max(@NotNull F64Slot result) {
        result.setF64(Double.POSITIVE_INFINITY);
    }

    @Override
    public void min(@NotNull F64Slot left, @NotNull F64Slot right, @NotNull F64Slot result) {
        result.setF64(Math.min(left.getF64(), right.getF64()));
    }

    @Override
    public void max(@NotNull F64Slot left, @NotNull F64Slot right, @NotNull F64Slot result) {
        result.setF64(Math.max(left.getF64(), right.getF64()));
    }

    @Override
    public int compareSlot(@NotNull F64Slot a, @NotNull F64Slot b) {
        return Double.compare(a.getF64(), b.getF64());
    }

    @Override
    public int sign(@NotNull F64Slot variable) {
        return Double.compare(variable.getF64(), 0.0);
    }

    @Override
    public void move(@NotNull F64Slot src, @NotNull F64Slot dst) {
        dst.setF64(src.getF64());
    }

    @Override
    public void exchange(@NotNull F64Slot first, @NotNull F64Slot second) {
        first.setF64(second.updateF64(first.getF64()));
    }

    @Override
    public void negate(@NotNull F64Slot variable, @NotNull F64Slot result) {
        result.setF64(-variable.getF64());
    }

    @Override
    public void absolute(@NotNull F64Slot variable, @NotNull F64Slot result) {
        result.setF64(variable.getF64());
    }

    @Override
    public boolean isInvertible(@NotNull F64Slot variable) {
        final double value = variable.getF64();
        return value == value && value != 0.0;
    }

    @Override
    public void invert(@NotNull F64Slot variable, @NotNull F64Slot result) {
        result.setF64(1.0 / variable.getF64());
    }

    @Override
    public void increase(@NotNull F64Slot variable, @NotNull F64Slot result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrease(@NotNull F64Slot variable, @NotNull F64Slot result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(@NotNull F64Slot left, @NotNull F64Slot right, @NotNull F64Slot result) {
        result.setF64(left.getF64() + right.getF64());
    }

    @Override
    public void subtract(@NotNull F64Slot left, @NotNull F64Slot right, @NotNull F64Slot result) {
        result.setF64(left.getF64() - right.getF64());
    }

    @Override
    public void multiply(@NotNull F64Slot left, @NotNull F64Slot right, @NotNull F64Slot result) {
        result.setF64(left.getF64() * right.getF64());
    }

    @Override
    public void square(@NotNull F64Slot variable, @NotNull F64Slot result) {
        final double value = variable.getF64();
        result.setF64(value * value);
    }

    @Override
    public void triangle(@NotNull F64Slot variable, @NotNull F64Slot result) {
        final double value = variable.getF64();
        result.setF64(0.5 * value * (value + 1.0));
    }

    @Override
    public void divide(@NotNull F64Slot left, @NotNull F64Slot right, @NotNull F64Slot result, @NotNull RoundingMode mode) {
        result.setF64(left.getF64() / right.getF64());
    }

    @Override
    public void divide(@NotNull F64Slot left, @NotNull F64Slot right, @Nullable F64Slot quotient, @Nullable F64Slot remainder) {
        final double leftValue = left.getF64();
        final double rightValue = right.getF64();
        final double remainderValue = leftValue % rightValue;
        if (quotient != null) {
            quotient.setF64(leftValue - rightValue * remainderValue);
        }
        if (remainder != null) {
            remainder.setF64(remainderValue);
        }
    }
}
