package mujica.ds.i8;

import mujica.reflect.function.ByteSupplier;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/1/1", name = "ByteSlot")
@CodeHistory(date = "2026/6/11")
public interface I8Slot {

    byte getByte();

    void setByte(byte newValue);

    default void setByte(@NotNull I8Slot newValue) {
        setByte(newValue.getByte());
    }

    default void setByte(@NotNull Number newValue) {
        setByte(newValue.byteValue());
    }

    default void setByte(@NotNull ByteSupplier newValue) {
        setByte(newValue.getAsByte());
    }

    default byte updateByte(byte newValue) {
        final byte oldValue = getByte();
        setByte(newValue);
        return oldValue;
    }

    default byte updateByte(@NotNull I8Slot newValue) {
        return updateByte(newValue.getByte());
    }

    default byte updateByte(@NotNull Number newValue) {
        return updateByte(newValue.byteValue());
    }

    default byte updateByte(@NotNull ByteSupplier newValue) {
        return updateByte(newValue.getAsByte());
    }

    static void exchangeByte(@NotNull I8Slot a, @NotNull I8Slot b) {
        a.setByte(b.updateByte(a.getByte()));
    }

    @NotNull
    default I8Slot boxByte(long newValue) {
        setByte(newValue);
        return this;
    }

    @NotNull
    default I8Slot boxByte(@NotNull I8Slot newValue) {
        return boxByte(newValue.getByte());
    }

    @NotNull
    default I8Slot boxByte(@NotNull Number newValue) {
        return boxByte(newValue.longValue());
    }

    @NotNull
    default I8Slot boxByte(@NotNull ByteSupplier newValue) {
        return boxByte(newValue.getAsByte());
    }

    @NotNull
    default I8Slot swapByte(@NotNull I8Slot that) {
        return boxByte(that.updateByte(getByte()));
    }
}
