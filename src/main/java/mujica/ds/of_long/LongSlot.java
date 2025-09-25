package mujica.ds.of_long;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongSupplier;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/9")
public interface LongSlot {

    long getLong();

    long setLong(long newValue);

    default long setLong(@NotNull Number newValue) {
        return setLong(newValue.longValue());
    }

    default long setLong(@NotNull LongSlot newValue) {
        return setLong(newValue.getLong());
    }

    default long setLong(@NotNull LongSupplier supplier) {
        return setLong(supplier.getAsLong());
    }

    static void exchange(@NotNull LongSlot a, @NotNull LongSlot b) {
        a.setLong(b.setLong(a.getLong()));
    }
}
