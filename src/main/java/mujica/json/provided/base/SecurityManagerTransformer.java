package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/5/8")
public class SecurityManagerTransformer implements JsonContextTransformer<SecurityManager>, JsonStructure {

    public static final SecurityManagerTransformer INSTANCE = new SecurityManagerTransformer();

    static final FastString THREAD_GROUP = new FastString("threadGroup");

    @Override
    public void transform(@NotNull SecurityManager manager, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.CLASS);
            out.stringValue(manager.getClass().getName());
            out.key(THREAD_GROUP);
            ThreadGroupTransformer.INSTANCE.transform(manager.getThreadGroup(), out, context);
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        final SecurityManager manager = System.getSecurityManager();
        if (manager != null) {
            transform(manager, jh, null);
        } else {
            jh.nullValue();
        }
    }
}
