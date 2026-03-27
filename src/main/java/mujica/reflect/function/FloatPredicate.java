package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;

import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/5")
@FunctionalInterface
public interface FloatPredicate {

    boolean test(float value);

    @NotNull
    default FloatPredicate and(@NotNull FloatPredicate other) {
        return (value) -> test(value) && other.test(value);
    }

    @NotNull
    default FloatPredicate or(@NotNull FloatPredicate other) {
        return (value) -> test(value) || other.test(value);
    }

    @NotNull
    default FloatPredicate negate() {
        return (value) -> !test(value);
    }
}
