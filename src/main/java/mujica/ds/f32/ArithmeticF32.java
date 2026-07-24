package mujica.ds.f32;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.Arrays;

@CodeHistory(date = "2026/7/16")
public class ArithmeticF32 extends PrimitiveArithmetic<F32Slot, float[]> {

    public ArithmeticF32() {
        super();
    }

    @NotNull
    @Override
    public F32Slot newSlot() {
        return new F32();
    }

    @NotNull
    @Override
    public F32Slot cloneSlot(@NotNull F32Slot original) {
        return new F32(original.getF32());
    }

    @NotNull
    @Override
    public float[] newArray(int length) {
        if (length == 0) {
            return SlotArrayAllocatorF32.EMPTY_ARRAY;
        }
        return new float[length];
    }

    @Override
    public float[] cloneArray(@NotNull float[] original) {
        if (original.length == 0) {
            return SlotArrayAllocatorF32.EMPTY_ARRAY;
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
    public void load(@NotNull float[] array, int index, @NotNull F32Slot slot) {
        slot.setF32(array[index]);
    }

    @Override
    public void store(@NotNull float[] array, int index, @NotNull F32Slot slot) {
        array[index] = slot.getF32();
    }

    @Override
    public void zero(@NotNull F32Slot result) {
        result.setF32(0.0f);
    }

    @Override
    public void one(@NotNull F32Slot result) {
        result.setF32(1.0f);
    }

    @Override
    public void two(@NotNull F32Slot result) {
        result.setF32(2.0f);
    }

    @Override
    public void min(@NotNull F32Slot result) {
        result.setF32(Float.NEGATIVE_INFINITY);
    }

    @Override
    public void max(@NotNull F32Slot result) {
        result.setF32(Float.POSITIVE_INFINITY);
    }

    @Override
    public void min(@NotNull F32Slot left, @NotNull F32Slot right, @NotNull F32Slot result) {
        result.setF32(Math.min(left.getF32(), right.getF32()));
    }

    @Override
    public void max(@NotNull F32Slot left, @NotNull F32Slot right, @NotNull F32Slot result) {
        result.setF32(Math.max(left.getF32(), right.getF32()));
    }

    @Override
    public int compareSlot(@NotNull F32Slot a, @NotNull F32Slot b) {
        return Float.compare(a.getF32(), b.getF32());
    }

    @Override
    public int sign(@NotNull F32Slot variable) {
        return Float.compare(variable.getF32(), 0.0f);
    }

    @Override
    public void move(@NotNull F32Slot src, @NotNull F32Slot dst) {
        dst.setF32(src.getF32());
    }

    @Override
    public void exchange(@NotNull F32Slot first, @NotNull F32Slot second) {
        first.setF32(second.updateF32(first.getF32()));
    }

    @Override
    public void negate(@NotNull F32Slot variable, @NotNull F32Slot result) {
        result.setF32(-variable.getF32());
    }

    @Override
    public void absolute(@NotNull F32Slot variable, @NotNull F32Slot result) {
        result.setF32(Math.abs(variable.getF32()));
    }

    @Override
    public boolean isInvertible(@NotNull F32Slot variable) {
        final float value = variable.getF32();
        return value == value && value != 0.0f;
    }

    @Override
    public void invert(@NotNull F32Slot variable, @NotNull F32Slot result) {
        result.setF32(1.0f / variable.getF32());
    }

    @Override
    public void increase(@NotNull F32Slot variable, @NotNull F32Slot result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrease(@NotNull F32Slot variable, @NotNull F32Slot result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(@NotNull F32Slot left, @NotNull F32Slot right, @NotNull F32Slot result) {
        result.setF32(left.getF32() + right.getF32());
    }

    @Override
    public void subtract(@NotNull F32Slot left, @NotNull F32Slot right, @NotNull F32Slot result) {
        result.setF32(left.getF32() - right.getF32());
    }

    @Override
    public void multiply(@NotNull F32Slot left, @NotNull F32Slot right, @NotNull F32Slot result) {
        result.setF32(left.getF32() * right.getF32());
    }

    @Override
    public void square(@NotNull F32Slot variable, @NotNull F32Slot result) {
        final float value = variable.getF32();
        result.setF32(value * value);
    }

    @Override
    public void triangle(@NotNull F32Slot variable, @NotNull F32Slot result) {
        final float value = variable.getF32();
        result.setF32(0.5f * value * (value + 1.0f));
    }

    @Override
    public void divide(@NotNull F32Slot left, @NotNull F32Slot right, @NotNull F32Slot result, @NotNull RoundingMode mode) {
        result.setF32(left.getF32() / right.getF32());
    }

    @Override
    public void divide(@NotNull F32Slot left, @NotNull F32Slot right, @Nullable F32Slot quotient, @Nullable F32Slot remainder) {
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
