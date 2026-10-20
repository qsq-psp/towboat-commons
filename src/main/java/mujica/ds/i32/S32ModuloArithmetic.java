package mujica.ds.i32;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.Arrays;

@CodeHistory(date = "2026/7/31")
public class S32ModuloArithmetic extends PrimitiveArithmetic<S32, int[]> {

    public static final S32ModuloArithmetic INSTANCE = new S32ModuloArithmetic();

    public S32ModuloArithmetic() {
        super();
    }

    @NotNull
    @Override
    public S32 newSlot() {
        return new S32();
    }

    @NotNull
    @Override
    public S32 cloneSlot(@NotNull S32 original) {
        return new S32(original.value);
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
        return Arrays.compare(left, right);
    }

    @Override
    public int length(@NotNull int[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull int[] array, int index, @NotNull S32 slot) {
        slot.value = array[index];
    }

    @Override
    public void store(@NotNull int[] array, int index, @NotNull S32 slot) {
        array[index] = slot.value;
    }

    @Override
    public void zero(@NotNull S32 out) {
        out.value = 0;
    }

    @Override
    public void one(@NotNull S32 out) {
        out.value = 1;
    }

    @Override
    public void two(@NotNull S32 out) {
        out.value = 2;
    }

    @Override
    public void min(@NotNull S32 result) {
        result.value = Integer.MIN_VALUE;
    }

    @Override
    public void max(@NotNull S32 result) {
        result.value = Integer.MAX_VALUE;
    }

    @Override
    public void min(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result) {
        result.value = Math.min(left.value, right.value);
    }

    @Override
    public void max(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result) {
        result.value = Math.max(left.value, right.value);
    }

    @Override
    public int compareSlot(@NotNull S32 left, @NotNull S32 right) {
        return Integer.compare(left.value, right.value);
    }

    @Override
    public int sign(@NotNull S32 in) {
        return Integer.signum(in.value);
    }

    @Override
    public void move(@NotNull S32 in, @NotNull S32 out) {
        out.value = in.value;
    }

    @Override
    public void exchange(@NotNull S32 first, @NotNull S32 second) {
        first.setI32(second.updateI32(first.getI32()));
    }

    @Override
    public void negate(@NotNull S32 in, @NotNull S32 out) {
        out.value = -in.value;
    }

    @Override
    public void absolute(@NotNull S32 in, @NotNull S32 out) {
        out.value = Math.abs(in.value);
    }

    @Override
    public boolean isInvertible(@NotNull S32 variable) {
        return (variable.value & 1) != 0;
    }

    @Override
    public void invert(@NotNull S32 in, @NotNull S32 out) {
        // todo
    }

    @Override
    public void increase(@NotNull S32 in, @NotNull S32 out) {
        out.value = in.value + 1;
    }

    @Override
    public void decrease(@NotNull S32 in, @NotNull S32 out) {
        out.value = in.value - 1;
    }

    @Override
    public void add(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        out.value = left.value + right.value;
    }

    @Override
    public void subtract(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        out.value = left.value - right.value;
    }

    @Override
    public void multiply(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        out.value = left.value * right.value;
    }

    @Override
    public void square(@NotNull S32 in, @NotNull S32 out) {
        out.value = in.value * in.value;
    }

    @Override
    public void triangle(@NotNull S32 in, @NotNull S32 out) {
        int leftValue = in.value;
        int rightValue = leftValue + 1;
        if ((leftValue & 1) == 0) {
            leftValue >>= 1;
        } else {
            rightValue >>= 1;
        }
        out.value = leftValue * rightValue;
    }

    @Override
    public void divide(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result, @NotNull RoundingMode mode) {
        final int resultValue = left.value / right.value;
        if (left.value == right.value * resultValue) {
            result.value = resultValue;
        } else {
            throw new ArithmeticException("can not divide exact");
        }
    }

    @Override
    public void divide(@NotNull S32 left, @NotNull S32 right, @Nullable S32 quotient, @Nullable S32 remainder) {
        if (quotient != null) {
            quotient.value = left.value / right.value;
        }
        if (remainder != null) {
            remainder.value = left.value % right.value;
        }
    }
}
