package mujica.json.provided;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/29.
 */
public class ClassLoaderTransformer implements JsonContextTransformer<ClassLoader>, JsonStructure {

    public static final ClassLoaderTransformer INSTANCE = new ClassLoaderTransformer();

    static final FastString CLASS = new FastString("class");

    static final FastString PARALLEL = new FastString("parallel");

    static final FastString NAME = new FastString("name");

    static final FastString PARENT = new FastString("parent");

    @Override
    public void transform(ClassLoader in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(CLASS);
            out.stringValue(in.getClass().getName());
            out.stringKey(PARALLEL);
            out.booleanValue(in.isRegisteredAsParallelCapable());
            String name = in.getName();
            if (name != null) {
                out.stringKey(NAME);
                out.stringValue(name);
            }
            ClassLoader parent = in.getParent();
            if (parent != null) {
                out.stringKey(PARENT);
                transform(parent, out, context);
            }
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(getClass().getClassLoader(), jh, null);
    }
}
