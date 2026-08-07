package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/12", name = "Slot")
@CodeHistory(date = "2026/6/25")
public class TypeSlot<T> {

    T value;

    public T get() {
        return value;
    }

    public void set(T newValue) {
        value = newValue;
    }

    public T update(T newValue) {
        final T oldValue = get();
        set(newValue);
        return oldValue;
    }

    @NotNull
    public TypeSlot<T> box(T newValue) {
        set(newValue);
        return this;
    }
}
