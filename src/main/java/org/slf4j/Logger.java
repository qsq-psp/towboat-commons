package org.slf4j;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2021/3/2", project = "webbiton")
@CodeHistory(date = "2025/7/29")
public interface Logger {

    String getName();

    boolean isTraceEnabled();

    void trace(String message);

    void trace(String format, Object argument0);

    void trace(String format, Object argument0, Object argument1);

    void trace(String format, Object... arguments);

    void trace(String format, Throwable throwable);

    boolean isDebugEnabled();

    void debug(String message);

    void debug(String format, Object argument0);

    void debug(String format, Object argument0, Object argument1);

    void debug(String format, Object... arguments);

    void debug(String format, Throwable throwable);

    boolean isInfoEnabled();

    void info(String message);

    void info(String format, Object argument0);

    void info(String format, Object argument0, Object argument1);

    void info(String format, Object... arguments);

    void info(String format, Throwable throwable);

    boolean isWarnEnabled();

    void warn(String message);

    void warn(String format, Object argument0);

    void warn(String format, Object argument0, Object argument1);

    void warn(String format, Object... arguments);

    void warn(String format, Throwable throwable);

    boolean isErrorEnabled();

    void error(String message);

    void error(String format, Object argument0);

    void error(String format, Object argument0, Object argument1);

    void error(String format, Object... arguments);

    void error(String format, Throwable throwable);
}
