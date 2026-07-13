package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

@CodeHistory(date = "2026/6/14")
public class Base2IntegerArithmetic<S extends Base2Integer> extends AbstractArithmetic<S> {

    @NotNull
    @Override
    public S newSlot() {
        return null;
    }

    @Override
    public void zero(@NotNull S result) {
        final Base2Iterator iterator = result.magnitude();
        while (iterator.next()) {
            iterator.setBit(false);
        }
        result.signBit().setBit(false);
    }

    @Override
    public void one(@NotNull S result) {
        final Base2Iterator iterator = result.magnitude();
        if (iterator.next()) {
            iterator.setBit(true);
        }
        while (iterator.next()) {
            iterator.setBit(false);
        }
        result.signBit().setBit(false);
    }

    @Override
    public int compareSlot(@NotNull S a, @NotNull S b) {
        return 0;
    }

    @Override
    public void move(@NotNull S src, @NotNull S dst) {

    }

    @Override
    public void negate(@NotNull S variable, @NotNull S result) {

    }

    @Override
    public void invert(@NotNull S variable, @NotNull S result) {

    }

    @Override
    public void add(@NotNull S left, @NotNull S right, @NotNull S result) {

    }

    @Override
    public void multiply(@NotNull S left, @NotNull S right, @NotNull S result) {

    }

    @Override
    public void divide(@NotNull S left, @NotNull S right, @NotNull S result, @NotNull RoundingMode mode) {

    }

    @Override
    public void divide(@NotNull S left, @NotNull S right, @Nullable S quotient, @Nullable S remainder) {

    }
}
