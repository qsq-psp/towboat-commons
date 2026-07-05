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

    float getFloat();

    void setFloat(float newValue);

    default void setFloat(@NotNull F32Slot newValueSlot) {
        setFloat(newValueSlot.getFloat());
    }

    default void setFloat(@NotNull Number newNumberValue) {
        setFloat(newNumberValue.floatValue());
    }

    default void setFloat(@NotNull FloatSupplier newValueSupplier) {
        setFloat(newValueSupplier.getAsFloat());
    }

    default float updateFloat(float newValue) {
        final float oldValue = getFloat();
        setFloat(newValue);
        return oldValue;
    }

    default float updateFloat(@NotNull F32Slot newValueSlot) {
        return updateFloat(newValueSlot.getFloat());
    }

    default float updateFloat(@NotNull Number newNumberValue) {
        return updateFloat(newNumberValue.floatValue());
    }

    default float updateFloat(@NotNull FloatSupplier newValueSupplier) {
        return updateFloat(newValueSupplier.getAsFloat());
    }

    static void exchangeFloat(@NotNull F32Slot a, @NotNull F32Slot b) {
        a.setFloat(b.updateFloat(a.getFloat()));
    }

    @NotNull
    default F32Slot boxFloat(float newValue) {
        setFloat(newValue);
        return this;
    }

    @NotNull
    default F32Slot boxFloat(@NotNull F32Slot newValueSlot) {
        return boxFloat(newValueSlot.getFloat());
    }

    @NotNull
    default F32Slot boxFloat(@NotNull Number newNumberValue) {
        return boxFloat(newNumberValue.floatValue());
    }

    @NotNull
    default F32Slot boxFloat(@NotNull FloatSupplier newValueSupplier) {
        return boxFloat(newValueSupplier.getAsFloat());
    }

    @NotNull
    default F32Slot swapFloat(@NotNull F32Slot that) {
        return boxFloat(that.updateFloat(getFloat()));
    }
}
