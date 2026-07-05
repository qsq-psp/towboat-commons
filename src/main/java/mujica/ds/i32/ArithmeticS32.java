package mujica.ds.i32;

import mujica.ds.slot.AbstractArithmetic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/6/29.
 */
public class ArithmeticS32 extends AbstractArithmetic<I32Slot> {

    public ArithmeticS32() {
        super();
    }

    @NotNull
    @Override
    public I32Slot newSlot() {
        return new S32();
    }

    @Override
    public void zero(@NotNull I32Slot result) {
        result.setI32(0);
    }

    @Override
    public void one(@NotNull I32Slot result) {
        result.setI32(1);
    }

    @Override
    public int compareSlot(@NotNull I32Slot a, @NotNull I32Slot b) {
        return Integer.compare(a.getI32(), b.getI32());
    }

    @Override
    public int sign(@NotNull I32Slot variable) {
        return Integer.signum(variable.getI32());
    }

    @Override
    public void move(@NotNull I32Slot src, @NotNull I32Slot dst) {
        dst.setI32(src.getI32());
    }

    @Override
    public void negate(@NotNull I32Slot variable, @NotNull I32Slot result) {
        result.setI32(Math.negateExact(variable.getI32()));
    }

    @Override
    public void absolute(@NotNull I32Slot variable, @NotNull I32Slot result) {
        final int value = variable.getI32();
        if (value >= 0) {
            result.setI32(value);
        } else if (value != Integer.MIN_VALUE) {
            result.setI32(-value);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public boolean isInvertible(@NotNull I32Slot variable) {
        final int value = variable.getI32();
        return value == 1 || value == -1;
    }

    @Override
    public void invert(@NotNull I32Slot variable, @NotNull I32Slot result) {
        if (isInvertible(variable)) {
            // same as move
            result.setI32(variable.getI32());
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull I32Slot variable, @NotNull I32Slot result) {
        result.setI32(Math.incrementExact(variable.getI32()));
    }

    @Override
    public void decrease(@NotNull I32Slot variable, @NotNull I32Slot result) {
        result.setI32(Math.decrementExact(variable.getI32()));
    }

    @Override
    public void add(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        result.setI32(Math.addExact(left.getI32(), result.getI32()));
    }

    @Override
    public void subtract(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        result.setI32(Math.subtractExact(left.getI32(), result.getI32()));
    }

    @Override
    public void multiply(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        result.setI32(Math.multiplyExact(left.getI32(), result.getI32()));
    }

    @Override
    public void square(@NotNull I32Slot variable, @NotNull I32Slot result) {
        final int value = variable.getI32();
        result.setI32(Math.multiplyExact(value, value));
    }

    @Override
    public void triangle(@NotNull I32Slot variable, @NotNull I32Slot result) {
        int left = variable.getI32();
        int right = Math.incrementExact(left);
        if ((left & 1) == 0) {
            left >>= 1;
        } else {
            right >>= 1;
        }
        result.setI32(Math.multiplyExact(left, right));
    }

    @Override
    public void divide(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        final int leftValue = left.getI32();
        final int rightValue = right.getI32();
        final int resultValue = leftValue / rightValue;
        if (leftValue == rightValue * resultValue) {
            result.setI32(resultValue);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void divide(@NotNull I32Slot left, @NotNull I32Slot right, @Nullable I32Slot quotient, @Nullable I32Slot remainder) {
        final int leftValue = left.getI32();
        final int rightValue = right.getI32();
        if (quotient != null) {
            quotient.setI32(leftValue / rightValue);
        }
        if (remainder != null) {
            remainder.setI32(leftValue % rightValue);
        }
    }

    @Override
    public void gcd(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        int leftValue = left.getI32();
        int rightValue = right.getI32();
        if (leftValue <= 0 || rightValue <= 0) {
            throw new ArithmeticException();
        }
        while (rightValue != 0) {
            int remainder = leftValue % rightValue;
            leftValue = rightValue;
            rightValue = remainder;
        }
        result.setI32(leftValue);
    }

    @Override
    public void lcm(@NotNull I32Slot left, @NotNull I32Slot right, @NotNull I32Slot result) {
        int leftValue = left.getI32();
        int rightValue = right.getI32();
        long product = ((long) leftValue) * rightValue;
        if (leftValue <= 0 || rightValue <= 0) {
            throw new ArithmeticException();
        }
        while (rightValue != 0) {
            int remainder = leftValue % rightValue;
            leftValue = rightValue;
            rightValue = remainder;
        }
        product /= leftValue;
        if (product <= Integer.MAX_VALUE) {
            result.setI32((int) product);
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void factorial(@NotNull I32Slot variable, @NotNull I32Slot result) {
        final int value = variable.getI32();
        if (value < 0) {
            throw new ArithmeticException();
        }
        // 12! = 479001600 < 2147483647 = Integer.MAX_VALUE
        // 13! = 6227020800 > 2147483647 = Integer.MAX_VALUE
        if (value > 12) {
            throw new ArithmeticException();
        }
        int product = 1;
        for (int index = 2; index <= value; index++) {
            product *= index;
        }
        result.setI32(product);
    }

    @Override
    public void doubleFactorial(@NotNull I32Slot variable, @NotNull I32Slot result) {
        int value = variable.getI32();
        if (value < 0) {
            throw new ArithmeticException();
        }
        // 19!! = 654729075 < 2147483647 = Integer.MAX_VALUE
        // 20!! = 3715891200 > 2147483647 = Integer.MAX_VALUE
        if (value > 19) {
            throw new ArithmeticException();
        }
        int product = 1;
        while (value >= 2) {
            product *= value;
            value -= 2;
        }
        result.setI32(product);
    }

    @Override
    public void arrangement(@NotNull I32Slot nSlot, @NotNull I32Slot mSlot, @NotNull I32Slot result) {
        final int n = nSlot.getI32();
        final int m = mSlot.getI32();
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException();
        }
        int product = 1;
        for (int i = n - m + 1; i <= n; i++) {
            product = Math.multiplyExact(product, i);
        }
        result.setI32(product);
    }

    @Override
    public void combination(@NotNull I32Slot nSlot, @NotNull I32Slot mSlot, @NotNull I32Slot result) {
        final int n = nSlot.getI32();
        int m = mSlot.getI32();
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException();
        }
        m = Math.min(m, n - m); // C(n, m) = C(n, n - m)
        if (m == 0) {
            result.setI32(1);
            return;
        }
        long w = n - m + 1;
        for (int i = m - 2; i >= 0; i--) {
            w = w * (n - i) / (m - i);
            if (w > Integer.MAX_VALUE) {
                throw new ArithmeticException();
            }
        }
        result.setI32((int) w);
    }
}
