package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/30.
 */
@CodeHistory(date = "2026/6/30")
public interface Rational extends Real {

    @NotNull
    @Override
    RealIterator realIterator();

    @NotNull
    Fraction<?> getFraction(); // Allocator
}
