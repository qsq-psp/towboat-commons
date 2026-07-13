package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/1/4", project = "infrastructure", name = "ThreadGroupValue")
@CodeHistory(date = "2022/7/19", project = "Ultramarine", name = "ThreadGroupValueSerializer")
@CodeHistory(date = "2026/4/30")
public class ThreadGroupTransformer implements JsonContextTransformer<ThreadGroup>, JsonStructure {

    public static final ThreadGroupTransformer INSTANCE = new ThreadGroupTransformer();

    static final FastString PARENT = new FastString("parent");

    static final FastString MAX_PRIORITY = new FastString("maxPriority");

    static final FastString DESTROYED = new FastString("destroyed");

    static final FastString ACTIVE_COUNT = new FastString("activeCount");

    @Override
    public void transform(@NotNull ThreadGroup in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(in.getName());
        }
        {
            ThreadGroup parent = in.getParent();
            if (parent != null) {
                out.key(PARENT);
                out.stringValue(parent.getName());
            }
        }
        {
            out.key(MAX_PRIORITY);
            out.numberValue(in.getMaxPriority());
            out.key(ThreadTransformer.DAEMON);
            out.booleanValue(in.isDaemon());
            out.key(DESTROYED);
            out.booleanValue(in.isDestroyed());
            out.key(ACTIVE_COUNT);
            out.numberValue(in.activeCount());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Thread.currentThread().getThreadGroup(), jh, null);
    }
}
