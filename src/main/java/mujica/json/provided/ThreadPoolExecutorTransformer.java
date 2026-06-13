package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created on 2026/5/3.
 */
public class ThreadPoolExecutorTransformer implements JsonContextTransformer<ThreadPoolExecutor> {

    public static final ThreadPoolExecutorTransformer INSTANCE = new ThreadPoolExecutorTransformer();

    @Override
    public void transform(@NotNull ThreadPoolExecutor in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("corePoolSize");
            out.numberValue(in.getCorePoolSize());
            out.stringKey("maximumPoolSize");
            out.numberValue(in.getMaximumPoolSize());
            out.stringKey("poolSize");
            out.numberValue(in.getPoolSize());
            out.stringKey("activeCount");
            out.numberValue(in.getActiveCount());
            out.stringKey("largestPoolSize");
            out.numberValue(in.getLargestPoolSize());
            out.stringKey("taskCount");
            out.numberValue(in.getTaskCount());
            out.stringKey("completedTaskCount");
            out.numberValue(in.getCompletedTaskCount());
        }
        out.closeObject();
    }
}
