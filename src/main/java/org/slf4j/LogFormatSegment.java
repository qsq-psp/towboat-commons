package org.slf4j;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created on 2025/8/19.
 * ch.qos.logback.core.pattern.Converter
 */
public interface LogFormatSegment extends Serializable {

    void append(@NotNull LogEvent event, @NotNull StringBuilder out);
}
