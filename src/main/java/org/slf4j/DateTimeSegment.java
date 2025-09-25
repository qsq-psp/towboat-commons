package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Created on 2025/8/21.
 */
@CodeHistory(date = "2025/8/21")
public class DateTimeSegment implements LogFormatSegment {

    private static final long serialVersionUID = 0x8A553EB3D24F8A47L;

    @NotNull
    final DateTimeFormatter dtf;

    public DateTimeSegment(@NotNull DateTimeFormatter dtf) {
        super();
        this.dtf = dtf;
    }

    public DateTimeSegment(@NotNull String pattern) {
        this(DateTimeFormatter.ofPattern(pattern));
    }

    public DateTimeSegment(@NotNull FormatStyle style) {
        this(DateTimeFormatter.ofLocalizedDateTime(style));
    }

    @Override
    public void append(@NotNull LogEvent event, @NotNull StringBuilder out) {
        dtf.formatTo(Instant.ofEpochMilli(event.timeStamp), out);
    }
}
