package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.io.UncheckedIOException;
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
    default Supplier<T> uncheck() {
        return () -> {
            try {
                return get();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    static <T> IOSupplier<T> check(@NotNull Supplier<T> action) {
        return () -> {
            try {
                return action.get();
            } catch (UncheckedIOException e) {
                throw e.getCause();
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

    static <T> IOSupplier<T> downgrade(@NotNull Supplier<T> action) {
        return () -> {
            try {
                return action.get();
            } catch (IOError e) {
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw (IOException) cause;
                }
                throw e;
            }
        };
    }

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
}
