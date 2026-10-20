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
    public void zero(@NotNull S out) {
        final Base2Iterator iterator = out.magnitude();
        while (iterator.next()) {
            iterator.setBit(false);
        }
        out.signBit().setBit(false);
    }

    @Override
    public void one(@NotNull S out) {
        final Base2Iterator iterator = out.magnitude();
        if (iterator.next()) {
            iterator.setBit(true);
        }
        while (iterator.next()) {
            iterator.setBit(false);
        }
        out.signBit().setBit(false);
    }

    @Override
    public int compareSlot(@NotNull S left, @NotNull S right) {
        return 0;
    }

    @Override
    public void move(@NotNull S in, @NotNull S out) {

    }

    @Override
    public void negate(@NotNull S in, @NotNull S out) {

    }

    @Override
    public void invert(@NotNull S in, @NotNull S out) {

    }

    @Override
    public void add(@NotNull S left, @NotNull S right, @NotNull S out) {

    }

    @Override
    public void multiply(@NotNull S left, @NotNull S right, @NotNull S out) {

    }

    @Override
    public void divide(@NotNull S left, @NotNull S right, @NotNull S result, @NotNull RoundingMode mode) {

    }

    @Override
    public void divide(@NotNull S left, @NotNull S right, @Nullable S quotient, @Nullable S remainder) {

    }
}
