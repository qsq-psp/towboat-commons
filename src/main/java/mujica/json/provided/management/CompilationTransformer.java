package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.CompilationMXBean;

@CodeHistory(date = "2026/5/9")
public class CompilationTransformer implements JsonContextTransformer<CompilationMXBean> {

    public static final CompilationTransformer INSTANCE = new CompilationTransformer();

    static final FastString TOTAL_COMPILATION_TIME = new FastString("totalCompilationTime");

    @Override
    public void transform(@NotNull CompilationMXBean bean, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.NAME);
            out.stringValue(bean.getName());
        }
        if (bean.isCompilationTimeMonitoringSupported()) {
            out.key(TOTAL_COMPILATION_TIME);
            out.numberValue(bean.getTotalCompilationTime()); // unit is ms
        }
        out.closeObject();
    }
}
