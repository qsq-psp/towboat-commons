package mujica.ds.of_long;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongSupplier;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/9")
public interface LongSlot {

    long getLong();

    void setLong(long newValue);

    default void setLong(@NotNull LongSlot newValue) {
        setLong(newValue.getLong());
    }

    default void setLong(@NotNull Number newValue) {
        setLong(newValue.longValue());
    }

    default void setLong(@NotNull LongSupplier supplier) {
        setLong(supplier.getAsLong());
    }
    
    default long updateLong(long newValue) {
        final long oldValue = getLong();
        setLong(newValue);
        return oldValue;
    }

    default long updateLong(@NotNull LongSlot newValue) {
        return updateLong(newValue.getLong());
    }
    
    default long updateLong(@NotNull Number newValue) {
        return updateLong(newValue.longValue());
    }
    
    default long updateLong(@NotNull LongSupplier newValue) {
        return updateLong(newValue.getAsLong());
    }

    static void exchangeLong(@NotNull LongSlot a, @NotNull LongSlot b) {
        a.setLong(b.updateLong(a.getLong()));
    }
    
    @NotNull
    default LongSlot boxLong(long newValue) {
        setLong(newValue);
        return this;
    }
    
    @NotNull
    default LongSlot boxLong(@NotNull LongSlot newValue) {
        return boxLong(newValue.getLong());
    }
    
    @NotNull
    default LongSlot boxLong(@NotNull Number newValue) {
        return boxLong(newValue.longValue());
    }
    
    @NotNull
    default LongSlot boxLong(@NotNull LongSupplier newValue) {
        return boxLong(newValue.getAsLong());
    }
    
    @NotNull
    default LongSlot swapLong(@NotNull LongSlot that) {
        return boxLong(that.updateLong(getLong()));
    }
}
