package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/8/24")
public class SourceFileNameFormatSegment implements StackTraceFormatSegment {

    @NotNull
    final String prefix, suffix, nullString;

    public SourceFileNameFormatSegment(@NotNull String prefix, @NotNull String suffix, @NotNull String nullString) {
        super();
        this.prefix = prefix;
        this.suffix = suffix;
        this.nullString = nullString;
    }

    @Override
    public void append(@NotNull StackTraceElement frame, @NotNull StringBuilder out) {
        final String fileName = frame.getFileName();
        if (fileName != null) {
            out.append(prefix).append(fileName).append(suffix);
        } else {
            out.append(nullString);
        }
    }
}
