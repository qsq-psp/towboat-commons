package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@CodeHistory(date = "2025/3/7")
@Stable(date = "2025/8/15")
@FunctionalInterface
public interface WildPredicate<T> {

    boolean test(T t) throws Throwable;

    @NotNull
    default Predicate<T> ignoreChecked(boolean value) {
        return (t) -> {
            try {
                return test(t);
            } catch (Error | RuntimeException error) {
                throw error;
            } catch (Throwable ignore) {
                return value;
            }
        };
    }

    @NotNull
    default Predicate<T> ignoreException(boolean value) {
        return (t) -> {
            try {
                return test(t);
            } catch (Error error) {
                throw error;
            } catch (Throwable ignore) {
                return value;
            }
        };
    }

    @NotNull
    default Predicate<T> ignoreAll(boolean value) {
        return (t) -> {
            try {
                return test(t);
            } catch (Throwable ignore) {
                return value;
            }
        };
    }

    @NotNull
    default WildPredicate<T> negate() {
        return (t) -> !test(t);
    }
}
