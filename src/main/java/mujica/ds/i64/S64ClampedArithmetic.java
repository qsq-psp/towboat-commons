package mujica.ds.i64;

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
            return I64SlotArrayAllocator.EMPTY_ARRAY;
        }
        return new long[length];
    }

    @Override
    public long[] cloneArray(@NotNull long[] original) {
        if (original.length == 0) {
            return I64SlotArrayAllocator.EMPTY_ARRAY;
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
    public void zero(@NotNull S64 out) {
        out.value = 0L;
    }

    @Override
    public void one(@NotNull S64 out) {
        out.value = 1L;
    }

    @Override
    public void two(@NotNull S64 out) {
        out.value = 2L;
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
    public int sign(@NotNull S64 in) {
        return Long.signum(in.value);
    }

    @Override
    public void move(@NotNull S64 in, @NotNull S64 out) {
        out.value = in.value;
    }

    @Override
    public void exchange(@NotNull S64 first, @NotNull S64 second) {
        first.setI64(second.updateI64(first.getI64()));
    }

    @Override
    public void negate(@NotNull S64 in, @NotNull S64 out) {
        if (in.value == Long.MIN_VALUE) {
            out.value = Long.MAX_VALUE;
        } else {
            out.value = -in.value;
        }
    }

    @Override
    public void absolute(@NotNull S64 in, @NotNull S64 out) {
        if (in.value >= 0L) {
            out.value = in.value;
        } else if (in.value == Long.MIN_VALUE) {
            out.value = Long.MAX_VALUE;
        } else {
            out.value = -in.value;
        }
    }

    @Override
    public boolean isInvertible(@NotNull S64 variable) {
        return variable.value == 1L || variable.value == -1L;
    }

    @Override
    public void invert(@NotNull S64 in, @NotNull S64 out) {
        if (isInvertible(in)) {
            // same as move
            out.value = in.value;
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull S64 in, @NotNull S64 out) {
        if (in.value == Long.MAX_VALUE) {
            out.value = Long.MAX_VALUE;
        } else {
            out.value = in.value + 1L;
        }
    }

    @Override
    public void decrease(@NotNull S64 in, @NotNull S64 out) {
        if (in.value == Long.MIN_VALUE) {
            out.value = Long.MIN_VALUE;
        } else {
            out.value = in.value - 1L;
        }
    }

    @Override
    public void add(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        out.value = left.value + right.value;
        if (((left.value ^ out.value) & (right.value ^ out.value)) < 0) {
            if (left.value < 0) {
                out.value = Long.MIN_VALUE;
            } else {
                out.value = Long.MAX_VALUE;
            }
        }
    }

    @Override
    public void subtract(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        out.value = left.value - right.value;
        if (((left.value ^ right.value) & (left.value ^ out.value)) < 0) {
            if (left.value < 0) {
                out.value = Long.MIN_VALUE;
            } else {
                out.value = Long.MAX_VALUE;
            }
        }
    }

    @Override
    public void multiply(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        try {
            out.value = Math.multiplyExact(left.value, right.value);
        } catch (ArithmeticException e) {
            if ((left.value ^ right.value) < 0L) {
                out.value = Long.MIN_VALUE;
            } else {
                out.value = Long.MAX_VALUE;
            }
        }
    }

    @Override
    public void square(@NotNull S64 in, @NotNull S64 out) {
        // 3037000499**2 = 9223372030926249001 < 9223372036854775807 = Long.MAX_VALUE
        // 3037000500**2 = 9223372037000250000 > 9223372036854775807 = Long.MAX_VALUE
        if (in.value < -3037000499L || in.value > 3037000499L) {
            out.value = Long.MAX_VALUE;
        } else {
            out.value = in.value * in.value;
        }
    }

    @Override
    public void triangle(@NotNull S64 in, @NotNull S64 out) {
        // 4294967295 * (4294967295 + 1) / 2 = 9223372034707292160 < 9223372036854775807 = Long.MAX_VALUE
        // 4294967296 * (4294967296 + 1) / 2 = 9223372039002259456 > 9223372036854775807 = Long.MAX_VALUE
        if (in.value < -4294967295L || in.value > 4294967295L) {
            out.value = Long.MAX_VALUE;
        } else {
            long leftValue = in.value;
            long rightValue = Math.incrementExact(leftValue);
            if ((leftValue & 1L) == 0L) {
                leftValue >>= 1;
            } else {
                rightValue >>= 1;
            }
            out.value = leftValue * rightValue;
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
