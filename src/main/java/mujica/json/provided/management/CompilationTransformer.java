package mujica.json.provided.management;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.lang.management.CompilationMXBean;

/**
 * Created on 2026/5/9.
 */
public class CompilationTransformer implements JsonContextTransformer<CompilationMXBean> {

    public static final CompilationTransformer INSTANCE = new CompilationTransformer();

    static final FastString TOTAL_COMPILATION_TIME = new FastString("totalCompilationTime");

    @Override
    public void transform(@NotNull CompilationMXBean in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(BufferPoolTransformer.NAME);
            out.stringValue(in.getName());
        }
        if (in.isCompilationTimeMonitoringSupported()) {
            out.stringKey(TOTAL_COMPILATION_TIME);
            out.numberValue(in.getTotalCompilationTime()); // unit is ms
        }
        out.closeObject();
    }
}
