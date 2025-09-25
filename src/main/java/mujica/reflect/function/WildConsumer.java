package mujica.reflect.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created on 2025/3/5.
 */
@FunctionalInterface
public interface WildConsumer<T> {

    void accept(T t) throws Throwable;

    @NotNull
    default Consumer<T> ignoreChecked() {
        return (t) -> {
            try {
                accept(t);
            } catch (Error | RuntimeException error) {
                throw error;
            } catch (Throwable ignore) {}
        };
    }

    @NotNull
    default Consumer<T> ignoreException() {
        return (t) -> {
            try {
                accept(t);
            } catch (Error error) {
                throw error;
            } catch (Throwable ignore) {}
        };
    }

    @NotNull
    default Consumer<T> ignoreAll() {
        return (t) -> {
            try {
                accept(t);
            } catch (Throwable ignore) {}
        };
    }

    @NotNull
    default WildConsumer<T> andThen(@NotNull Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {accept(t); after.accept(t); };
    }
}
