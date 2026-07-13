package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2026/5/3.
 */
@CodeHistory(date = "2026/5/3")
public class ThreadPoolExecutorTransformer implements JsonContextTransformer<ThreadPoolExecutor> {

    public static final ThreadPoolExecutorTransformer INSTANCE = new ThreadPoolExecutorTransformer();

    @Override
    public void transform(@NotNull ThreadPoolExecutor executor, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("shutdown");
            out.booleanValue(executor.isShutdown());
            out.key("terminating");
            out.booleanValue(executor.isTerminating());
            out.key("threadFactory");
            out.stringValue(executor.getThreadFactory().getClass().getName());
            out.key("rejectedExecutionHandler");
            out.stringValue(executor.getRejectedExecutionHandler().getClass().getName());
            out.key("corePoolSize");
            out.numberValue(executor.getCorePoolSize());
            out.key("allowCoreThreadTimeOut");
            out.booleanValue(executor.allowsCoreThreadTimeOut());
            out.key("maximumPoolSize");
            out.numberValue(executor.getMaximumPoolSize());
            out.key("keepAliveTime");
            out.numberValue(executor.getKeepAliveTime(TimeUnit.NANOSECONDS));
            out.key("workQueue");
            out.stringValue(executor.getQueue().getClass().getName());
            out.key("poolSize");
            out.numberValue(executor.getPoolSize());
            out.key("activeCount");
            out.numberValue(executor.getActiveCount());
            out.key("largestPoolSize");
            out.numberValue(executor.getLargestPoolSize());
            out.key("taskCount");
            out.numberValue(executor.getTaskCount());
            out.key("completedTaskCount");
            out.numberValue(executor.getCompletedTaskCount());
        }
        if (executor instanceof ScheduledThreadPoolExecutor) {
            ScheduledThreadPoolExecutor scheduled = (ScheduledThreadPoolExecutor) executor;
            out.key("continueExistingPeriodicTasksAfterShutdown");
            out.booleanValue(scheduled.getContinueExistingPeriodicTasksAfterShutdownPolicy());
            out.key("executeExistingDelayedTasksAfterShutdown");
            out.booleanValue(scheduled.getExecuteExistingDelayedTasksAfterShutdownPolicy());
            out.key("removeOnCancel");
            out.booleanValue(scheduled.getRemoveOnCancelPolicy());
        }
        out.closeObject();
    }
}
