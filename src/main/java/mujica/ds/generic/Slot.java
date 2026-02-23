package mujica.ds.generic;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/12")
public interface Slot<T> {

    T get();

    void set(T newValue);

    default T update(T newValue) {
        final T oldValue = get();
        set(newValue);
        return oldValue;
    }

    @NotNull
    default Slot<T> box(T newValue) {
        set(newValue);
        return this;
    }
}
