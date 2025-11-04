package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@CodeHistory(date = "2025/9/12")
public interface CharSequencePredicate extends Predicate<CharSequence> {

    @Override
    boolean test(@Nullable CharSequence string);

    boolean test(@NotNull CharSequence string, @Index(of = "string") int start, @Index(of = "string", inclusive = false) int end);
}
