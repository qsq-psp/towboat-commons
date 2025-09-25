package org.slf4j;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/19.
 * ch.qos.logback.core.pattern.FormatInfo
 */
public abstract class AlignedFormatSegment implements LogFormatSegment {

    private static final long serialVersionUID = 0x152336C0DA1DE124L;

    private static final int SPACE_COUNT = 32;

    private static final String SPACES = " ".repeat(SPACE_COUNT);

    private static void appendSpaces(int count, @NotNull StringBuilder out) {
        while (count >= SPACE_COUNT) {
            out.append(SPACES);
            count -= SPACE_COUNT;
        }
        if (count > 0) {
            out.append(SPACES, 0, count);
        }
    }

    final int minLength;

    final int maxLength;

    final boolean padLeft;

    final boolean truncateLeft;

    protected AlignedFormatSegment(int minLength, int maxLength, boolean padLeft, boolean truncateLeft) {
        super();
        if (!(0 <= minLength && minLength <= maxLength)) {
            throw new IllegalArgumentException();
        }
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.padLeft = padLeft;
        this.truncateLeft = truncateLeft;
    }

    protected void fit(@NotNull String string, @NotNull StringBuilder out) {
        final int length = string.length();
        if (length < minLength) {
            if (padLeft) {
                appendSpaces(minLength - length, out);
            }
            out.append(string);
            if (!padLeft) {
                appendSpaces(minLength - length, out);
            }
        } else if (length > maxLength) {
            if (truncateLeft) {
                out.append(string, length - maxLength, length);
            } else {
                out.append(string, 0, maxLength);
            }
        } else {
            out.append(string);
        }
    }
}
