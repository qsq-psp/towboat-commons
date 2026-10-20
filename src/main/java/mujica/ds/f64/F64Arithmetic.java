package mujica.ds.f64;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2026/7/17", name = "ArithmeticF64")
@CodeHistory(date = "2026/8/12")
public class F64Arithmetic extends PrimitiveArithmetic<F64, double[]> {

    public static final F64Arithmetic INSTANCE = new F64Arithmetic();

    public F64Arithmetic() {
        super();
    }

    @NotNull
    @Override
    public F64 newSlot() {
        return new F64();
    }

    @NotNull
    @Override
    public F64 cloneSlot(@NotNull F64 original) {
        return new F64(original.getF64());
    }

    @NotNull
    @Override
    public double[] newArray(int length) {
        if (length == 0) {
            return F64SlotArrayAllocator.EMPTY_ARRAY;
        }
        return new double[length];
    }

    @Override
    public double[] cloneArray(@NotNull double[] original) {
        if (original.length == 0) {
            return F64SlotArrayAllocator.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int length(@NotNull double[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull double[] array, int index, @NotNull F64 slot) {
        slot.value = array[index];
    }

    @Override
    public void store(@NotNull double[] array, int index, @NotNull F64 slot) {
        array[index] = slot.value;
    }

    @Override
    public void zero(@NotNull F64 out) {
        out.value = 0.0;
    }

    @Override
    public void one(@NotNull F64 out) {
        out.value = 1.0;
    }

    @Override
    public void two(@NotNull F64 out) {
        out.value = 2.0;
    }

    @Override
    public void min(@NotNull F64 result) {
        result.value = Double.NEGATIVE_INFINITY;
    }

    @Override
    public void max(@NotNull F64 result) {
        result.value = Double.POSITIVE_INFINITY;
    }

    @Override
    public void min(@NotNull F64 left, @NotNull F64 right, @NotNull F64 result) {
        result.setF64(Math.min(left.getF64(), right.getF64()));
    }

    @Override
    public void max(@NotNull F64 left, @NotNull F64 right, @NotNull F64 result) {
        result.setF64(Math.max(left.getF64(), right.getF64()));
    }

    @Override
    public int compareSlot(@NotNull F64 left, @NotNull F64 right) {
        return Double.compare(left.getF64(), right.getF64());
    }

    @Override
    public int sign(@NotNull F64 in) {
        return Double.compare(in.getF64(), 0.0);
    }

    @Override
    public void move(@NotNull F64 in, @NotNull F64 out) {
        out.setF64(in.getF64());
    }

    @Override
    public void exchange(@NotNull F64 first, @NotNull F64 second) {
        first.setF64(second.updateF64(first.getF64()));
    }

    @Override
    public void negate(@NotNull F64 in, @NotNull F64 out) {
        out.setF64(-in.getF64());
    }

    @Override
    public void absolute(@NotNull F64 in, @NotNull F64 out) {
        out.setF64(in.getF64());
    }

    @Override
    public boolean isInvertible(@NotNull F64 variable) {
        final double value = variable.getF64();
        return value == value && value != 0.0;
    }

    @Override
    public void invert(@NotNull F64 in, @NotNull F64 out) {
        out.setF64(1.0 / in.getF64());
    }

    @Override
    public void increase(@NotNull F64 in, @NotNull F64 out) {
        out.value = Math.nextUp(in.value);
    }

    @Override
    public void decrease(@NotNull F64 in, @NotNull F64 out) {
        out.value = Math.nextDown(in.value);
    }

    @Override
    public void add(@NotNull F64 left, @NotNull F64 right, @NotNull F64 out) {
        out.setF64(left.getF64() + right.getF64());
    }

    @Override
    public void subtract(@NotNull F64 left, @NotNull F64 right, @NotNull F64 out) {
        out.setF64(left.getF64() - right.getF64());
    }

    @Override
    public void multiply(@NotNull F64 left, @NotNull F64 right, @NotNull F64 out) {
        out.setF64(left.getF64() * right.getF64());
    }

    @Override
    public void square(@NotNull F64 in, @NotNull F64 out) {
        final double value = in.getF64();
        out.setF64(value * value);
    }

    @Override
    public void triangle(@NotNull F64 in, @NotNull F64 out) {
        final double value = in.getF64();
        out.setF64(0.5 * value * (value + 1.0));
    }

    @Override
    public void divide(@NotNull F64 left, @NotNull F64 right, @NotNull F64 result, @NotNull RoundingMode mode) {
        result.setF64(left.getF64() / right.getF64());
    }

    @Override
    public void divide(@NotNull F64 left, @NotNull F64 right, @Nullable F64 quotient, @Nullable F64 remainder) {
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
