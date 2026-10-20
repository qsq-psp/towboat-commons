package mujica.ds.i32;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.slot.PrimitiveArithmetic;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.Arrays;

@CodeHistory(date = "2026/6/29", name = "ArithmeticS32")
@CodeHistory(date = "2026/8/7")
public class S32Arithmetic extends PrimitiveArithmetic<S32, int[]> {

    private static final long serialVersionUID = 0xF017537B325B68A0L;
    
    public static final S32Arithmetic INSTANCE = new S32Arithmetic();

    public S32Arithmetic() {
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
        out.value = 2;
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
    public int compareSlot(@NotNull S32 left, @NotNull S32 right) {
        return Integer.compare(left.value, right.value);
    }

    @Override
    public int sign(@NotNull S32 in) {
        return Integer.signum(in.value);
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
        out.value = Math.negateExact(in.value);
    }

    @Override
    public void absolute(@NotNull S32 in, @NotNull S32 out) {
        if (in.value >= 0) {
            out.value = in.value;
        } else if (in.value != Integer.MIN_VALUE) {
            out.value = -in.value;
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public boolean isInvertible(@NotNull S32 variable) {
        return variable.value == 1 || variable.value == -1;
    }

    @Override
    public void invert(@NotNull S32 in, @NotNull S32 out) {
        if (isInvertible(in)) {
            // same as move
            out.value = in.value;
        } else {
            throw new ArithmeticException();
        }
    }

    @Override
    public void increase(@NotNull S32 in, @NotNull S32 out) {
        out.setI32(Math.incrementExact(in.getI32()));
    }

    @Override
    public void decrease(@NotNull S32 in, @NotNull S32 out) {
        out.setI32(Math.decrementExact(in.getI32()));
    }

    @Override
    public void add(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        out.setI32(Math.addExact(left.getI32(), right.getI32()));
    }

    @Override
    public void subtract(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        out.setI32(Math.subtractExact(left.getI32(), right.getI32()));
    }

    @Override
    public void multiply(@NotNull S32 left, @NotNull S32 right, @NotNull S32 out) {
        out.setI32(Math.multiplyExact(left.getI32(), right.getI32()));
    }

    @Override
    public void square(@NotNull S32 in, @NotNull S32 out) {
        // 46340**2 = 2147395600 < 2147483647 = Integer.MAX_VALUE
        // 46341**2 = 2147488281 < 2147483647 = Integer.MAX_VALUE
        if (in.value < -46340 || in.value > 46340) {
            throw new ArithmeticException();
        }
        out.value = in.value * in.value;
    }

    @Override
    public void triangle(@NotNull S32 in, @NotNull S32 out) {
        // 65535 * (65535 + 1) / 2 = 2147450880 < 2147483647 = Integer.MAX_VALUE
        // 65536 * (65536 + 1) / 2 = 2147516416 > 2147483647 = Integer.MAX_VALUE
        if (in.value < -65536 || in.value > 65535) {
            throw new ArithmeticException();
        }
        int leftValue = in.value;
        int rightValue = Math.incrementExact(leftValue);
        if ((leftValue & 1) == 0) {
            leftValue >>= 1;
        } else {
            rightValue >>= 1;
        }
        out.value = leftValue * rightValue;
    }

    @Override
    public void divide(@NotNull S32 left, @NotNull S32 right, @NotNull S32 quotient, @NotNull RoundingMode mode) {
        quotient.value = left.value / right.value;
        final int productValue = right.value * quotient.value;
        if (left.value == productValue) {
            return;
        }
        switch (mode) {
            case UP:
                if ((left.value ^ right.value) >= 0) {
                    assert quotient.value >= 0;
                    assert quotient.value < Integer.MAX_VALUE;
                    quotient.value++;
                } else {
                    assert quotient.value < 0;
                    assert quotient.value > Integer.MIN_VALUE;
                    quotient.value--;
                }
                break;
            case DOWN:
                break;
            case CEILING:
                if ((left.value ^ right.value) >= 0) {
                    assert quotient.value >= 0;
                    assert quotient.value < Integer.MAX_VALUE;
                    quotient.value++;
                }
                break;
            case FLOOR:
                if ((left.value ^ right.value) < 0) {
                    assert quotient.value < 0;
                    assert quotient.value > Integer.MIN_VALUE;
                    quotient.value--;
                }
                break;
            case UNNECESSARY:
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

    @Override
    public void mean(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result, @NotNull RoundingMode mode) {
        long sum = ((long) left.value) + right.value;
        switch (mode) {
            case UP:
                if (sum >= 0L) {
                    sum++;
                } else {
                    sum--;
                }
                // no break here
            case DOWN:
                sum >>= 1;
                break;
            case CEILING:
                if (sum >= 0L) {
                    sum++;
                }
                sum >>= 1;
                break;
            case FLOOR:
                if (sum < 0L) {
                    sum--;
                }
                sum >>= 1;
                break;
            default:
                throw new IllegalArgumentException();
        }
        result.value = (int) sum;
    }

    @Override
    public void gcd(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result) {
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
    public void lcm(@NotNull S32 left, @NotNull S32 right, @NotNull S32 result) {
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
    public void factorial(@NotNull S32 variable, @NotNull S32 result) {
        final int value = variable.value;
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
        result.value = product;
    }

    @Override
    public void doubleFactorial(@NotNull S32 variable, @NotNull S32 result) {
        int value = variable.value;
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
        result.value = product;
    }

    @Override
    public void arrangement(@NotNull S32 nSlot, @NotNull S32 mSlot, @NotNull S32 result) {
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
    public void combination(@NotNull S32 nSlot, @NotNull S32 mSlot, @NotNull S32 result) {
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

    @CodeHistory(date = "2026/8/8")
    public static class S32HashSAC extends S32Arithmetic implements SlotArrayComparator.HashSAC<S32, int[]> {

        @NotNull
        final CapacityPolicy capacityPolicy;

        public S32HashSAC(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public int slotHash(@NotNull S32 slot) {
            return slot.value;
        }
    }
}
