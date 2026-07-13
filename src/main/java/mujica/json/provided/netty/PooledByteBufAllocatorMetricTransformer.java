package mujica.json.provided.netty;

import io.netty.buffer.PoolArenaMetric;
import io.netty.buffer.PooledByteBufAllocatorMetric;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created on 2026/6/8.
 */
public class PooledByteBufAllocatorMetricTransformer implements JsonContextTransformer<PooledByteBufAllocatorMetric> {

    private static void transformExposedList(@NotNull String key, @NotNull List<PoolArenaMetric> list, @NotNull JsonHandler out, @Nullable JsonContext context) {
        if (list.isEmpty()) {
            return;
        }
        out.key(key);
        out.openArray();
        for (PoolArenaMetric item : list) {
            PoolArenaMetricTransformer.INSTANCE.transform(item, out, context);
        }
        out.closeArray();
    }

    @Override
    public void transform(@NotNull PooledByteBufAllocatorMetric in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            transformExposedList("heap", in.heapArenas(), out, context);
            transformExposedList("direct", in.heapArenas(), out, context);
        }
        {
            out.key("threadLocalCacheCount");
            out.numberValue(in.numThreadLocalCaches());
            out.key("smallCacheSize");
            out.numberValue(in.smallCacheSize());
            out.key("normalCacheSize");
            out.numberValue(in.normalCacheSize());
            out.key("chunkSize");
            out.numberValue(in.chunkSize());
            out.key("usedHeapMemory");
            out.numberValue(in.usedHeapMemory());
            out.key("usedDirectMemory");
            out.numberValue(in.usedDirectMemory());
        }
        out.closeObject();
    }
}
