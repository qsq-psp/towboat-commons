package mujica.ds.i64;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2025/6/23", name = "SlotLongContext")
@CodeHistory(date = "2026/6/15")
public class ArithmeticS64 extends PrimitiveArithmetic<S64, long[]> {

    public static final ArithmeticS64 INSTANCE = new ArithmeticS64();

    public ArithmeticS64() {
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
        result.setI64(Math.negateExact(variable.getI64()));
    }

    @Override
    public void absolute(@NotNull S64 variable, @NotNull S64 result) {
        final long value = variable.getI64();
        if (value >= 0L) {
            result.setI64(value);
        } else if (value != Long.MIN_VALUE) {
            result.setI64(-value);
        } else {
            throw new ArithmeticException();
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
        result.setI64(Math.incrementExact(variable.getI64()));
    }

    @Override
    public void decrease(@NotNull S64 variable, @NotNull S64 result) {
        result.setI64(Math.decrementExact(variable.getI64()));
    }

    @Override
    public void add(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        result.setI64(Math.addExact(left.getI64(), right.getI64()));
    }

    @Override
    public void subtract(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        result.setI64(Math.subtractExact(left.getI64(), right.getI64()));
    }

    @Override
    public void multiply(@NotNull S64 left, @NotNull S64 right, @NotNull S64 result) {
        result.value = Math.multiplyExact(left.value, right.value);
    }

    @Override
    public void square(@NotNull S64 variable, @NotNull S64 result) {
        final long value = variable.getI64();
        result.setI64(Math.multiplyExact(value, value));
    }

    @Override
    public void triangle(@NotNull S64 variable, @NotNull S64 result) {
        long left = variable.getI64();
        long right = Math.incrementExact(left);
        if ((left & 1L) == 0) {
            left >>= 1;
        } else {
            right >>= 1;
        }
        result.setI64(Math.multiplyExact(left, right));
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
