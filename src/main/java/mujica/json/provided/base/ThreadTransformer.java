package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/1/3", project = "infrastructure", name = "ThreadValue")
@CodeHistory(date = "2022/7/16", project = "Ultramarine", name = "ThreadValueSerializer")
@CodeHistory(date = "2026/4/30")
public class ThreadTransformer implements JsonContextTransformer<Thread>, JsonStructure {

    public static final ThreadTransformer INSTANCE = new ThreadTransformer();

    static final FastString ID = new FastString("id");

    static final FastString STATE = new FastString("state");

    static final FastString DAEMON = new FastString("daemon");

    static final FastString PRIORITY = new FastString("priority");

    static final FastString CLASS_LOADER = new FastString("classLoader");

    @Override
    public void transform(Thread in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ClassLoaderTransformer.CLASS);
            out.stringValue(in.getClass().getName());
            out.stringKey(ClassLoaderTransformer.NAME);
            out.stringValue(in.getName());
            out.stringKey(ID);
            out.numberValue(in.getId());
            out.stringKey(STATE);
            out.stringValue(in.getState().toString());
            out.stringKey(DAEMON);
            out.booleanValue(in.isDaemon());
            out.stringKey(PRIORITY);
            out.numberValue(in.getPriority());
            out.stringKey(CLASS_LOADER);
            ClassLoaderTransformer.INSTANCE.transform(in.getContextClassLoader(), out, context);
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Thread.currentThread(), jh, null);
    }
}
