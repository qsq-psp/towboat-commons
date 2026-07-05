package mujica.ds.i64;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongSupplier;

@CodeHistory(date = "2024/1/20", project = "Ultramarine", name = "LongSlot")
@CodeHistory(date = "2025/3/9", name = "LongSlot")
@CodeHistory(date = "2025/6/15")
public interface I64Slot {

    long getI64();

    void setI64(long newValue);

    default void setI64(@NotNull I64Slot newValue) {
        setI64(newValue.getI64());
    }

    default void setI64(@NotNull Number newValue) {
        setI64(newValue.longValue());
    }

    default void setI64(@NotNull LongSupplier supplier) {
        setI64(supplier.getAsLong());
    }
    
    default long updateI64(long newValue) {
        final long oldValue = getI64();
        setI64(newValue);
        return oldValue;
    }

    default long updateI64(@NotNull I64Slot newValue) {
        return updateI64(newValue.getI64());
    }
    
    default long updateI64(@NotNull Number newValue) {
        return updateI64(newValue.longValue());
    }
    
    default long updateI64(@NotNull LongSupplier newValue) {
        return updateI64(newValue.getAsLong());
    }

    static void exchangeI64(@NotNull I64Slot a, @NotNull I64Slot b) {
        a.setI64(b.updateI64(a.getI64()));
    }
    
    @NotNull
    default I64Slot boxI64(long newValue) {
        setI64(newValue);
        return this;
    }
    
    @NotNull
    default I64Slot boxI64(@NotNull I64Slot newValue) {
        return boxI64(newValue.getI64());
    }
    
    @NotNull
    default I64Slot boxI64(@NotNull Number newValue) {
        return boxI64(newValue.longValue());
    }
    
    @NotNull
    default I64Slot boxI64(@NotNull LongSupplier newValue) {
        return boxI64(newValue.getAsLong());
    }
    
    @NotNull
    default I64Slot swapI64(@NotNull I64Slot that) {
        return boxI64(that.updateI64(getI64()));
    }
}
