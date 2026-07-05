package mujica.ds.slot;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/30.
 */
public interface Rational extends Real {

    @NotNull
    @Override
    RealIterator realIterator();

    @NotNull
    Fraction<?> getFraction(); // Allocator
}
