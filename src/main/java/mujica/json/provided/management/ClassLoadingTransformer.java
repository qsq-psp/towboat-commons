package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.ClassLoadingMXBean;

@CodeHistory(date = "2026/5/1")
public class ClassLoadingTransformer implements JsonContextTransformer<ClassLoadingMXBean> {

    public static final ClassLoadingTransformer INSTANCE = new ClassLoadingTransformer();

    static final FastString TOTAL_LOADED = new FastString("totalLoaded");

    static final FastString LOADED = new FastString("loaded");

    static final FastString UNLOADED = new FastString("unloaded");

    static final FastString VERBOSE = new FastString("verbose");

    @Override
    public void transform(@NotNull ClassLoadingMXBean bean, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(TOTAL_LOADED);
            out.numberValue(bean.getTotalLoadedClassCount());
            out.key(LOADED);
            out.numberValue(bean.getLoadedClassCount());
            out.key(UNLOADED);
            out.numberValue(bean.getUnloadedClassCount());
            out.key(VERBOSE);
            out.booleanValue(bean.isVerbose());
        }
        out.closeObject();
    }
}
