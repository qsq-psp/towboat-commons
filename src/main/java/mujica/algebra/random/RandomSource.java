package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.IntToLongFunction;
import java.util.function.LongSupplier;

@CodeHistory(date = "2018/11/28", project = "mmc", name = "DiscreteRandom")
@CodeHistory(date = "2020/7/20", project = "va")
@CodeHistory(date = "2022/4/2")
@FunctionalInterface
public interface RandomSource extends IntToLongFunction, Cloneable {

    long applyAsLong(int bitCount);

    @NotNull
    default IntSupplier intBind(int bitCount) {
        if (!(0 < bitCount && bitCount <= Integer.SIZE)) {
            throw new IllegalArgumentException();
        }
        return () -> (int) applyAsLong(bitCount);
    }

    @NotNull
    default LongSupplier longBind(int bitCount) {
        if (!(0 < bitCount && bitCount <= Long.SIZE)) {
            throw new IllegalArgumentException();
        }
        return () -> applyAsLong(bitCount);
    }
}
