package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@CodeHistory(date = "2026/1/31")
@FunctionalInterface
public interface UniversalStringifier extends Function<Object, String> {

    @NotNull
    String apply(@NotNull Object object);
}
