package mujica.ds.i32;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;

@CodeHistory(date = "2018/7/3", project = "existence", name = "HasIntValue")
@CodeHistory(date = "2024/1/20", project = "Ultramarine", name = "IntSlot")
@CodeHistory(date = "2025/3/9", name = "IntSlot")
@CodeHistory(date = "2025/6/15")
public interface I32Slot {

    int getI32();

    void setI32(int newValue);

    default void setI32(@NotNull I32Slot newValueSlot) {
        setI32(newValueSlot.getI32());
    }

    default void setI32(@NotNull Number newNumberValue) {
        setI32(newNumberValue.intValue());
    }

    default void setI32(@NotNull IntSupplier newValueSupplier) {
        setI32(newValueSupplier.getAsInt());
    }

    default int updateI32(int newValue) {
        final int oldValue = getI32();
        setI32(newValue);
        return oldValue;
    }

    default int updateI32(@NotNull I32Slot newValueSlot) {
        return updateI32(newValueSlot.getI32());
    }

    default int updateI32(@NotNull Number newNumberValue) {
        return updateI32(newNumberValue.intValue());
    }

    default int updateI32(@NotNull IntSupplier newValueSupplier) {
        return updateI32(newValueSupplier.getAsInt());
    }

    static void exchangeI32(@NotNull I32Slot a, @NotNull I32Slot b) {
        a.setI32(b.updateI32(a.getI32()));
    }

    @NotNull
    default I32Slot boxI32(int newValue) {
        setI32(newValue);
        return this;
    }

    @NotNull
    default I32Slot boxI32(@NotNull I32Slot newValueSlot) {
        return boxI32(newValueSlot.getI32());
    }

    @NotNull
    default I32Slot boxI32(@NotNull Number newNumberValue) {
        return boxI32(newNumberValue.intValue());
    }

    @NotNull
    default I32Slot boxI32(@NotNull IntSupplier newValueSupplier) {
        return boxI32(newValueSupplier.getAsInt());
    }

    @NotNull
    default I32Slot swapI32(@NotNull I32Slot that) {
        return boxI32(that.updateI32(getI32()));
    }
}
