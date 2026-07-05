package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/6/17.
 */
@CodeHistory(date = "2026/6/17")
public class FractionArithmetic<S extends Base2Integer> extends AbstractArithmetic<Fraction<S>> {

    @NotNull
    protected final Arithmetic<S> arithmetic;

    protected S slot3, slot4, slot5;

    public FractionArithmetic(@NotNull Arithmetic<S> arithmetic) {
        super();
        this.arithmetic = arithmetic;
        slot0 = newSlot();
        slot1 = newSlot();
        slot2 = newSlot();
        slot3 = arithmetic.newSlot();
        slot4 = arithmetic.newSlot();
        slot5 = arithmetic.newSlot();
    }

    @NotNull
    @Override
    public Fraction<S> newSlot() {
        return new Fraction<>(arithmetic.newSlot(), arithmetic.newSlot());
    }

    @Override
    public void zero(@NotNull Fraction<S> result) {
        arithmetic.zero(result.p);
        arithmetic.one(result.q);
    }

    @Override
    public void one(@NotNull Fraction<S> result) {
        arithmetic.one(result.p);
        arithmetic.one(result.q);
    }

    @Override
    public int compareSlot(@NotNull Fraction<S> a, @NotNull Fraction<S> b) {
        arithmetic.multiply(a.p, b.p, slot3);
        arithmetic.multiply(a.q, b.q, slot4);
        return arithmetic.compareSlot(slot3, slot4);
    }

    @Override
    public int sign(@NotNull Fraction<S> variable) {
        return arithmetic.sign(variable.p) * arithmetic.sign(variable.q);
    }

    @Override
    public void move(@NotNull Fraction<S> src, @NotNull Fraction<S> dst) {
        arithmetic.move(src.p, dst.p);
        arithmetic.move(src.q, dst.q);
    }

    @Override
    public void negate(@NotNull Fraction<S> variable, @NotNull Fraction<S> result) {
        arithmetic.negate(variable.p, result.p);
        arithmetic.move(variable.q, result.q);
    }

    @Override
    public boolean isInvertible(@NotNull Fraction<S> variable) {
        return arithmetic.isInvertible(variable.p);
    }

    @Override
    public void invert(@NotNull Fraction<S> variable, @NotNull Fraction<S> result) {
        if (!arithmetic.isInvertible(variable.p)) {
            throw new ArithmeticException();
        }
        arithmetic.move(variable.p, result.q);
        arithmetic.move(variable.q, result.p);
    }

    @Override
    public void increase(@NotNull Fraction<S> variable, @NotNull Fraction<S> result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrease(@NotNull Fraction<S> variable, @NotNull Fraction<S> result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @NotNull Fraction<S> result) {
        arithmetic.multiply(left.p, right.q, slot3);
        arithmetic.multiply(left.q, right.p, slot4);
        arithmetic.add(slot3, slot4, slot5);
        arithmetic.multiply(left.q, right.q, slot4);
        arithmetic.gcd(slot4, slot5, slot3);
        arithmetic.divide(slot4, slot3, result.q);
        arithmetic.divide(slot5, slot3, result.p);
    }

    @Override
    public void subtract(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @NotNull Fraction<S> result) {
        arithmetic.multiply(left.p, right.q, slot3);
        arithmetic.multiply(left.q, right.p, slot4);
        arithmetic.subtract(slot3, slot4, slot5);
        arithmetic.multiply(left.q, right.q, slot4);
        arithmetic.gcd(slot4, slot5, slot3);
        arithmetic.divide(slot4, slot3, result.q);
        arithmetic.divide(slot5, slot3, result.p);
    }

    @Override
    public void multiply(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @NotNull Fraction<S> result) {
        arithmetic.multiply(left.p, right.p, slot3);
        arithmetic.multiply(left.q, right.q, slot4);
        arithmetic.gcd(slot3, slot4, slot5);
        arithmetic.divide(slot3, slot5, result.p);
        arithmetic.divide(slot4, slot5, result.q);
    }

    @Override
    public void square(@NotNull Fraction<S> variable, @NotNull Fraction<S> result) {
        arithmetic.square(variable.p, result.p);
        arithmetic.square(variable.q, result.q);
    }

    @Override
    public void divide(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @NotNull Fraction<S> result) {
        arithmetic.multiply(left.p, right.q, slot3);
        arithmetic.multiply(left.q, right.p, slot4);
        arithmetic.gcd(slot3, slot4, slot5);
        arithmetic.divide(slot3, slot5, result.p);
        arithmetic.divide(slot4, slot5, result.q);
    }

    @Override
    public void divide(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @Nullable Fraction<S> quotient, @Nullable Fraction<S> remainder) {
        throw new UnsupportedOperationException();
    }
}
