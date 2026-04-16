package mujica.ds.of_int;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;

@CodeHistory(date = "2018/7/3", project = "existence", name = "HasIntValue")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/9")
public interface IntSlot {

    int getInt();

    void setInt(int newValue);

    default void setInt(@NotNull IntSlot newValueSlot) {
        setInt(newValueSlot.getInt());
    }

    default void setInt(@NotNull Number newNumberValue) {
        setInt(newNumberValue.intValue());
    }

    default void setInt(@NotNull IntSupplier newValueSupplier) {
        setInt(newValueSupplier.getAsInt());
    }

    default int updateInt(int newValue) {
        final int oldValue = getInt();
        setInt(newValue);
        return oldValue;
    }

    default int updateInt(@NotNull IntSlot newValue) {
        return updateInt(newValue.getInt());
    }

    default int updateInt(@NotNull Number newValue) {
        return updateInt(newValue.intValue());
    }

    default int updateInt(@NotNull IntSupplier newValue) {
        return updateInt(newValue.getAsInt());
    }

    static void exchangeInt(@NotNull IntSlot a, @NotNull IntSlot b) {
        a.setInt(b.updateInt(a.getInt()));
    }

    @NotNull
    default IntSlot boxInt(int newValue) {
        setInt(newValue);
        return this;
    }

    @NotNull
    default IntSlot boxInt(@NotNull IntSlot newValue) {
        return boxInt(newValue.getInt());
    }

    @NotNull
    default IntSlot boxInt(@NotNull Number newValue) {
        return boxInt(newValue.intValue());
    }

    @NotNull
    default IntSlot boxInt(@NotNull IntSupplier newValue) {
        return boxInt(newValue.getAsInt());
    }

    @NotNull
    default IntSlot swapInt(@NotNull IntSlot that) {
        return boxInt(that.updateInt(getInt()));
    }
}
