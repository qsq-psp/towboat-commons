package mujica.ds.i64;

import mujica.ds.slot.Arithmetic;
import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2025/2/27", name = "ClampedMath")
@CodeHistory(date = "2026/8/4")
public class S64ClampedArithmetic extends PrimitiveArithmetic<S64, long[]> {

    public static final S64ClampedArithmetic INSTANCE = new S64ClampedArithmetic();

    public S64ClampedArithmetic() {
        super();
    }

    @NotNull
    @Override
    public S64 newSlot() {
        return new S64();
    }

    @NotNull
    @Override
    public S64 cloneSlot(@NotNull S64 original) {
        return new S64(original.value);
    }

    @NotNull
    @Override
    public long[] newArray(int length) {
        if (length == 0) {
            return SlotArrayAllocatorI64.EMPTY_ARRAY;
        }
        return new long[length];
    }

    @Override
    public long[] cloneArray(@NotNull long[] original) {
        if (original.length == 0) {
            return SlotArrayAllocatorI64.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int length(@NotNull long[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull long[] array, int index, @NotNull S64 slot) {
        slot.value = array[index];
    }

    @Override
    public void store(@NotNull long[] array, int index, @NotNull S64 slot) {
        array[index] = slot.value;
    }

    @Override
    public void zero(@NotNull S64 result) {
        result.value = 0L;
    }

    @Override
    public void one(@NotNull S64 result) {
        result.value = 1L;
    }

    @Override
    public void two(@NotNull S64 result) {
        result.value = 2L;
    }

    @Override
    public void min(@NotNull S64 result) {
        result.value = Long.MIN_VALUE;
    }

    @Override
    public void max(@NotNull S64 result) {
        result.value = Long.MAX_VALUE;
    }

    @Override
    public void min(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        result.value = Math.min(left.value, right.value);
    }

    @Override
    public void max(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        result.value = Math.max(left.value, right.value);
    }

    @Override
    public int compareSlot(@NotNull S64 left, @NotNull S64 right) {
        return Long.compare(left.value, right.value);
    }

    @Override
    public int sign(@NotNull S64 variable) {
        return Long.signum(variable.value);
    }

    @Override
    public void move(@NotNull S64 src, @NotNull S64 dst) {
        dst.value = src.value;
    }

    @Override
    public void exchange(@NotNull S64 a, @NotNull S64 b) {
        a.setI64(b.updateI64(a.getI64()));
    }

    @Override
    public void negate(@NotNull S64 variable, @NotNull S64 result) {
        if (variable.value == Long.MIN_VALUE) {
            result.value = Long.MAX_VALUE;
        } else {
            result.value = -variable.value;
        }
    }

    @Override
    public void absolute(@NotNull S64 variable, @NotNull S64 result) {
        if (variable.value >= 0) {
            result.value = variable.value;
        } else if (variable.value == Long.MIN_VALUE) {
            result.value = Long.MAX_VALUE;
        } else {
            result.value = -variable.value;
        }
    }

    @Override
    public boolean isInvertible(@NotNull S64 variable) {
        return variable.value == 1L || variable.value == -1L;
    }

    @Override
    public void invert(@NotNull S64 variable, @NotNull S64 result) {
        if (isInvertible(variable)) {
            // same as move
            result.value = variable.value;
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull S64 variable, @NotNull S64 result) {
        if (variable.value == Long.MAX_VALUE) {
            result.value = Long.MAX_VALUE;
        } else {
            result.value = variable.value + 1L;
        }
    }

    @Override
    public void decrease(@NotNull S64 variable, @NotNull S64 result) {
        if (variable.value == Long.MIN_VALUE) {
            result.value = Long.MIN_VALUE;
        } else {
            result.value = variable.value - 1L;
        }
    }

    @Override
    public void add(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        result.value = left.value + right.value;
        if (((left.value ^ result.value) & (right.value ^ result.value)) < 0) {
            if (left.value < 0) {
                result.value = Long.MIN_VALUE;
            } else {
                result.value = Long.MAX_VALUE;
            }
        }
    }

    @Override
    public void subtract(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        result.value = left.value - right.value;
        if (((left.value ^ right.value) & (left.value ^ result.value)) < 0) {
            if (left.value < 0) {
                result.value = Long.MIN_VALUE;
            } else {
                result.value = Long.MAX_VALUE;
            }
        }
    }

    @Override
    public void multiply(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        try {
            result.value = Math.multiplyExact(left.value, right.value);
        } catch (ArithmeticException e) {
            if ((left.value ^ right.value) < 0L) {
                result.value = Long.MIN_VALUE;
            } else {
                result.value = Long.MAX_VALUE;
            }
        }
    }

    @Override
    public void divide(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result, @NotNull RoundingMode mode) {
        final long resultValue = left.value / right.value;
        if (left.value == right.value * resultValue) {
            result.value = resultValue;
        } else {
            throw new ArithmeticException("can not divide exact");
        }
    }

    @Override
    public void divide(@NotNull S64 left, @NotNull S64 right, @Nullable S64 quotient, @Nullable S64 remainder) {
        if (quotient != null) {
            quotient.setI64(left.value / right.value);
        }
        if (remainder != null) {
            remainder.setI64(left.value % right.value);
        }
    }
}
