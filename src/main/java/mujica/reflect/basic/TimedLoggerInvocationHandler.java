package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.function.Function;

@CodeHistory(date = "2026/3/20")
public class TimedLoggerInvocationHandler extends LoggerInvocationHandler {

    public TimedLoggerInvocationHandler(@NotNull Object target, @NotNull Logger logger, @NotNull String id, @NotNull Function<Object, String> toStringFunction) {
        super(target, logger, id, toStringFunction);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (logger.isInfoEnabled()) {
            String argsString = toStringFunction.apply(args);
            long start, end;
            start = System.nanoTime();
            try {
                Object returnValue = method.invoke(target, args);
                end = System.nanoTime();
                logger.info("{}.{}{} = {} in {}", id, method.getName(), argsString, toStringFunction.apply(returnValue), timeString(start, end));
                return returnValue;
            } catch (Throwable e) {
                end = System.nanoTime();
                logger.info("{}.{}{} throws {} in {}", id, method.getName(), argsString, timeString(start, end), e);
                throw e;
            }
        } else {
            return method.invoke(target, args);
        }
    }

    String timeString(long start, long end) {
        return (end - start) + "ns";
    }
}
