package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/18.
 */
@CodeHistory(date = "2025/12/18")
public interface SplitSource<T extends SplitSource<T>> extends RandomSource {

    @NotNull
    T duplicate();

    @NotNull
    T split();

    @Override
    long next(int bitCount);
}
