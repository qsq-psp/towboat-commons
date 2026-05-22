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

    default int updateInt(@NotNull IntSlot newValueSlot) {
        return updateInt(newValueSlot.getInt());
    }

    default int updateInt(@NotNull Number newNumberValue) {
        return updateInt(newNumberValue.intValue());
    }

    default int updateInt(@NotNull IntSupplier newValueSupplier) {
        return updateInt(newValueSupplier.getAsInt());
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
    default IntSlot boxInt(@NotNull IntSlot newValueSlot) {
        return boxInt(newValueSlot.getInt());
    }

    @NotNull
    default IntSlot boxInt(@NotNull Number newNumberValue) {
        return boxInt(newNumberValue.intValue());
    }

    @NotNull
    default IntSlot boxInt(@NotNull IntSupplier newValueSupplier) {
        return boxInt(newValueSupplier.getAsInt());
    }

    @NotNull
    default IntSlot swapInt(@NotNull IntSlot that) {
        return boxInt(that.updateInt(getInt()));
    }
}
