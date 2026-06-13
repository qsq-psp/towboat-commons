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
        out.stringKey(key);
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
            out.stringKey("threadLocalCacheCount");
            out.numberValue(in.numThreadLocalCaches());
            out.stringKey("smallCacheSize");
            out.numberValue(in.smallCacheSize());
            out.stringKey("normalCacheSize");
            out.numberValue(in.normalCacheSize());
            out.stringKey("chunkSize");
            out.numberValue(in.chunkSize());
            out.stringKey("usedHeapMemory");
            out.numberValue(in.usedHeapMemory());
            out.stringKey("usedDirectMemory");
            out.numberValue(in.usedDirectMemory());
        }
        out.closeObject();
    }
}
