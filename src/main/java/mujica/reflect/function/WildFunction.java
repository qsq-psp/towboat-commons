package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
@CodeHistory(date = "2025/3/6")
@Stable(date = "2025/8/15")
@FunctionalInterface
public interface WildFunction<T, R> {

    R apply(T t) throws Throwable;

    @NotNull
    default Function<T, R> ignoreChecked(R value) {
        return (t) -> {
            try {
                return apply(t);
            } catch (Error | RuntimeException error) {
                throw error;
            } catch (Throwable ignore) {
                return value;
            }
        };
    }

    @NotNull
    default Function<T, R> ignoreException(R value) {
        return (t) -> {
            try {
                return apply(t);
            } catch (Error error) {
                throw error;
            } catch (Throwable ignore) {
                return value;
            }
        };
    }

    @NotNull
    default Function<T, R> ignoreAll(R value) {
        return (t) -> {
            try {
                return apply(t);
            } catch (Throwable ignore) {
                return value;
            }
        };
    }

    @NotNull
    default <V> WildFunction<V, R> compose(@NotNull WildFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    @NotNull
    default <V> WildFunction<T, V> andThen(@NotNull WildFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }
}
