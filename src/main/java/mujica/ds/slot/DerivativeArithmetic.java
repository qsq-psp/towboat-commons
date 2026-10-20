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
    public void zero(@NotNull Derivative<S> out) {
        arithmetic.zero(out.x);
        arithmetic.zero(out.d);
    }

    @Override
    public void one(@NotNull Derivative<S> out) {
        arithmetic.one(out.x);
        arithmetic.zero(out.d);
    }

    @Override
    public void two(@NotNull Derivative<S> out) {
        arithmetic.two(out.x);
        arithmetic.two(out.d);
    }

    @Override
    public int compareSlot(@NotNull Derivative<S> left, @NotNull Derivative<S> right) {
        return arithmetic.compareSlot(left.x, right.x);
    }

    @Override
    public int sign(@NotNull Derivative<S> in) {
        return arithmetic.sign(in.x);
    }

    @Override
    public void move(@NotNull Derivative<S> in, @NotNull Derivative<S> out) {
        arithmetic.move(in.x, out.x);
        arithmetic.move(in.d, out.d);
    }

    @Override
    public void negate(@NotNull Derivative<S> in, @NotNull Derivative<S> out) {
        arithmetic.negate(in.x, out.x);
        arithmetic.negate(in.d, out.d);
    }

    @Override
    public void invert(@NotNull Derivative<S> in, @NotNull Derivative<S> out) {

    }

    @Override
    public void increase(@NotNull Derivative<S> in, @NotNull Derivative<S> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrease(@NotNull Derivative<S> in, @NotNull Derivative<S> out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @NotNull Derivative<S> out) {
        arithmetic.add(left.x, right.x, out.x);
        arithmetic.add(left.d, right.d, out.d);
    }

    @Override
    public void subtract(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @NotNull Derivative<S> out) {
        arithmetic.subtract(left.x, right.x, out.x);
        arithmetic.subtract(left.d, right.d, out.d);
    }

    @Override
    public void multiply(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @NotNull Derivative<S> out) {
        arithmetic.multiply(left.x, right.x, out.x);
    }

    @Override
    public void divide(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @NotNull Derivative<S> result, @NotNull RoundingMode mode) {

    }

    @Override
    public void divide(@NotNull Derivative<S> left, @NotNull Derivative<S> right, @Nullable Derivative<S> quotient, @Nullable Derivative<S> remainder) {

    }
}
