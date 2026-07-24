package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/4/29")
public class ClassLoaderTransformer implements JsonContextTransformer<ClassLoader>, JsonStructure {

    public static final ClassLoaderTransformer INSTANCE = new ClassLoaderTransformer();

    static final FastString CLASS = new FastString("class");

    static final FastString PARALLEL = new FastString("parallel");

    static final FastString NAME = new FastString("name");

    static final FastString PARENT = new FastString("parent");

    @Override
    public void transform(@NotNull ClassLoader classLoader, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(CLASS);
            out.stringValue(classLoader.getClass().getName());
            out.key(PARALLEL);
            out.booleanValue(classLoader.isRegisteredAsParallelCapable());
            String name = classLoader.getName();
            if (name != null) {
                out.key(NAME);
                out.stringValue(name);
            }
            ClassLoader parent = classLoader.getParent();
            if (parent != null) {
                out.key(PARENT);
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
