package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;

/**
 * Created on 2026/6/28.
 */
@CodeHistory(date = "2026/6/28")
public abstract class AbstractArithmetic<S> implements Arithmetic<S> {

    protected S slot0, slot1, slot2;

    protected AbstractArithmetic() {
        super();
    }

    @Override
    public void two(@NotNull S result) {
        final S slot1 = newSlot();
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
        one(slot1);
        add(slot1, slot1, slot2);
        divide(slot0, slot2, result, RoundingMode.UNNECESSARY);
    }

    @Override
    public void mean(@NotNull S left, @NotNull S right, @NotNull S result, @NotNull RoundingMode mode) {
        add(left, right, slot0);
        one(slot1);
        add(slot1, slot1, slot2);
        divide(slot0, slot2, result, mode);
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
