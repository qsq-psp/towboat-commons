package mujica.ds.i32;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.Arrays;

@CodeHistory(date = "2025/2/27", name = "ClampedMath")
@CodeHistory(date = "2026/7/31")
public class S32ClampedArithmetic extends PrimitiveArithmetic<S32, int[]> {

    public static final S32ClampedArithmetic INSTANCE = new S32ClampedArithmetic();

    public S32ClampedArithmetic() {
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
            return SlotArrayAllocatorI32.EMPTY_ARRAY;
        }
        return new int[length];
    }

    @Override
    public int[] cloneArray(@NotNull int[] original) {
        if (original.length == 0) {
            return SlotArrayAllocatorI32.EMPTY_ARRAY;
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
    public void zero(@NotNull S32 result) {
        result.value = 0;
    }

    @Override
    public void one(@NotNull S32 result) {
        result.value = 1;
    }

    @Override
    public void two(@NotNull S32 result) {
        result.value = 2;
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
    public int compareSlot(@NotNull S32 a, @NotNull S32 b) {
        return Integer.compare(a.value, b.value);
    }

    @Override
    public int sign(@NotNull S32 variable) {
        return Integer.signum(variable.value);
    }

    @Override
    public void move(@NotNull S32 src, @NotNull S32 dst) {
        dst.value = src.value;
    }

    @Override
    public void exchange(@NotNull S32 a, @NotNull S32 b) {
        a.setI32(b.updateI32(a.getI32()));
    }

    @Override
    public void negate(@NotNull S32 variable, @NotNull S32 result) {
        if (variable.value == Integer.MIN_VALUE) {
            result.value = Integer.MAX_VALUE;
        } else {
            result.value = -variable.value;
        }
    }

    @Override
    public void absolute(@NotNull S32 variable, @NotNull S32 result) {
        if (variable.value >= 0) {
            result.value = variable.value;
        } else if (variable.value == Integer.MIN_VALUE) {
            result.value = Integer.MAX_VALUE;
        } else {
            result.value = -variable.value;
        }
    }

    @Override
    public boolean isInvertible(@NotNull S32 variable) {
        return variable.value == 1 || variable.value == -1;
    }

    @Override
    public void invert(@NotNull S32 variable, @NotNull S32 result) {
        if (isInvertible(variable)) {
            // same as move
            result.value = variable.value;
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull S32 variable, @NotNull S32 result) {
        if (variable.value == Integer.MAX_VALUE) {
            result.value = Integer.MAX_VALUE;
        } else {
            result.value = variable.value + 1;
        }
    }

    @Override
    public void decrease(@NotNull S32 variable, @NotNull S32 result) {
        if (variable.value == Integer.MIN_VALUE) {
            result.value = Integer.MIN_VALUE;
        } else {
            result.value = variable.value - 1;
        }
    }

    @Override
    public void add(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result) {
        result.value = left.value + right.value;
        if (((left.value ^ result.value) & (right.value ^ result.value)) < 0) {
            if (left.value < 0) {
                result.value = Integer.MIN_VALUE;
            } else {
                result.value = Integer.MAX_VALUE;
            }
        }
    }

    @Override
    public void subtract(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result) {
        result.value = left.value - right.value;
        if (((left.value ^ right.value) & (left.value ^ result.value)) < 0) {
            if (left.value < 0) {
                result.value = Integer.MIN_VALUE;
            } else {
                result.value = Integer.MAX_VALUE;
            }
        }
    }

    @Override
    public void multiply(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result) {
        final long resultValue = ((long) left.value) * right.value;
        result.value = (int) resultValue;
        if (result.value != resultValue) {
            if (resultValue < 0) {
                result.value = Integer.MIN_VALUE;
            } else {
                result.value = Integer.MAX_VALUE;
            }
        }
    }

    @Override
    public void square(@NotNull S32 variable, @NotNull S32 result) {
        // 46340**2 = 2147395600 < 2147483647 = Integer.MAX_VALUE
        // 46341**2 = 2147488281 < 2147483647 = Integer.MAX_VALUE
        if (variable.value < -46340 || variable.value > 46340) {
            result.value = Integer.MAX_VALUE;
        } else {
            result.value = variable.value * variable.value;
        }
    }

    @Override
    public void triangle(@NotNull S32 variable, @NotNull S32 result) {
        // 65535 * (65535 + 1) / 2 = 2147450880 < 2147483647 = Integer.MAX_VALUE
        // 65536 * (65536 + 1) / 2 = 2147516416 > 2147483647 = Integer.MAX_VALUE
        if (variable.value < -65536 || variable.value > 65535) {
            result.value = Integer.MAX_VALUE;
        } else {
            int leftValue = variable.value;
            int rightValue = Math.incrementExact(leftValue);
            if ((leftValue & 1) == 0) {
                leftValue >>= 1;
            } else {
                rightValue >>= 1;
            }
            result.value = leftValue * rightValue;
        }
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
