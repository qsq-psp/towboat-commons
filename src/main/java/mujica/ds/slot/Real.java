package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/28")
public interface Real {

    @NotNull
    RealIterator realIterator();
}
