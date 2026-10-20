package mujica.json.provided.netty;

import io.netty.buffer.PoolArenaMetric;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/6/8")
public class PoolArenaMetricTransformer implements JsonContextTransformer<PoolArenaMetric> {

    public static final PoolArenaMetricTransformer INSTANCE = new PoolArenaMetricTransformer();

    @Override
    public void transform(@NotNull PoolArenaMetric metric, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("threadCache");
            out.numberValue(metric.numThreadCaches());
            out.key("allocation");
            out.numberValue(metric.numAllocations());
            out.key("smallAllocation");
            out.numberValue(metric.numSmallAllocations());
            out.key("normalAllocation");
            out.numberValue(metric.numNormalAllocations());
            out.key("hugeAllocation");
            out.numberValue(metric.numHugeAllocations());
            out.key("deallocation");
            out.numberValue(metric.numDeallocations());
            out.key("smallDeallocation");
            out.numberValue(metric.numSmallDeallocations());
            out.key("normalDeallocation");
            out.numberValue(metric.numNormalDeallocations());
            out.key("hugeDeallocation");
            out.numberValue(metric.numHugeDeallocations());
            out.key("activeAllocation");
            out.numberValue(metric.numActiveAllocations());
            out.key("activeSmallAllocation");
            out.numberValue(metric.numActiveSmallAllocations());
            out.key("activeNormalAllocation");
            out.numberValue(metric.numActiveNormalAllocations());
            out.key("activeHugeAllocation");
            out.numberValue(metric.numActiveHugeAllocations());
            out.key("activeByte");
            out.numberValue(metric.numActiveBytes());
        }
        out.closeObject();
    }
}
