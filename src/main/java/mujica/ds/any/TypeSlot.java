package mujica.ds.any;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/12", name = "Slot")
@CodeHistory(date = "2026/6/25")
public interface TypeSlot<T> {

    T get();

    void set(T newValue);

    default T update(T newValue) {
        final T oldValue = get();
        set(newValue);
        return oldValue;
    }

    @NotNull
    default TypeSlot<T> box(T newValue) {
        set(newValue);
        return this;
    }
}
