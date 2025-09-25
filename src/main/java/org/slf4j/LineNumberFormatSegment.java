package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/23.
 */
@CodeHistory(date = "2025/8/23")
public class LineNumberFormatSegment implements StackTraceFormatSegment {

    private static final long serialVersionUID = 0xE802B41E0B81E632L;

    @NotNull
    final String prefix, suffix, unknownString, nativeString;

    public LineNumberFormatSegment(@NotNull String prefix, @NotNull String suffix, @NotNull String unknownString, @NotNull String nativeString) {
        super();
        this.prefix = prefix;
        this.suffix = suffix;
        this.unknownString = unknownString;
        this.nativeString = nativeString;
    }

    @Override
    public void append(@NotNull StackTraceElement frame, @NotNull StringBuilder out) {
        final int lineNumber = frame.getLineNumber();
        if (lineNumber >= 0) {
            out.append(prefix).append(lineNumber).append(suffix);
        } else if (lineNumber != -2) {
            out.append(unknownString);
        } else {
            out.append(nativeString);
        }
    }
}
