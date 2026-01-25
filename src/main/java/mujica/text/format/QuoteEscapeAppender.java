package mujica.text.format;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/1/25.
 */
public class QuoteEscapeAppender extends QuoteAppender {

    @NotNull
    final EscapeAppender escape;

    public QuoteEscapeAppender(@NotNull EscapeAppender escape, @NotNull String prefix, @NotNull String suffix) {
        super(prefix, suffix);
        this.escape = escape;
    }
}
