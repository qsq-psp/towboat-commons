package mujica.ds.f32;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.Arrays;

@CodeHistory(date = "2026/7/16", name = "ArithmeticF32")
@CodeHistory(date = "2026/8/9")
public class F32Arithmetic extends PrimitiveArithmetic<F32, float[]> {

    public static final F32Arithmetic INSTANCE = new F32Arithmetic();

    public F32Arithmetic() {
        super();
    }

    @NotNull
    @Override
    public F32 newSlot() {
        return new F32();
    }

    @NotNull
    @Override
    public F32 cloneSlot(@NotNull F32 original) {
        return new F32(original.getF32());
    }

    @NotNull
    @Override
    public float[] newArray(int length) {
        if (length == 0) {
            return F32SlotArrayAllocator.EMPTY_ARRAY;
        }
        return new float[length];
    }

    @Override
    public float[] cloneArray(@NotNull float[] original) {
        if (original.length == 0) {
            return F32SlotArrayAllocator.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int compareArray(@NotNull float[] left, @NotNull float[] right) {
        return Arrays.compare(left, right);
    }

    @Override
    public int length(@NotNull float[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull float[] array, int index, @NotNull F32 slot) {
        slot.value = array[index];
    }

    @Override
    public void store(@NotNull float[] array, int index, @NotNull F32 slot) {
        array[index] = slot.value;
    }

    @Override
    public void zero(@NotNull F32 out) {
        out.value = 0.0f;
    }

    @Override
    public void one(@NotNull F32 out) {
        out.value = 1.0f;
    }

    @Override
    public void two(@NotNull F32 out) {
        out.value = 2.0f;
    }

    @Override
    public void min(@NotNull F32 result) {
        result.value = Float.NEGATIVE_INFINITY;
    }

    @Override
    public void max(@NotNull F32 result) {
        result.value = Float.POSITIVE_INFINITY;
    }

    @Override
    public void min(@NotNull F32 left, @NotNull F32 right, @NotNull F32 result) {
        result.value = Math.min(left.value, right.value);
    }

    @Override
    public void max(@NotNull F32 left, @NotNull F32 right, @NotNull F32 result) {
        result.value = Math.max(left.value, right.value);
    }

    @Override
    public int compareSlot(@NotNull F32 left, @NotNull F32 right) {
        return Float.compare(left.value, right.value);
    }

    @Override
    public int sign(@NotNull F32 in) {
        return Float.compare(in.value, 0.0f);
    }

    @Override
    public void move(@NotNull F32 in, @NotNull F32 out) {
        out.value = in.value;
    }

    @Override
    public void exchange(@NotNull F32 first, @NotNull F32 second) {
        final float temp = first.value;
        first.value = second.value;
        second.value = temp;
    }

    @Override
    public void negate(@NotNull F32 in, @NotNull F32 out) {
        out.value = -in.value;
    }

    @Override
    public void absolute(@NotNull F32 in, @NotNull F32 out) {
        out.value = Math.abs(in.value);
    }

    @Override
    public boolean isInvertible(@NotNull F32 variable) {
        return variable.value == variable.value && variable.value != 0.0f;
    }

    @Override
    public void invert(@NotNull F32 in, @NotNull F32 out) {
        out.value = 1.0f / in.value;
    }

    @Override
    public void increase(@NotNull F32 in, @NotNull F32 out) {
        out.setF32(Math.nextUp(in.getF32()));
    }

    @Override
    public void decrease(@NotNull F32 in, @NotNull F32 out) {
        out.setF32(Math.nextDown(in.getF32()));
    }

    @Override
    public void add(@NotNull F32 left, @NotNull F32 right, @NotNull F32 out) {
        out.setF32(left.getF32() + right.getF32());
    }

    @Override
    public void subtract(@NotNull F32 left, @NotNull F32 right, @NotNull F32 out) {
        out.setF32(left.getF32() - right.getF32());
    }

    @Override
    public void multiply(@NotNull F32 left, @NotNull F32 right, @NotNull F32 out) {
        out.setF32(left.getF32() * right.getF32());
    }

    @Override
    public void square(@NotNull F32 in, @NotNull F32 out) {
        final float value = in.getF32();
        out.setF32(value * value);
    }

    @Override
    public void triangle(@NotNull F32 in, @NotNull F32 out) {
        final float value = in.getF32();
        out.setF32(0.5f * value * (value + 1.0f));
    }

    @Override
    public void divide(@NotNull F32 left, @NotNull F32 right, @NotNull F32 result, @NotNull RoundingMode mode) {
        result.setF32(left.getF32() / right.getF32());
    }

    @Override
    public void divide(@NotNull F32 left, @NotNull F32 right, @Nullable F32 quotient, @Nullable F32 remainder) {
        final float leftValue = left.getF32();
        final float rightValue = right.getF32();
        final float remainderValue = leftValue % rightValue;
        if (quotient != null) {
            quotient.setF32(leftValue - rightValue * remainderValue);
        }
        if (remainder != null) {
            remainder.setF32(remainderValue);
        }
    }
}
