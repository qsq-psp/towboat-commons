package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/5/8.
 */
public class SecurityManagerTransformer implements JsonContextTransformer<SecurityManager>, JsonStructure {

    public static final SecurityManagerTransformer INSTANCE = new SecurityManagerTransformer();

    static final FastString THREAD_GROUP = new FastString("threadGroup");

    @Override
    public void transform(@NotNull SecurityManager in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ClassLoaderTransformer.CLASS);
            out.stringValue(in.getClass().getName());
            out.stringKey(THREAD_GROUP);
            ThreadGroupTransformer.INSTANCE.transform(in.getThreadGroup(), out, context);
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
