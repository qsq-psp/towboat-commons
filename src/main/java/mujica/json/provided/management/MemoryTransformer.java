package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.MemoryMXBean;

/**
 * Created on 2026/6/14.
 */
@CodeHistory(date = "2026/6/14")
public class MemoryTransformer implements JsonContextTransformer<MemoryMXBean> {

    static final FastString PENDING = new FastString("pending");

    static final FastString HEAP = new FastString("heap");

    static final FastString NON_HEAP = new FastString("nonHeap");

    @Override
    public void transform(@NotNull MemoryMXBean in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey(PENDING);
            out.numberValue(in.getObjectPendingFinalizationCount());
            out.stringKey(HEAP);
            MemoryUsageTransformer.INSTANCE.transform(in.getHeapMemoryUsage(), out, context);
            out.stringKey(NON_HEAP);
            MemoryUsageTransformer.INSTANCE.transform(in.getNonHeapMemoryUsage(), out, context);
            out.stringKey(ClassLoadingTransformer.VERBOSE);
            out.booleanValue(in.isVerbose());
        }
        out.closeObject();
    }
}
