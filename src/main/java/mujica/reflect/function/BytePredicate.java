package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2018/9/20", project = "TubeM")
@CodeHistory(date = "2025/11/21")
@Stable(date = "2025/11/21")
@FunctionalInterface
public interface BytePredicate {

    boolean test(byte value);

    @NotNull
    default BytePredicate and(@NotNull BytePredicate other) {
        return (value) -> test(value) && other.test(value);
    }

    @NotNull
    default BytePredicate or(@NotNull BytePredicate other) {
        return (value) -> test(value) || other.test(value);
    }

    @NotNull
    default BytePredicate negate() {
        return (value) -> !test(value);
    }
}
