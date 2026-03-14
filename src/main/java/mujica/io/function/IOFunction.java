package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Function;

/**
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
@CodeHistory(date = "2024/6/6", project = "UltraIO")
@CodeHistory(date = "2025/3/4")
@FunctionalInterface
public interface IOFunction<T, R> {

    /**
     * @throws IOException the general class of exceptions produced by failed or interrupted I/O operations.
     */
    R apply(T t) throws IOException;

    @NotNull
    default Function<T, R> uncheck() {
        return (t) -> {
            try {
                return apply(t);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    @NotNull
    static <T, R> IOFunction<T, R> check(@NotNull Function<T, R> action) {
        return (t) -> {
            try {
                return action.apply(t);
            } catch (UncheckedIOException e) {
                throw e.getCause();
            }
        };
    }

    @NotNull
    default Function<T, R> upgrade() {
        return (t) -> {
            try {
                return apply(t);
            } catch (IOException e) {
                throw new IOError(e);
            }
        };
    }

    static <T, R> IOFunction<T, R> downgrade(@NotNull Function<T, R> action) {
        return (t) -> {
            try {
                return action.apply(t);
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
    default Function<T, R> ignore(R value) {
        return (t) -> {
            try {
                return apply(t);
            } catch (IOException ignore) {
                return value;
            }
        };
    }

    @NotNull
    default <V> IOFunction<V, R> compose(@NotNull IOFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    @NotNull
    default <V> IOFunction<T, V> andThen(@NotNull IOFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }
}
