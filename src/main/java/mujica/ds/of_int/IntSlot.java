package mujica.ds.of_int;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;

/**
 * Created in existence on 2018/7/3, named HasIntValue.
 * Recreated in Ultramarine on 2024/1/20.
 * Moved here on 2025/3/9.
 */
@CodeHistory(date = "2018/7/3", project = "existence", name = "HasIntValue")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/9")
public interface IntSlot {

    int getInt();

    int setInt(int newValue);

    default int setInt(@NotNull Number newValue) {
        return setInt(newValue.intValue());
    }

    default int setInt(@NotNull IntSlot newValue) {
        return setInt(newValue.getInt());
    }

    default int setInt(@NotNull IntSupplier supplier) {
        return setInt(supplier.getAsInt());
    }

    static void exchange(@NotNull IntSlot a, @NotNull IntSlot b) {
        a.setInt(b.setInt(a.getInt()));
    }
}
