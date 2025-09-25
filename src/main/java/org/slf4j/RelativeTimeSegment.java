package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@CodeHistory(date = "2025/8/25")
public class RelativeTimeSegment implements LogFormatSegment {

    private static final long serialVersionUID = 0x47DDA27F495B66EDL;

    @NotNull
    final TimeUnit maxUnit;

    public RelativeTimeSegment(@NotNull TimeUnit maxUnit) {
        super();
        this.maxUnit = maxUnit;
    }

    @Override
    public void append(@NotNull LogEvent event, @NotNull StringBuilder out) {
        //
    }
}
