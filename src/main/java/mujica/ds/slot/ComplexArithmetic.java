package mujica.ds.slot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/6/17.
 */
public class ComplexArithmetic<S extends Real> extends AbstractArithmetic<Complex<S>> {

    @NotNull
    protected final Arithmetic<S> arithmetic;

    @NotNull
    protected S slot3, slot4, slot5, slot6;

    public ComplexArithmetic(@NotNull Arithmetic<S> arithmetic) {
        super();
        this.arithmetic = arithmetic;
        slot0 = newSlot();
        slot1 = newSlot();
        slot2 = newSlot();
        slot3 = arithmetic.newSlot();
        slot4 = arithmetic.newSlot();
        slot5 = arithmetic.newSlot();
        slot6 = arithmetic.newSlot();
    }

    @NotNull
    @Override
    public Complex<S> newSlot() {
        return new Complex<>(arithmetic.newSlot(), arithmetic.newSlot());
    }

    @Override
    public void zero(@NotNull Complex<S> result) {
        arithmetic.zero(result.a);
        arithmetic.zero(result.b);
    }

    @Override
    public void one(@NotNull Complex<S> result) {
        arithmetic.one(result.a);
        arithmetic.zero(result.b);
    }

    @Override
    public int compareSlot(@NotNull Complex<S> a, @NotNull Complex<S> b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int sign(@NotNull Complex<S> variable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void move(@NotNull Complex<S> src, @NotNull Complex<S> dst) {
        arithmetic.move(src.a, dst.a);
        arithmetic.move(src.b, dst.b);
    }

    @Override
    public void negate(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        arithmetic.negate(variable.a, result.a);
        arithmetic.negate(variable.b, result.b);
    }

    @Override
    public void conjugate(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        arithmetic.move(variable.a, result.a);
        arithmetic.negate(variable.b, result.b);
    }

    @Override
    public void conjugateTranspose(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        arithmetic.conjugateTranspose(variable.a, result.a);
        arithmetic.conjugateTranspose(variable.b, slot4);
        arithmetic.negate(slot4, result.b);
    }

    @Override
    public boolean isInvertible(@NotNull Complex<S> variable) {
        return arithmetic.isInvertible(variable.a) || arithmetic.isInvertible(variable.b);
    }

    @Override
    public void invert(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        arithmetic.square(variable.a, slot3);
        arithmetic.square(variable.b, slot4);
        arithmetic.add(slot3, slot4, slot5);
        arithmetic.divide(variable.a, slot5, result.a);
        arithmetic.negate(variable.b, slot4);
        arithmetic.divide(slot4, slot5, result.b);
    }

    @Override
    public void increase(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrease(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(@NotNull Complex<S> left, @NotNull Complex<S> right, @NotNull Complex<S> result) {
        arithmetic.add(left.a, right.a, result.a);
        arithmetic.add(left.b, right.b, result.b);
    }

    @Override
    public void subtract(@NotNull Complex<S> left, @NotNull Complex<S> right, @NotNull Complex<S> result) {
        arithmetic.subtract(left.a, right.a, result.a);
        arithmetic.subtract(left.b, right.b, result.b);
    }

    @Override
    public void multiply(@NotNull Complex<S> left, @NotNull Complex<S> right, @NotNull Complex<S> result) {
        arithmetic.multiply(left.a, right.a, slot3);
        arithmetic.multiply(left.b, right.b, slot4);
        arithmetic.subtract(slot3, slot4, result.a);
        arithmetic.multiply(left.a, right.b, slot3);
        arithmetic.multiply(left.b, right.a, slot4);
        arithmetic.add(slot3, slot4, result.b);
    }

    @Override
    public void square(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        arithmetic.square(variable.a, slot3);
        arithmetic.square(variable.b, slot4);
        arithmetic.subtract(slot3, slot4, result.a);
        arithmetic.multiply(variable.a, variable.b, slot3);
        arithmetic.multiply(variable.b, variable.a, slot4);
        arithmetic.add(slot3, slot4, result.b);
    }

    @Override
    public void triangle(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void divide(@NotNull Complex<S> left, @NotNull Complex<S> right, @NotNull Complex<S> result) {
        arithmetic.square(right.a, slot3);
        arithmetic.square(right.b, slot4);
        arithmetic.add(slot3, slot4, slot6);
        arithmetic.multiply(left.a, right.a, slot3);
        arithmetic.multiply(left.b, right.b, slot4);
        arithmetic.add(slot3, slot4, slot5);
        arithmetic.divide(slot5, slot6, result.a);
        arithmetic.multiply(left.a, right.b, slot3);
        arithmetic.multiply(left.b, right.a, slot4);
        arithmetic.subtract(slot4, slot3, slot5);
        arithmetic.divide(slot5, slot6, result.b);
    }

    @Override
    public void divide(@NotNull Complex<S> left, @NotNull Complex<S> right, @Nullable Complex<S> quotient, @Nullable Complex<S> remainder) {
        throw new UnsupportedOperationException();
    }
}
