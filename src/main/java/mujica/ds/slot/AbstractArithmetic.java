package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;

@CodeHistory(date = "2026/6/28")
@DirectSubclass({PrimitiveArithmetic.class, CompoundArithmetic.class})
public abstract class AbstractArithmetic<S> implements Arithmetic<S> {

    private static final long serialVersionUID = 0xF6DA8D9CAA9EB68EL;

    protected AbstractArithmetic() {
        super();
    }

    @Override
    public void two(@NotNull S out) {
        final S slot = newSlot();
        try {
            one(slot);
            add(slot, slot, out);
        } finally {
            releaseSlot(slot);
        }
    }

    @Override
    public int sign(@NotNull S in) {
        final S slot = newSlot();
        try {
            zero(slot);
            return compareSlot(in, slot);
        } finally {
            releaseSlot(slot);
        }
    }

    @Override
    public void absolute(@NotNull S in, @NotNull S out) {
        if (sign(in) >= 0) {
            move(in, out);
        } else {
            negate(in, out);
        }
    }

    @Override
    public void conjugate(@NotNull S variable, @NotNull S result) {
        move(variable, result);
    }

    @Override
    public void transpose(@NotNull S variable, @NotNull S result) {
        move(variable, result);
    }

    @Override
    public void conjugateTranspose(@NotNull S in, @NotNull S out) {
        move(in, out);
    }

    @Override
    public boolean isInvertible(@NotNull S variable) {
        return sign(variable) != 0;
    }

    @Override
    public int rank(@NotNull S in) {
        return isInvertible(in) ? 1 : 0;
    }

    @Override
    public void increase(@NotNull S in, @NotNull S out) {
        final S slot = newSlot();
        try {
            one(slot);
            add(in, slot, out);
        } finally {
            releaseSlot(slot);
        }
    }

    @Override
    public void decrease(@NotNull S in, @NotNull S out) {
        final S slot = newSlot();
        try {
            one(slot);
            subtract(in, slot, out);
        } finally {
            releaseSlot(slot);
        }
    }

    @Override
    public void subtract(@NotNull S left, @NotNull S right, @NotNull S out) {
        final S slot = newSlot();
        try {
            one(slot);
            negate(right, slot);
            add(left, slot, out);
        } finally {
            releaseSlot(slot);
        }
    }

    @Override
    public void square(@NotNull S in, @NotNull S out) {
        multiply(in, in, out);
    }

    @Override
    public void triangle(@NotNull S in, @NotNull S out) {
        final S increasedVariable = newSlot();
        final S product = newSlot();
        final S two = newSlot();
        try {
            increase(in, increasedVariable);
            multiply(in, increasedVariable, product);
            two(two);
            divide(product, two, out, RoundingMode.UNNECESSARY);
        } finally {
            releaseSlot(increasedVariable);
            releaseSlot(product);
            releaseSlot(two);
        }
    }

    @Override
    public void mean(@NotNull S left, @NotNull S right, @NotNull S result, @NotNull RoundingMode mode) {
        final S sumSlot = newSlot();
        final S twoSlot = newSlot();
        try {
            add(left, right, sumSlot);
            two(twoSlot);
            divide(sumSlot, twoSlot, result, mode);
        } finally {
            releaseSlot(sumSlot);
            releaseSlot(twoSlot);
        }
    }

    @Override
    public void gcd(@NotNull S leftIn, @NotNull S rightIn, @NotNull S result) {
        if (sign(leftIn) <= 0 || sign(rightIn) <= 0) {
            throw new ArithmeticException();
        }
        final S leftSlot = newSlot();
        final S rightSlot = newSlot();
        final S remainderSlot = newSlot();
        try {
            move(leftIn, leftSlot);
            leftIn = leftSlot;
            move(rightIn, rightSlot);
            rightIn = rightSlot;
            S remainder = remainderSlot;
            do {
                divide(leftIn, rightIn, null, remainder);
                S temp = leftIn;
                leftIn = rightIn;
                rightIn = remainder;
                remainder = temp;
            } while (sign(rightIn) != 0);
            move(leftIn, result);
        } finally {
            releaseSlot(leftSlot);
            releaseSlot(rightSlot);
            releaseSlot(remainderSlot);
        }
    }

    @Override
    public void lcm(@NotNull S left, @NotNull S right, @NotNull S result) {
        final S gcdSlot = newSlot();
        final S productSlot = newSlot();
        try {
            gcd(left, right, gcdSlot);
            multiply(left, right, productSlot);
            divide(productSlot, gcdSlot, result, RoundingMode.UNNECESSARY);
        } finally {
            releaseSlot(gcdSlot);
            releaseSlot(productSlot);
        }
    }

    @Override
    public void factorial(@NotNull S variable, @NotNull S result) {
        //
    }

    @Override
    public void doubleFactorial(@NotNull S variable, @NotNull S result) {
        //
    }

    @Override
    public void fibonacci(@NotNull S variable, @NotNull S result) {
        //
    }

    @Override
    public void arrangement(@NotNull S nSlot, @NotNull S mSlot, @NotNull S result) {
        //
    }

    @Override
    public void combination(@NotNull S nSlot, @NotNull S mSlot, @NotNull S result) {
        //
    }

    @Override
    public void squareRoot(@NotNull S variable, @NotNull S result, @NotNull RoundingMode mode) {
        //
    }

    @Override
    public void log2(@NotNull S variable, @NotNull S result, @NotNull RoundingMode mode) {
        //
    }
}
