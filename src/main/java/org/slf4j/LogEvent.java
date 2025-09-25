package org.slf4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Created on 2025/8/19.
 */
public class LogEvent implements Serializable {

    final long timeStamp;

    @NotNull
    Level level;

    @NotNull
    final String loggerName; // to serialize, not Logger, only its name

    @NotNull
    final String threadName;

    @Nullable
    final String format;

    @Nullable
    final Throwable throwable;

    public LogEvent(long timeStamp, @NotNull Level level, @NotNull String loggerName, @NotNull String threadName, @Nullable String format, @Nullable Throwable throwable) {
        super();
        this.timeStamp = timeStamp;
        this.level = level;
        this.loggerName = loggerName;
        this.threadName = threadName;
        this.format = format;
        this.throwable = throwable;
    }
}
