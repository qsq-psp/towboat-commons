package mujica.ds.slot;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/30.
 */
public interface RealIterator {

    boolean next();

    @NotNull
    Rational lowerBound();

    @NotNull
    Rational higherBound();
}
