package mujica.ds.of_byte;

import mujica.reflect.function.ByteSupplier;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/1/1")
@Stable(date = "2026/2/3")
public interface ByteSlot {

    byte getByte();

    void setByte(byte newValue);

    default void setByte(@NotNull ByteSlot newValue) {
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

    default byte updateByte(@NotNull ByteSlot newValue) {
        return updateByte(newValue.getByte());
    }

    default byte updateByte(@NotNull Number newValue) {
        return updateByte(newValue.byteValue());
    }

    default byte updateByte(@NotNull ByteSupplier newValue) {
        return updateByte(newValue.getAsByte());
    }

    static void exchangeByte(@NotNull ByteSlot a, @NotNull ByteSlot b) {
        a.setByte(b.updateByte(a.getByte()));
    }

    @NotNull
    default ByteSlot boxByte(long newValue) {
        setByte(newValue);
        return this;
    }

    @NotNull
    default ByteSlot boxByte(@NotNull ByteSlot newValue) {
        return boxByte(newValue.getByte());
    }

    @NotNull
    default ByteSlot boxByte(@NotNull Number newValue) {
        return boxByte(newValue.longValue());
    }

    @NotNull
    default ByteSlot boxByte(@NotNull ByteSupplier newValue) {
        return boxByte(newValue.getAsByte());
    }

    @NotNull
    default ByteSlot swapByte(@NotNull ByteSlot that) {
        return boxByte(that.updateByte(getByte()));
    }
}
