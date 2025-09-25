package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.util.function.Supplier;

@CodeHistory(date = "2024/6/11", project = "UltraIO")
@CodeHistory(date = "2025/3/4")
@FunctionalInterface
public interface IOSupplier<T> {

    /**
     * @throws IOException the general class of exceptions produced by failed or interrupted I/O operations.
     */
    T get() throws IOException;

    @NotNull
    default Supplier<T> ignore(T value) {
        return () -> {
            try {
                return get();
            } catch (IOException ignore) {
                return value;
            }
        };
    }

    @NotNull
    default Supplier<T> upgrade() {
        return () -> {
            try {
                return get();
            } catch (IOException e) {
                throw new IOError(e);
            }
        };
    }
}
