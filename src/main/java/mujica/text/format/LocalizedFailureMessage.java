package mujica.text.format;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Created on 2025/9/11.
 */
interface LocalizedFailureMessage extends Supplier<String>, Serializable {

    @NotNull
    LocalizedFailureMessage replace(@NotNull String mark, @NotNull String content);

    @NotNull
    LocalizedFailureMessage replaceQuoted(@NotNull String mark, @NotNull String content);

    @NotNull
    LocalizedFailureMessage replace(@NotNull String mark, int value);

    @NotNull
    LocalizedFailureMessage replace(@NotNull String mark, char value);

    @NotNull
    LocalizedFailureMessage replaceQuoted(@NotNull String mark, char value);

    @Override
    @Nullable
    String get();
}
