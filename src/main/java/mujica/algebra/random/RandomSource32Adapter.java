package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;

@CodeHistory(date = "2025/12/30")
public class RandomSource32Adapter implements RandomSource {

    @NotNull
    final IntSupplier source32;

    public RandomSource32Adapter(@NotNull IntSupplier source32) {
        super();
        this.source32 = source32;
    }

    @Override
    public long applyAsLong(int bitCount) {
        if (bitCount <= Integer.SIZE) {
            if (bitCount <= 0) {
                throw new IllegalArgumentException();
            } else {
                return ((1L << bitCount) - 1L) & source32.getAsInt();
            }
        } else {
            if (bitCount <= Long.SIZE) {
                bitCount -= Integer.SIZE;
                long value = ((1L << bitCount) - 1L) & source32.getAsInt();
                value |= ((long) source32.getAsInt()) << bitCount;
                return value;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
