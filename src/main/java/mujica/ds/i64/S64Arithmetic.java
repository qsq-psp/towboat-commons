package mujica.ds.i64;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2025/6/23", name = "SlotLongContext")
@CodeHistory(date = "2026/6/15", name = "ArithmeticS64")
@CodeHistory(date = "2026/8/8")
public class S64Arithmetic extends PrimitiveArithmetic<S64, long[]> {

    public static final S64Arithmetic INSTANCE = new S64Arithmetic();

    public S64Arithmetic() {
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
        out.value = Math.negateExact(in.value);
    }

    @Override
    public void absolute(@NotNull S64 in, @NotNull S64 out) {
        if (in.value >= 0L) {
            out.value = in.value;
        } else if (in.value == Long.MIN_VALUE) {
            throw new ArithmeticException();
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
            throw new ArithmeticException();
        } else {
            out.value = in.value + 1L;
        }
    }

    @Override
    public void decrease(@NotNull S64 in, @NotNull S64 out) {
        if (in.value == Long.MIN_VALUE) {
            throw new ArithmeticException();
        } else {
            out.value = in.value - 1L;
        }
    }

    @Override
    public void add(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        out.value = Math.addExact(left.value, right.value);
    }

    @Override
    public void subtract(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        out.value = Math.subtractExact(left.value, right.value);
    }

    @Override
    public void multiply(@NotNull S64 left, @NotNull S64 right, @NotNull S64 out) {
        out.value = Math.multiplyExact(left.value, right.value);
    }

    @Override
    public void square(@NotNull S64 in, @NotNull S64 out) {
        // 3037000499**2 = 9223372030926249001 < 9223372036854775807 = Long.MAX_VALUE
        // 3037000500**2 = 9223372037000250000 > 9223372036854775807 = Long.MAX_VALUE
        if (in.value < -3037000499L || in.value > 3037000499L) {
            throw new ArithmeticException();
        } else {
            out.value = in.value * in.value;
        }
    }

    @Override
    public void triangle(@NotNull S64 in, @NotNull S64 out) {
        // 4294967295 * (4294967295 + 1) / 2 = 9223372034707292160 < 9223372036854775807 = Long.MAX_VALUE
        // 4294967296 * (4294967296 + 1) / 2 = 9223372039002259456 > 9223372036854775807 = Long.MAX_VALUE
        if (in.value < -4294967295L || in.value > 4294967295L) {
            throw new ArithmeticException();
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
    public void divide(@NotNull S64 left, @NotNull S64 right, @NotNull S64 quotient, @NotNull RoundingMode mode) {
        quotient.value = left.value / right.value;
        final long productValue = right.value * quotient.value;
        if (left.value == productValue) {
            return;
        }
        switch (mode) {
            case UP:
                if ((left.value ^ right.value) >= 0L) {
                    assert quotient.value >= 0L;
                    assert quotient.value < Long.MAX_VALUE;
                    quotient.value++;
                } else {
                    assert quotient.value < 0L;
                    assert quotient.value > Long.MIN_VALUE;
                    quotient.value--;
                }
                break;
            case DOWN:
                break;
            case CEILING:
                if ((left.value ^ right.value) >= 0L) {
                    assert quotient.value >= 0L;
                    assert quotient.value < Long.MAX_VALUE;
                    quotient.value++;
                }
                break;
            case FLOOR:
                if ((left.value ^ right.value) < 0L) {
                    assert quotient.value < 0L;
                    assert quotient.value > Long.MIN_VALUE;
                    quotient.value--;
                }
                break;
            case UNNECESSARY:
                throw new ArithmeticException("can not divide exact");
        }
    }

    @Override
    public void divide(@NotNull S64 left, @NotNull S64 right, @Nullable S64 quotient, @Nullable S64 remainder) {
        if (quotient != null) {
            quotient.value = left.value / right.value;
        }
        if (remainder != null) {
            remainder.value = left.value % right.value;
        }
    }

    @Override
    public void mean(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result, @NotNull RoundingMode mode) {
        long sum = left.value + right.value;
        boolean sign;
        if (left.value < 0L) {
            if (right.value < 0L) {
                sign = true; // negative
            } else {
                sign = sum < 0L;
            }
        } else {
            if (right.value < 0L) {
                sign = sum < 0L;
            } else {
                sign = false; // positive
            }
        }
        switch (mode) {
            case UP:
                if (sign) {
                    sum--;
                } else {
                    sum++;
                }
                // no break here
            case DOWN:
                sum >>>= 1;
                if (sign) {
                    sum |= Long.MIN_VALUE;
                }
                break;
            case CEILING:
                if (sign) {
                    sum >>>= 1;
                    sum |= Long.MIN_VALUE;
                } else {
                    sum++;
                    sum >>>= 1;
                }
                break;
            case FLOOR:
                if (sign) {
                    sum--;
                    sum >>>= 1;
                    sum |= Long.MIN_VALUE;
                } else {
                    sum >>>= 1;
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        result.value = sum;
    }

    @Override
    public void gcd(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        long leftValue = left.getI64();
        long rightValue = right.getI64();
        if (leftValue <= 0L || rightValue <= 0L) {
            throw new ArithmeticException();
        }
        while (rightValue != 0) {
            long remainder = leftValue % rightValue;
            leftValue = rightValue;
            rightValue = remainder;
        }
        result.setI64(leftValue);
    }

    @Override
    public void factorial(@NotNull S64 variable, @NotNull S64 result) {
        final long value = variable.getI64();
        if (value < 0L) {
            throw new ArithmeticException();
        }
        // 20! = 2432902008176640000 < 9223372036854775807 = Long.MAX_VALUE
        // 21! = 51090942171709440000 > 9223372036854775807 = Long.MAX_VALUE
        if (value > 20L) {
            throw new ArithmeticException();
        }
        long product = 1L;
        for (long index = 2L; index <= value; index++) {
            product *= index;
        }
        result.setI64(product);
    }

    @Override
    public void doubleFactorial(@NotNull S64 variable, @NotNull S64 result) {
        long value = variable.getI64();
        if (value < 0L) {
            throw new ArithmeticException();
        }
        // 33!! = 6332659870762850625 < 9223372036854775807 = Long.MAX_VALUE
        // 34!! = 46620662575398912000 > 9223372036854775807 = Long.MAX_VALUE
        if (value > 33L) {
            throw new ArithmeticException();
        }
        long product = 1L;
        while (value >= 2L) {
            product *= value;
            value -= 2L;
        }
        result.setI64(product);
    }

    @Override
    public void arrangement(@NotNull S64 nSlot, @NotNull S64 mSlot, @NotNull S64 result) {
        final long n = nSlot.getI64();
        final long m = mSlot.getI64();
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException();
        }
        long product = 1L;
        for (long i = n - m + 1; i <= n; i++) {
            product = Math.multiplyExact(product, i);
        }
        result.setI64(product);
    }

    @Override
    public void combination(@NotNull S64 nSlot, @NotNull S64 mSlot, @NotNull S64 result) {
        final long n = nSlot.getI64();
        long m = mSlot.getI64();
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException();
        }
        m = Math.min(m, n - m); // C(n, m) = C(n, n - m)
        if (m == 0) {
            result.setI64(1L);
            return;
        }
        long w = n - m + 1;
        for (long i = m - 2; i >= 0; i--) {
            w = w * (n - i) / (m - i);
            if (w > Integer.MAX_VALUE) { // todo
                throw new ArithmeticException();
            }
        }
        result.setI64(w);
    }
}
