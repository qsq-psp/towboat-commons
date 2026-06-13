package mujica.ds.f64;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

@CodeHistory(date = "2018/7/3", project = "existence", name = "HasDoubleValue")
@CodeHistory(date = "2024/1/20", project = "Ultramarine", name = "DoubleSlot")
@CodeHistory(date = "2025/3/20", name = "DoubleSlot")
@CodeHistory(date = "2026/6/10")
public interface F64Slot {

    @ReferencePage(title = "MAX_SAFE_INTEGER", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER")
    long MAX_SAFE_S64 = (1L << 53) - 1;

    @ReferencePage(title = "MIN_SAFE_INTEGER", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MIN_SAFE_INTEGER")
    long MIN_SAFE_S64 = -MAX_SAFE_S64;

    @ReferencePage(title = "Number.EPSILON", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/EPSILON")
    double EPSILON = Math.ulp(1.0);

    double getDouble();

    void setDouble(double newValue);

    default void setDouble(@NotNull F64Slot newValueSlot) {
        setDouble(newValueSlot.getDouble());
    }

    default void setDouble(@NotNull Number newNumberValue) {
        setDouble(newNumberValue.doubleValue());
    }

    default void setDouble(@NotNull DoubleSupplier newValueSupplier) {
        setDouble(newValueSupplier.getAsDouble());
    }

    default double updateDouble(double newValue) {
        final double oldValue = getDouble();
        setDouble(newValue);
        return oldValue;
    }

    default double updateDouble(@NotNull F64Slot newValueSlot) {
        return updateDouble(newValueSlot.getDouble());
    }

    default double updateDouble(@NotNull Number newNumberValue) {
        return updateDouble(newNumberValue.doubleValue());
    }

    default double updateDouble(@NotNull DoubleSupplier newValueSupplier) {
        return updateDouble(newValueSupplier.getAsDouble());
    }

    static void exchangeDouble(@NotNull F64Slot a, @NotNull F64Slot b) {
        a.setDouble(b.updateDouble(a.getDouble()));
    }

    @NotNull
    default F64Slot boxDouble(double newValue) {
        setDouble(newValue);
        return this;
    }

    @NotNull
    default F64Slot boxDouble(@NotNull F64Slot newValueSlot) {
        return boxDouble(newValueSlot.getDouble());
    }

    @NotNull
    default F64Slot boxDouble(@NotNull Number newNumberValue) {
        return boxDouble(newNumberValue.doubleValue());
    }

    @NotNull
    default F64Slot boxDouble(@NotNull DoubleSupplier newValueSupplier) {
        return boxDouble(newValueSupplier.getAsDouble());
    }

    @NotNull
    default F64Slot swapDouble(@NotNull F64Slot that) {
        return boxDouble(that.updateDouble(getDouble()));
    }
}
