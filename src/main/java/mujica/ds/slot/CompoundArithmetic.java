package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;

@CodeHistory(date = "2026/9/5")
@DirectSubclass({FractionArithmetic.class, ComplexArithmetic.class, DerivativeArithmetic.class})
public abstract class CompoundArithmetic<S> extends AbstractArithmetic<S> {

    protected S slot0, slot1, slot2;

    protected CompoundArithmetic() {
        super();
    }

    @Override
    public void two(@NotNull S result) {
        one(slot1);
        add(slot1, slot1, result);
    }

    @Override
    public int sign(@NotNull S variable) {
        zero(slot0);
        return compareSlot(variable, slot0);
    }

    @Override
    public void absolute(@NotNull S variable, @NotNull S result) {
        if (sign(variable) >= 0) {
            move(variable, result);
        } else {
            negate(variable, result);
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
    public void conjugateTranspose(@NotNull S variable, @NotNull S result) {
        move(variable, result);
    }

    @Override
    public boolean isInvertible(@NotNull S variable) {
        return sign(variable) != 0;
    }

    @Override
    public void increase(@NotNull S variable, @NotNull S result) {
        one(slot1);
        add(variable, slot1, result);
    }

    @Override
    public void decrease(@NotNull S variable, @NotNull S result) {
        one(slot1);
        subtract(variable, slot1, result);
    }

    @Override
    public void subtract(@NotNull S left, @NotNull S right, @NotNull S result) {
        negate(right, slot0);
        add(left, slot0, result);
    }

    @Override
    public void square(@NotNull S variable, @NotNull S result) {
        multiply(variable, variable, result);
    }

    @Override
    public void triangle(@NotNull S variable, @NotNull S result) {
        increase(variable, slot1);
        multiply(variable, slot1, slot0);
        two(slot2);
        divide(slot0, slot2, result, RoundingMode.UNNECESSARY);
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
    public void gcd(@NotNull S left, @NotNull S right, @NotNull S result) {
        if (sign(left) <= 0 || sign(right) <= 0) {
            throw new ArithmeticException();
        }
        move(left, slot0);
        left = slot0;
        move(right, slot1);
        right = slot1;
        S remainder = slot2;
        do {
            divide(left, right, null, remainder);
            S temp = left;
            left = right;
            right = remainder;
            remainder = temp;
        } while (sign(right) != 0);
        move(left, result);
    }

    @Override
    public void lcm(@NotNull S left, @NotNull S right, @NotNull S result) {
        gcd(left, right, slot2);
        multiply(left, right, slot1);
        divide(slot1, slot2, result, RoundingMode.UNNECESSARY);
    }
}
