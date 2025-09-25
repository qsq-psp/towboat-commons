package org.slf4j;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/20.
 */
public class LoggerNameSegment extends AlignedFormatSegment {

    private static final long serialVersionUID = 0x4E2F311599E29F18L;

    public LoggerNameSegment(int minLength, int maxLength, boolean padLeft, boolean truncateLeft) {
        super(minLength, maxLength, padLeft, truncateLeft);
    }

    @Override
    public void append(@NotNull LogEvent event, @NotNull StringBuilder out) {
        fit(event.loggerName, out); // todo
    }
}
