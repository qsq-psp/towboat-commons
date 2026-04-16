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
}
