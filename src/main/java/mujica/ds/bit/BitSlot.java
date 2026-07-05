package mujica.ds.bit;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/4/22", name = "BooleanSlot")
@CodeHistory(date = "2026/6/23")
public interface BitSlot {

    boolean getBit();

    void setBit(boolean newValue);

    default void setBit(@NotNull BitSlot newValueSlot) {
        setBit(newValueSlot.getBit());
    }

    default void setBit(@NotNull BooleanSupplier newValueSupplier) {
        setBit(newValueSupplier.getAsBoolean());
    }

    default boolean updateBit(boolean newValue) {
        final boolean oldValue = getBit();
        setBit(newValue);
        return oldValue;
    }

    default boolean updateBit(@NotNull BitSlot newValueSlot) {
        return updateBit(newValueSlot.getBit());
    }

    default boolean updateBit(@NotNull BooleanSupplier newValueSupplier) {
        return updateBit(newValueSupplier.getAsBoolean());
    }

    static void exchangeBit(@NotNull BitSlot a, @NotNull BitSlot b) {
        a.setBit(b.updateBit(a.getBit()));
    }

    default void flipBit() {
        setBit(!getBit());
    }

    @NotNull
    default BitSlot boxBit(boolean newValue) {
        setBit(newValue);
        return this;
    }

    @NotNull
    default BitSlot boxBit(@NotNull BooleanSupplier newValueSupplier) {
        setBit(newValueSupplier.getAsBoolean());
        return this;
    }

    @NotNull
    default BitSlot swapBit(@NotNull BitSlot that) {
        return boxBit(that.updateBit(getBit()));
    }
}
