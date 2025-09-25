package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/8/7")
public abstract class AbstractLogger implements Logger {

    @NotNull
    public final String name;

    protected AbstractLogger(@NotNull String name) {
        super();
        this.name = name;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    public abstract boolean isEnabled(@NotNull Level level);

    public abstract void log(@NotNull Level level, String message);

    public abstract void log(@NotNull Level level, String format, Object argument0);

    public abstract void log(@NotNull Level level, String format, Object argument0, Object argument1);

    public abstract void log(@NotNull Level level, String format, Object... arguments);

    public abstract void log(@NotNull Level level, String format, Throwable throwable);

    @Override
    public boolean isTraceEnabled() {
        return isEnabled(Level.TRACE);
    }

    @Override
    public void trace(String message) {
        log(Level.TRACE, message);
    }

    @Override
    public void trace(String format, Object argument0) {
        log(Level.TRACE, format, argument0);
    }

    @Override
    public void trace(String format, Object argument0, Object argument1) {
        log(Level.TRACE, format, argument0, argument1);
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(Level.TRACE, format, arguments);
    }

    @Override
    public void trace(String format, Throwable throwable) {
        log(Level.TRACE, format, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return isEnabled(Level.DEBUG);
    }

    @Override
    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    @Override
    public void debug(String format, Object argument0) {
        log(Level.DEBUG, format, argument0);
    }

    @Override
    public void debug(String format, Object argument0, Object argument1) {
        log(Level.DEBUG, format, argument0, argument1);
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(Level.DEBUG, format, arguments);
    }

    @Override
    public void debug(String format, Throwable throwable) {
        log(Level.DEBUG, format, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return isEnabled(Level.INFO);
    }

    @Override
    public void info(String message) {
        log(Level.INFO, message);
    }

    @Override
    public void info(String format, Object argument0) {
        log(Level.INFO, format, argument0);
    }

    @Override
    public void info(String format, Object argument0, Object argument1) {
        log(Level.INFO, format, argument0, argument1);
    }

    @Override
    public void info(String format, Object... arguments) {
        log(Level.INFO, format, arguments);
    }

    @Override
    public void info(String format, Throwable throwable) {
        log(Level.INFO, format, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return isEnabled(Level.WARN);
    }

    @Override
    public void warn(String message) {
        log(Level.WARN, message);
    }

    @Override
    public void warn(String format, Object argument0) {
        log(Level.WARN, format, argument0);
    }

    @Override
    public void warn(String format, Object argument0, Object argument1) {
        log(Level.WARN, format, argument0, argument1);
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(Level.WARN, format, arguments);
    }

    @Override
    public void warn(String format, Throwable throwable) {
        log(Level.WARN, format, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return isEnabled(Level.ERROR);
    }

    @Override
    public void error(String message) {
        log(Level.ERROR, message);
    }

    @Override
    public void error(String format, Object argument0) {
        log(Level.ERROR, format, argument0);
    }

    @Override
    public void error(String format, Object argument0, Object argument1) {
        log(Level.ERROR, format, argument0, argument1);
    }

    @Override
    public void error(String format, Object... arguments) {
        log(Level.ERROR, format, arguments);
    }

    @Override
    public void error(String format, Throwable throwable) {
        log(Level.ERROR, format, throwable);
    }
}
