package org.slf4j;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created on 2025/8/23.
 */
public interface StackTraceFormatSegment extends Serializable {

    void append(@NotNull StackTraceElement frame, @NotNull StringBuilder out);
}
