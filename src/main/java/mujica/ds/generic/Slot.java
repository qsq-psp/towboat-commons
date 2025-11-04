package mujica.ds.generic;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/12")
public interface Slot<T> {

    T get();

    T set(@NotNull T newValue);
}
