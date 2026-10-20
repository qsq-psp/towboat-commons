package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2026/6/17")
public class FractionArithmetic<S extends Base2Integer> extends CompoundArithmetic<Fraction<S>> {

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
    public void zero(@NotNull Fraction<S> out) {
        arithmetic.zero(out.p);
        arithmetic.one(out.q);
    }

    @Override
    public void one(@NotNull Fraction<S> out) {
        arithmetic.one(out.p);
        arithmetic.one(out.q);
    }

    @Override
    public void two(@NotNull Fraction<S> out) {
        arithmetic.two(out.p);
        arithmetic.one(out.q);
    }

    @Override
    public int compareSlot(@NotNull Fraction<S> left, @NotNull Fraction<S> right) {
        arithmetic.multiply(left.p, right.p, slot3);
        arithmetic.multiply(left.q, right.q, slot4);
        return arithmetic.compareSlot(slot3, slot4);
    }

    @Override
    public int sign(@NotNull Fraction<S> in) {
        return arithmetic.sign(in.p) * arithmetic.sign(in.q);
    }

    @Override
    public void move(@NotNull Fraction<S> in, @NotNull Fraction<S> out) {
        arithmetic.move(in.p, out.p);
        arithmetic.move(in.q, out.q);
    }

    @Override
    public void negate(@NotNull Fraction<S> in, @NotNull Fraction<S> out) {
        arithmetic.negate(in.p, out.p);
        arithmetic.move(in.q, out.q);
    }

    @Override
    public boolean isInvertible(@NotNull Fraction<S> variable) {
        return arithmetic.isInvertible(variable.p);
    }

    @Override
    public void invert(@NotNull Fraction<S> in, @NotNull Fraction<S> out) {
        if (!arithmetic.isInvertible(in.p)) {
            throw new ArithmeticException();
        }
        arithmetic.move(in.p, out.q);
        arithmetic.move(in.q, out.p);
    }

    @Override
    public void increase(@NotNull Fraction<S> in, @NotNull Fraction<S> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrease(@NotNull Fraction<S> in, @NotNull Fraction<S> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @NotNull Fraction<S> out) {
        arithmetic.multiply(left.p, right.q, slot3);
        arithmetic.multiply(left.q, right.p, slot4);
        arithmetic.add(slot3, slot4, slot5);
        arithmetic.multiply(left.q, right.q, slot4);
        arithmetic.gcd(slot4, slot5, slot3);
        arithmetic.divide(slot4, slot3, out.q, RoundingMode.UNNECESSARY);
        arithmetic.divide(slot5, slot3, out.p, RoundingMode.UNNECESSARY);
    }

    @Override
    public void subtract(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @NotNull Fraction<S> out) {
        arithmetic.multiply(left.p, right.q, slot3);
        arithmetic.multiply(left.q, right.p, slot4);
        arithmetic.subtract(slot3, slot4, slot5);
        arithmetic.multiply(left.q, right.q, slot4);
        arithmetic.gcd(slot4, slot5, slot3);
        arithmetic.divide(slot4, slot3, out.q, RoundingMode.UNNECESSARY);
        arithmetic.divide(slot5, slot3, out.p, RoundingMode.UNNECESSARY);
    }

    @Override
    public void multiply(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @NotNull Fraction<S> out) {
        arithmetic.multiply(left.p, right.p, slot3);
        arithmetic.multiply(left.q, right.q, slot4);
        arithmetic.gcd(slot3, slot4, slot5);
        arithmetic.divide(slot3, slot5, out.p, RoundingMode.UNNECESSARY);
        arithmetic.divide(slot4, slot5, out.q, RoundingMode.UNNECESSARY);
    }

    @Override
    public void square(@NotNull Fraction<S> in, @NotNull Fraction<S> out) {
        arithmetic.square(in.p, out.p);
        arithmetic.square(in.q, out.q);
    }

    @Override
    public void divide(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @NotNull Fraction<S> result, @NotNull RoundingMode mode) {
        arithmetic.multiply(left.p, right.q, slot3);
        arithmetic.multiply(left.q, right.p, slot4);
        arithmetic.gcd(slot3, slot4, slot5);
        arithmetic.divide(slot3, slot5, result.p, RoundingMode.UNNECESSARY);
        arithmetic.divide(slot4, slot5, result.q, RoundingMode.UNNECESSARY);
    }

    @Override
    public void divide(@NotNull Fraction<S> left, @NotNull Fraction<S> right, @Nullable Fraction<S> quotient, @Nullable Fraction<S> remainder) {
        throw new UnsupportedOperationException();
    }
}
