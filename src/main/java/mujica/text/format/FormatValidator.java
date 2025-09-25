package mujica.text.format;

import mujica.reflect.function.CharSequencePredicate;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@CodeHistory(date = "2025/4/1")
public interface FormatValidator extends CharSequencePredicate {

    @Override
    boolean test(@Nullable CharSequence string);

    @Override
    boolean test(@NotNull CharSequence string, @Index(of = "string") int start, @Index(of = "string", inclusive = false) int end);

    /**
     * @return the reason why format check failed, or null if success
     */
    @Nullable
    String message(@Nullable Locale locale, @Nullable CharSequence string);

    /**
     * @return the reason why format check failed, or null if success
     */
    @Nullable
    String message(@Nullable Locale locale, @NotNull CharSequence string, @Index(of = "string") int start, @Index(of = "string", inclusive = false) int end);

    @Nullable
    String regularExpression();
}
