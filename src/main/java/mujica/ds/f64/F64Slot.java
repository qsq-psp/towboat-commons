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
    long MAX_SAFE_F64 = (1L << 53) - 1;

    @ReferencePage(title = "MIN_SAFE_INTEGER", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MIN_SAFE_INTEGER")
    long MIN_SAFE_F64 = -MAX_SAFE_F64;

    @ReferencePage(title = "Number.EPSILON", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/EPSILON")
    double EPSILON = Math.ulp(1.0);

    double getF64();

    void setF64(double newValue);

    default void setF64(@NotNull F64Slot newValueSlot) {
        setF64(newValueSlot.getF64());
    }

    default void setF64(@NotNull Number newNumberValue) {
        setF64(newNumberValue.doubleValue());
    }

    default void setF64(@NotNull DoubleSupplier newValueSupplier) {
        setF64(newValueSupplier.getAsDouble());
    }

    default double updateF64(double newValue) {
        final double oldValue = getF64();
        setF64(newValue);
        return oldValue;
    }

    default double updateF64(@NotNull F64Slot newValueSlot) {
        return updateF64(newValueSlot.getF64());
    }

    default double updateF64(@NotNull Number newNumberValue) {
        return updateF64(newNumberValue.doubleValue());
    }

    default double updateF64(@NotNull DoubleSupplier newValueSupplier) {
        return updateF64(newValueSupplier.getAsDouble());
    }

    static void exchangeDouble(@NotNull F64Slot a, @NotNull F64Slot b) {
        a.setF64(b.updateF64(a.getF64()));
    }

    @NotNull
    default F64Slot boxF64(double newValue) {
        setF64(newValue);
        return this;
    }

    @NotNull
    default F64Slot boxF64(@NotNull F64Slot newValueSlot) {
        return boxF64(newValueSlot.getF64());
    }

    @NotNull
    default F64Slot boxF64(@NotNull Number newNumberValue) {
        return boxF64(newNumberValue.doubleValue());
    }

    @NotNull
    default F64Slot boxF64(@NotNull DoubleSupplier newValueSupplier) {
        return boxF64(newValueSupplier.getAsDouble());
    }

    @NotNull
    default F64Slot swapF64(@NotNull F64Slot that) {
        return boxF64(that.updateF64(getF64()));
    }
}
