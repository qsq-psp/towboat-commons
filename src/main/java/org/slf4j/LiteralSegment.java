package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/19.
 */
@CodeHistory(date = "2025/8/19")
public class LiteralSegment implements LogFormatSegment {

    private static final long serialVersionUID = 0xE0AD1C39326AE1A0L;

    @NotNull
    final String string;

    public LiteralSegment(@NotNull String string) {
        super();
        this.string = string;
    }

    @Override
    public void append(@NotNull LogEvent event, @NotNull StringBuilder out) {
        out.append(string);
    }
}
