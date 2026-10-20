package mujica.ds.i32;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.Arrays;

@CodeHistory(date = "2026/10/20")
public class S32ModuloS32Arithmetic extends PrimitiveArithmetic<S32, int[]> {

    public final int modulo;

    public S32ModuloS32Arithmetic(int modulo) {
        super();
        if (modulo <= 1) {
            throw new IllegalArgumentException();
        }
        this.modulo = modulo;
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
        out.value = 2 % modulo;
    }

    @Override
    public void min(@NotNull S32 result) {
        result.value = 0;
    }

    @Override
    public void max(@NotNull S32 result) {
        result.value = modulo - 1;
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
        if (in.value % modulo == 0) {
            return 0;
        } else {
            return 1;
        }
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
        int value = -(in.value % modulo);
        if (value < 0) {
            value += modulo;
        }
        out.value = value;
    }

    @Override
    public void absolute(@NotNull S32 in, @NotNull S32 out) {
        int value = in.value % modulo;
        if (value < 0) {
            value += modulo;
        }
        out.value = value;
    }

    @Override
    public boolean isInvertible(@NotNull S32 variable) {
        return false; // todo
    }

    @Override
    public void invert(@NotNull S32 in, @NotNull S32 out) {
        // todo
    }

    @Override
    public void increase(@NotNull S32 in, @NotNull S32 out) {
        int value = in.value % modulo + 1;
        if (value < 0) {
            value += modulo;
        } else if (value >= modulo) {
            value -= modulo;
        }
        out.value = value;
    }

    @Override
    public void decrease(@NotNull S32 in, @NotNull S32 out) {
        int value = in.value % modulo - 1;
        if (value < 0) {
            value += modulo;
        }
        out.value = value;
    }

    @Override
    public void add(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        int value;
        if (modulo <= Integer.MAX_VALUE / 2 + 1) {
            value = left.value % modulo + right.value % modulo;
            if (value >= modulo) {
                value -= modulo;
            }
        } else {
            value = (int) ((((long) left.value) + ((long) right.value)) % modulo);
        }
        if (value < 0) {
            value += modulo;
        }
        out.value = value;
    }

    @Override
    public void subtract(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        int value;
        if (modulo <= Integer.MAX_VALUE / 2 + 1) {
            value = left.value % modulo - right.value % modulo;
            if (value >= modulo) {
                value -= modulo;
            }
        } else {
            value = (int) ((((long) left.value) - ((long) right.value)) % modulo);
        }
        if (value < 0) {
            value += modulo;
        }
        out.value = value;
    }

    @Override
    public void multiply(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        // 46340**2 = 2147395600 < 2147483647 = Integer.MAX_VALUE
        // 46341**2 = 2147488281 < 2147483647 = Integer.MAX_VALUE
        int value;
        if (modulo <= 46340) {
            value = (left.value % modulo) * (right.value % modulo) % modulo;
        } else {
            value = (int) ((((long) left.value) * ((long) right.value)) % modulo);
        }
        if (value < 0) {
            value += modulo;
        }
        out.value = value;
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
