package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.util.function.Predicate;

@CodeHistory(date = "2024/6/7", project = "UltraIO")
@CodeHistory(date = "2025/3/4")
@FunctionalInterface
public interface IOPredicate<T> {

    /**
     * @throws IOException the general class of exceptions produced by failed or interrupted I/O operations.
     */
    boolean test(T t) throws IOException;

    @NotNull
    default Predicate<T> ignore(boolean value) {
        return (t) -> {
            try {
                return test(t);
            } catch (IOException ignore) {
                return value;
            }
        };
    }

    @NotNull
    default Predicate<T> upgrade() {
        return (t) -> {
            try {
                return test(t);
            } catch (IOException e) {
                throw new IOError(e);
            }
        };
    }

    @NotNull
    default IOPredicate<T> and(@NotNull IOPredicate<? super T> other) {
        return (t) -> test(t) && other.test(t);
    }

    @NotNull
    default IOPredicate<T> or(@NotNull IOPredicate<? super T> other) {
        return (t) -> test(t) || other.test(t);
    }

    @NotNull
    default IOPredicate<T> negate() {
        return (t) -> !test(t);
    }
}
