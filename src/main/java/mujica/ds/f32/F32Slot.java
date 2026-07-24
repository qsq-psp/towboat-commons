package mujica.ds.f32;

import mujica.reflect.function.FloatSupplier;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2024/1/20", project = "Ultramarine", name = "FloatSlot")
@CodeHistory(date = "2026/4/14", name = "FloatSlot")
@CodeHistory(date = "2026/6/10")
public interface F32Slot {

    int MAX_SAFE_S32 = (1 << 23) - 1;

    int MIN_SAFE_S32 = -MAX_SAFE_S32;

    float EPSILON = Math.ulp(1.0f);

    float getF32();

    void setF32(float newValue);

    default void setF32(@NotNull F32Slot newValueSlot) {
        setF32(newValueSlot.getF32());
    }

    default void setF32(@NotNull Number newNumberValue) {
        setF32(newNumberValue.floatValue());
    }

    default void setF32(@NotNull FloatSupplier newValueSupplier) {
        setF32(newValueSupplier.getAsFloat());
    }

    default float updateF32(float newValue) {
        final float oldValue = getF32();
        setF32(newValue);
        return oldValue;
    }

    default float updateF32(@NotNull F32Slot newValueSlot) {
        return updateF32(newValueSlot.getF32());
    }

    default float updateF32(@NotNull Number newNumberValue) {
        return updateF32(newNumberValue.floatValue());
    }

    default float updateF32(@NotNull FloatSupplier newValueSupplier) {
        return updateF32(newValueSupplier.getAsFloat());
    }

    static void exchangeF32(@NotNull F32Slot a, @NotNull F32Slot b) {
        a.setF32(b.updateF32(a.getF32()));
    }

    @NotNull
    default F32Slot boxF32(float newValue) {
        setF32(newValue);
        return this;
    }

    @NotNull
    default F32Slot boxF32(@NotNull F32Slot newValueSlot) {
        return boxF32(newValueSlot.getF32());
    }

    @NotNull
    default F32Slot boxF32(@NotNull Number newNumberValue) {
        return boxF32(newNumberValue.floatValue());
    }

    @NotNull
    default F32Slot boxF32(@NotNull FloatSupplier newValueSupplier) {
        return boxF32(newValueSupplier.getAsFloat());
    }

    @NotNull
    default F32Slot swapF32(@NotNull F32Slot that) {
        return boxF32(that.updateF32(getF32()));
    }
}
