package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/7")
public interface Base2Float {

    @NotNull
    Base2Integer exponent();

    @NotNull
    Base2Integer mantissa();
}
