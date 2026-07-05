package mujica.ds.i8;

import mujica.reflect.function.ByteSupplier;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/1/1", name = "ByteSlot")
@CodeHistory(date = "2026/6/11")
public interface I8Slot {

    byte getI8();

    void setI8(byte newValue);

    default void setI8(@NotNull I8Slot newValue) {
        setI8(newValue.getI8());
    }

    default void setI8(@NotNull Number newValue) {
        setI8(newValue.byteValue());
    }

    default void setI8(@NotNull ByteSupplier newValue) {
        setI8(newValue.getAsByte());
    }

    default byte updateI8(byte newValue) {
        final byte oldValue = getI8();
        setI8(newValue);
        return oldValue;
    }

    default byte updateI8(@NotNull I8Slot newValue) {
        return updateI8(newValue.getI8());
    }

    default byte updateI8(@NotNull Number newValue) {
        return updateI8(newValue.byteValue());
    }

    default byte updateI8(@NotNull ByteSupplier newValue) {
        return updateI8(newValue.getAsByte());
    }

    static void exchangeI8(@NotNull I8Slot a, @NotNull I8Slot b) {
        a.setI8(b.updateI8(a.getI8()));
    }

    @NotNull
    default I8Slot boxI8(long newValue) {
        setI8(newValue);
        return this;
    }

    @NotNull
    default I8Slot boxI8(@NotNull I8Slot newValue) {
        return boxI8(newValue.getI8());
    }

    @NotNull
    default I8Slot boxI8(@NotNull Number newValue) {
        return boxI8(newValue.longValue());
    }

    @NotNull
    default I8Slot boxI8(@NotNull ByteSupplier newValue) {
        return boxI8(newValue.getAsByte());
    }

    @NotNull
    default I8Slot swapI8(@NotNull I8Slot that) {
        return boxI8(that.updateI8(getI8()));
    }
}
