package mujica.ds.of_double;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

@CodeHistory(date = "2018/7/3", project = "existence", name = "HasDoubleValue")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/20")
public interface DoubleSlot {

    @ReferencePage(title = "MAX_SAFE_INTEGER", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER")
    long MAX_SAFE_LONG = (1L << 53) - 1;

    @ReferencePage(title = "MIN_SAFE_INTEGER", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MIN_SAFE_INTEGER")
    long MIN_SAFE_LONG = -MAX_SAFE_LONG;

    @ReferencePage(title = "Number.EPSILON", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/EPSILON")
    double EPSILON = Math.ulp(1.0);

    double getDouble();

    void setDouble(double newValue);

    default void setDouble(@NotNull DoubleSlot newValueSlot) {
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

    default double updateDouble(@NotNull DoubleSlot newValueSlot) {
        return updateDouble(newValueSlot.getDouble());
    }

    default double updateDouble(@NotNull Number newNumberValue) {
        return updateDouble(newNumberValue.doubleValue());
    }

    default double updateDouble(@NotNull DoubleSupplier newValueSupplier) {
        return updateDouble(newValueSupplier.getAsDouble());
    }

    static void exchangeDouble(@NotNull DoubleSlot a, @NotNull DoubleSlot b) {
        a.setDouble(b.updateDouble(a.getDouble()));
    }

    @NotNull
    default DoubleSlot boxDouble(double newValue) {
        setDouble(newValue);
        return this;
    }

    @NotNull
    default DoubleSlot boxDouble(@NotNull DoubleSlot newValueSlot) {
        return boxDouble(newValueSlot.getDouble());
    }

    @NotNull
    default DoubleSlot boxDouble(@NotNull Number newNumberValue) {
        return boxDouble(newNumberValue.doubleValue());
    }

    @NotNull
    default DoubleSlot boxDouble(@NotNull DoubleSupplier newValueSupplier) {
        return boxDouble(newValueSupplier.getAsDouble());
    }

    @NotNull
    default DoubleSlot swapDouble(@NotNull DoubleSlot that) {
        return boxDouble(that.updateDouble(getDouble()));
    }
}
