package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/30")
public class PatternConcatAppender extends CharSequenceAppender {

    @NotNull
    protected final PatternEscapeAppender[] sequence;

    public PatternConcatAppender(@NotNull PatternEscapeAppender[] sequence) {
        super();
        this.sequence = sequence;
    }
}
