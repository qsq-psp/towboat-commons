package org.slf4j;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/21.
 */
public class ThreadNameSegment extends AlignedFormatSegment {

    private static final long serialVersionUID = 0x008A20235FAF4EE7L;

    public ThreadNameSegment(int minLength, int maxLength, boolean padLeft, boolean truncateLeft) {
        super(minLength, maxLength, padLeft, truncateLeft);
    }

    @Override
    public void append(@NotNull LogEvent event, @NotNull StringBuilder out) {
        fit(event.threadName, out);
    }
}
