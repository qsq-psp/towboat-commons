package mujica.ds.of_float;

import mujica.reflect.function.FloatSupplier;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/14")
public interface FloatSlot {

    float EPSILON = Math.ulp(1.0f);

    float getFloat();

    void setFloat(float newValue);

    default void setFloat(@NotNull FloatSlot newValueSlot) {
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

    default float updateFloat(@NotNull FloatSlot newValueSlot) {
        return updateFloat(newValueSlot.getFloat());
    }

    default float updateFloat(@NotNull Number newNumberValue) {
        return updateFloat(newNumberValue.floatValue());
    }

    default float updateFloat(@NotNull FloatSupplier newValueSupplier) {
        return updateFloat(newValueSupplier.getAsFloat());
    }

    static void exchangeFloat(@NotNull FloatSlot a, @NotNull FloatSlot b) {
        a.setFloat(b.updateFloat(a.getFloat()));
    }

    @NotNull
    default FloatSlot boxFloat(float newValue) {
        setFloat(newValue);
        return this;
    }

    @NotNull
    default FloatSlot boxFloat(@NotNull FloatSlot newValueSlot) {
        return boxFloat(newValueSlot.getFloat());
    }

    @NotNull
    default FloatSlot boxFloat(@NotNull Number newNumberValue) {
        return boxFloat(newNumberValue.floatValue());
    }

    @NotNull
    default FloatSlot boxFloat(@NotNull FloatSupplier newValueSupplier) {
        return boxFloat(newValueSupplier.getAsFloat());
    }

    @NotNull
    default FloatSlot swapFloat(@NotNull FloatSlot that) {
        return boxFloat(that.updateFloat(getFloat()));
    }
}
