package org.slf4j;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/30.
 */
public interface ThrowableFormatSegment {

    void append(@NotNull String tag, @NotNull Throwable throwable, @NotNull StringBuilder out);
}
