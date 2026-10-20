package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2026/6/17")
public class ComplexArithmetic<S extends Real> extends CompoundArithmetic<Complex<S>> {

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
    public void zero(@NotNull Complex<S> out) {
        arithmetic.zero(out.a);
        arithmetic.zero(out.b);
    }

    @Override
    public void one(@NotNull Complex<S> out) {
        arithmetic.one(out.a);
        arithmetic.zero(out.b);
    }

    @Override
    public void two(@NotNull Complex<S> out) {
        arithmetic.two(out.a);
        arithmetic.two(out.b);
    }

    @Override
    public int compareSlot(@NotNull Complex<S> left, @NotNull Complex<S> right) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int sign(@NotNull Complex<S> in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void move(@NotNull Complex<S> in, @NotNull Complex<S> out) {
        arithmetic.move(in.a, out.a);
        arithmetic.move(in.b, out.b);
    }

    @Override
    public void negate(@NotNull Complex<S> in, @NotNull Complex<S> out) {
        arithmetic.negate(in.a, out.a);
        arithmetic.negate(in.b, out.b);
    }

    @Override
    public void conjugate(@NotNull Complex<S> variable, @NotNull Complex<S> result) {
        arithmetic.move(variable.a, result.a);
        arithmetic.negate(variable.b, result.b);
    }

    @Override
    public void conjugateTranspose(@NotNull Complex<S> in, @NotNull Complex<S> out) {
        arithmetic.conjugateTranspose(in.a, out.a);
        arithmetic.conjugateTranspose(in.b, slot4);
        arithmetic.negate(slot4, out.b);
    }

    @Override
    public boolean isInvertible(@NotNull Complex<S> variable) {
        return arithmetic.isInvertible(variable.a) || arithmetic.isInvertible(variable.b);
    }

    @Override
    public void invert(@NotNull Complex<S> in, @NotNull Complex<S> out) {
        arithmetic.square(in.a, slot3);
        arithmetic.square(in.b, slot4);
        arithmetic.add(slot3, slot4, slot5);
        arithmetic.divide(in.a, slot5, out.a, RoundingMode.UNNECESSARY);
        arithmetic.negate(in.b, slot4);
        arithmetic.divide(slot4, slot5, out.b, RoundingMode.UNNECESSARY);
    }

    @Override
    public void increase(@NotNull Complex<S> in, @NotNull Complex<S> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrease(@NotNull Complex<S> in, @NotNull Complex<S> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(@NotNull Complex<S> left, @NotNull Complex<S> right, @NotNull Complex<S> out) {
        arithmetic.add(left.a, right.a, out.a);
        arithmetic.add(left.b, right.b, out.b);
    }

    @Override
    public void subtract(@NotNull Complex<S> left, @NotNull Complex<S> right, @NotNull Complex<S> out) {
        arithmetic.subtract(left.a, right.a, out.a);
        arithmetic.subtract(left.b, right.b, out.b);
    }

    @Override
    public void multiply(@NotNull Complex<S> left, @NotNull Complex<S> right, @NotNull Complex<S> out) {
        arithmetic.multiply(left.a, right.a, slot3);
        arithmetic.multiply(left.b, right.b, slot4);
        arithmetic.subtract(slot3, slot4, out.a);
        arithmetic.multiply(left.a, right.b, slot3);
        arithmetic.multiply(left.b, right.a, slot4);
        arithmetic.add(slot3, slot4, out.b);
    }

    @Override
    public void square(@NotNull Complex<S> in, @NotNull Complex<S> out) {
        arithmetic.square(in.a, slot3);
        arithmetic.square(in.b, slot4);
        arithmetic.subtract(slot3, slot4, out.a);
        arithmetic.multiply(in.a, in.b, slot3);
        arithmetic.multiply(in.b, in.a, slot4);
        arithmetic.add(slot3, slot4, out.b);
    }

    @Override
    public void triangle(@NotNull Complex<S> in, @NotNull Complex<S> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void divide(@NotNull Complex<S> left, @NotNull Complex<S> right, @NotNull Complex<S> result, @NotNull RoundingMode mode) {
        arithmetic.square(right.a, slot3);
        arithmetic.square(right.b, slot4);
        arithmetic.add(slot3, slot4, slot6);
        arithmetic.multiply(left.a, right.a, slot3);
        arithmetic.multiply(left.b, right.b, slot4);
        arithmetic.add(slot3, slot4, slot5);
        arithmetic.divide(slot5, slot6, result.a, mode);
        arithmetic.multiply(left.a, right.b, slot3);
        arithmetic.multiply(left.b, right.a, slot4);
        arithmetic.subtract(slot4, slot3, slot5);
        arithmetic.divide(slot5, slot6, result.b, mode);
    }

    @Override
    public void divide(@NotNull Complex<S> left, @NotNull Complex<S> right, @Nullable Complex<S> quotient, @Nullable Complex<S> remainder) {
        throw new UnsupportedOperationException();
    }
}
