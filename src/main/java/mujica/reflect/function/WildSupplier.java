package mujica.reflect.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Created on 2025/3/8.
 */
@FunctionalInterface
public interface WildSupplier<T> {

    T get() throws Throwable;

    @NotNull
    default Supplier<T> ignoreChecked(T value) {
        return () -> {
            try {
                return get();
            } catch (Error | RuntimeException error) {
                throw error;
            } catch (Throwable ignore) {
                return value;
            }
        };
    }

    @NotNull
    default Supplier<T> ignoreException(T value) {
        return () -> {
            try {
                return get();
            } catch (Error error) {
                throw error;
            } catch (Throwable ignore) {
                return value;
            }
        };
    }

    @NotNull
    default Supplier<T> ignoreAll(T value) {
        return () -> {
            try {
                return get();
            } catch (Throwable ignore) {
                return value;
            }
        };
    }
}
