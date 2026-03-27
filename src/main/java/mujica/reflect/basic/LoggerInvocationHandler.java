package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import mujica.text.format.AppenderToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Function;

@CodeHistory(date = "2026/3/18")
public class LoggerInvocationHandler implements InvocationHandler {

    @NotNull
    final Object target;

    @NotNull
    final Logger logger;

    @NotNull
    final String id;

    @NotNull
    final Function<Object, String> toStringFunction;

    public LoggerInvocationHandler(@NotNull Object target, @NotNull Logger logger, @NotNull String id, @NotNull Function<Object, String> toStringFunction) {
        super();
        this.target = target;
        this.logger = logger;
        this.id = id;
        this.toStringFunction = toStringFunction;
    }

    public LoggerInvocationHandler(@NotNull Object target, @NotNull Logger logger, @NotNull Function<Object, String> toStringFunction) {
        this(target, logger, target.toString(), toStringFunction);
    }

    public LoggerInvocationHandler(@NotNull Object target, @NotNull Logger logger, @NotNull String id) {
        this(target, logger, id, AppenderToStringBuilder.Java.get());
    }

    public LoggerInvocationHandler(@NotNull Object target, @NotNull Logger logger) {
        this(target, logger, target.toString(), AppenderToStringBuilder.Java.get());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (logger.isInfoEnabled()) {
            String argsString = toStringFunction.apply(args);
            try {
                Object returnValue = method.invoke(target, args);
                logger.info("{}.{}{} = {}", id, method.getName(), argsString, toStringFunction.apply(returnValue));
                return returnValue;
            } catch (Throwable e) {
                logger.info("{}.{}{} throws {}", id, method.getName(), argsString, e.getClass().getSimpleName(), e);
                throw e;
            }
        } else {
            return method.invoke(target, args);
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "LoggerInvocationHandler[logger = " + logger + ", id = " + id + "]";
    }
}
