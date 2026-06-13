package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ClassLoadingMXBean;

/**
 * Created on 2026/5/1.
 */
public class ClassLoadingTransformer implements JsonContextTransformer<ClassLoadingMXBean> {

    public static final ClassLoadingTransformer INSTANCE = new ClassLoadingTransformer();

    static final FastString TOTAL_LOADED = new FastString("totalLoaded");

    static final FastString LOADED = new FastString("loaded");

    static final FastString UNLOADED = new FastString("unloaded");

    static final FastString VERBOSE = new FastString("verbose");

    @Override
    public void transform(ClassLoadingMXBean in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(TOTAL_LOADED);
            out.numberValue(in.getTotalLoadedClassCount());
            out.stringKey(LOADED);
            out.numberValue(in.getLoadedClassCount());
            out.stringKey(UNLOADED);
            out.numberValue(in.getUnloadedClassCount());
            out.stringKey(VERBOSE);
            out.booleanValue(in.isVerbose());
        }
        out.closeObject();
    }
}
