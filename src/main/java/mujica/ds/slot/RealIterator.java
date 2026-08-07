package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/30")
public interface RealIterator {

    boolean next();

    @NotNull
    Rational lowerBound();

    @NotNull
    Rational higherBound();
}
