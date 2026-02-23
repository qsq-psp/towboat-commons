package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/27")
public interface CharSequenceOperator {

    @NotNull
    String apply(@NotNull CharSequence string);

    @NotNull
    String apply(@NotNull CharSequence string,
                 @Index(of = "string") int startIndex,
                 @Index(of = "string", inclusive = false) int endIndex);
}
