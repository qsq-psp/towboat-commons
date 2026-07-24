package mujica.ds.i64;

import mujica.ds.slot.PrimitiveArithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2025/6/23", name = "SlotLongContext")
@CodeHistory(date = "2026/6/15")
public class ArithmeticS64 extends PrimitiveArithmetic<I64Slot, long[]> {

    public ArithmeticS64() {
        super();
    }

    @NotNull
    @Override
    public I64Slot newSlot() {
        return new S64();
    }

    @NotNull
    @Override
    public I64Slot cloneSlot(@NotNull I64Slot original) {
        return new S64(original.getI64());
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
    public void load(@NotNull long[] array, int index, @NotNull I64Slot slot) {
        slot.setI64(array[index]);
    }

    @Override
    public void store(@NotNull long[] array, int index, @NotNull I64Slot slot) {
        array[index] = slot.getI64();
    }

    @Override
    public void zero(@NotNull I64Slot result) {
        result.setI64(0L);
    }

    @Override
    public void one(@NotNull I64Slot result) {
        result.setI64(1L);
    }

    @Override
    public void two(@NotNull I64Slot result) {
        result.setI64(2L);
    }

    @Override
    public void min(@NotNull I64Slot result) {
        result.setI64(Long.MIN_VALUE);
    }

    @Override
    public void max(@NotNull I64Slot result) {
        result.setI64(Long.MAX_VALUE);
    }

    @Override
    public void min(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.min(left.getI64(), right.getI64()));
    }

    @Override
    public void max(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.max(left.getI64(), right.getI64()));
    }

    @Override
    public int compareSlot(@NotNull I64Slot a, @NotNull I64Slot b) {
        return Long.compare(a.getI64(), b.getI64());
    }

    @Override
    public int sign(@NotNull I64Slot variable) {
        return Long.signum(variable.getI64());
    }

    @Override
    public void move(@NotNull I64Slot src, @NotNull I64Slot dst) {
        dst.setI64(src.getI64());
    }

    @Override
    public void exchange(@NotNull I64Slot a, @NotNull I64Slot b) {
        a.setI64(b.updateI64(a.getI64()));
    }

    @Override
    public void negate(@NotNull I64Slot variable, @NotNull I64Slot result) {
        result.setI64(Math.negateExact(variable.getI64()));
    }

    @Override
    public void absolute(@NotNull I64Slot variable, @NotNull I64Slot result) {
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
    public boolean isInvertible(@NotNull I64Slot variable) {
        final long value = variable.getI64();
        return value == 1L || value == -1L;
    }

    @Override
    public void invert(@NotNull I64Slot variable, @NotNull I64Slot result) {
        if (isInvertible(variable)) {
            // same as move
            result.setI64(variable.getI64());
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull I64Slot variable, @NotNull I64Slot result) {
        result.setI64(Math.incrementExact(variable.getI64()));
    }

    @Override
    public void decrease(@NotNull I64Slot variable, @NotNull I64Slot result) {
        result.setI64(Math.decrementExact(variable.getI64()));
    }

    @Override
    public void add(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.addExact(left.getI64(), right.getI64()));
    }

    @Override
    public void subtract(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.subtractExact(left.getI64(), right.getI64()));
    }

    @Override
    public void multiply(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
        result.setI64(Math.multiplyExact(left.getI64(), right.getI64()));
    }

    @Override
    public void square(@NotNull I64Slot variable, @NotNull I64Slot result) {
        final long value = variable.getI64();
        result.setI64(Math.multiplyExact(value, value));
    }

    @Override
    public void triangle(@NotNull I64Slot variable, @NotNull I64Slot result) {
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
    public void divide(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result, @NotNull RoundingMode mode) {
        final long leftValue = left.getI64();
        final long rightValue = right.getI64();
        final long resultValue = leftValue / rightValue;
        if (leftValue == rightValue * resultValue) {
            result.setI64(resultValue);
        } else {
            throw new ArithmeticException("can not divide exact");
        }
    }

    @Override
    public void divide(@NotNull I64Slot left, @NotNull I64Slot right, @Nullable I64Slot quotient, @Nullable I64Slot remainder) {
        final long leftValue = left.getI64();
        final long rightValue = right.getI64();
        if (quotient != null) {
            quotient.setI64(leftValue / rightValue);
        }
        if (remainder != null) {
            remainder.setI64(leftValue % rightValue);
        }
    }

    @Override
    public void gcd(@NotNull I64Slot left, @NotNull I64Slot right, @NotNull I64Slot result) {
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
    public void factorial(@NotNull I64Slot variable, @NotNull I64Slot result) {
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
    public void doubleFactorial(@NotNull I64Slot variable, @NotNull I64Slot result) {
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
    public void arrangement(@NotNull I64Slot nSlot, @NotNull I64Slot mSlot, @NotNull I64Slot result) {
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
    public void combination(@NotNull I64Slot nSlot, @NotNull I64Slot mSlot, @NotNull I64Slot result) {
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
