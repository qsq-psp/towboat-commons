package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2026/9/6")
public class DerivativeArithmetic<S extends Real> extends CompoundArithmetic<Derivative<S>> {

    @NotNull
    protected final Arithmetic<S> arithmetic;

    public DerivativeArithmetic(@NotNull Arithmetic<S> arithmetic) {
        super();
        this.arithmetic = arithmetic;
    }

    @NotNull
    @Override
    public Derivative<S> newSlot() {
        return new Derivative<>(arithmetic.newSlot(), arithmetic.newSlot());
    }

    @Override
    public void zero(@NotNull Derivative<S> result) {
        arithmetic.zero(result.x);
        arithmetic.zero(result.d);
    }

    @Override
    public void one(@NotNull Derivative<S> result) {
        arithmetic.one(result.x);
        arithmetic.zero(result.d);
    }

    @Override
    public void two(@NotNull Derivative<S> result) {
        arithmetic.two(result.x);
        arithmetic.two(result.d);
    }

    @Override
    public int compareSlot(@NotNull Derivative<S> a, @NotNull Derivative<S> b) {
        return arithmetic.compareSlot(a.x, b.x);
    }

    @Override
    public int sign(@NotNull Derivative<S> variable) {
        return arithmetic.sign(variable.x);
    }

    @Override
    public void move(@NotNull Derivative<S> src, @NotNull Derivative<S> dst) {
        arithmetic.move(src.x, dst.x);
        arithmetic.move(src.d, dst.d);
    }

    @Override
    public void negate(@NotNull Derivative<S> variable, @NotNull Derivative<S> result) {
        arithmetic.negate(variable.x, result.x);
        arithmetic.negate(variable.d, result.d);
    }

    @Override
    public void invert(@NotNull Derivative<S> variable, @NotNull Derivative<S> result) {

    }

    @Override
    public void increase(@NotNull Derivative<S> variable, @NotNull Derivative<S> result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrease(@NotNull Derivative<S> variable, @NotNull Derivative<S> result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @NotNull Derivative<S> result) {
        arithmetic.add(left.x, right.x, result.x);
        arithmetic.add(left.d, right.d, result.d);
    }

    @Override
    public void subtract(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @NotNull Derivative<S> result) {
        arithmetic.subtract(left.x, right.x, result.x);
        arithmetic.subtract(left.d, right.d, result.d);
    }

    @Override
    public void multiply(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @NotNull Derivative<S> result) {
        arithmetic.multiply(left.x, right.x, result.x);
    }

    @Override
    public void divide(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @NotNull Derivative<S> result, @NotNull RoundingMode mode) {

    }

    @Override
    public void divide(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @Nullable Derivative<S> quotient, @Nullable Derivative<S> remainder) {

    }
}
