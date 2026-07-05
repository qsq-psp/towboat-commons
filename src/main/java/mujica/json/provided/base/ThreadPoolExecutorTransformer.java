package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadPoolExecutor;

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
            out.stringKey("corePoolSize");
            out.numberValue(executor.getCorePoolSize());
            out.stringKey("maximumPoolSize");
            out.numberValue(executor.getMaximumPoolSize());
            out.stringKey("poolSize");
            out.numberValue(executor.getPoolSize());
            out.stringKey("activeCount");
            out.numberValue(executor.getActiveCount());
            out.stringKey("largestPoolSize");
            out.numberValue(executor.getLargestPoolSize());
            out.stringKey("taskCount");
            out.numberValue(executor.getTaskCount());
            out.stringKey("completedTaskCount");
            out.numberValue(executor.getCompletedTaskCount());
        }
        out.closeObject();
    }
}
