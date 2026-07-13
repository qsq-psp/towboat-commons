package mujica.json.provided.netty;

import io.netty.buffer.PoolArenaMetric;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/6/8.
 */
@CodeHistory(date = "2026/6/8")
public class PoolArenaMetricTransformer implements JsonContextTransformer<PoolArenaMetric> {

    public static final PoolArenaMetricTransformer INSTANCE = new PoolArenaMetricTransformer();

    @Override
    public void transform(@NotNull PoolArenaMetric in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("threadCache");
            out.numberValue(in.numThreadCaches());
            out.key("allocation");
            out.numberValue(in.numAllocations());
            out.key("smallAllocation");
            out.numberValue(in.numSmallAllocations());
            out.key("normalAllocation");
            out.numberValue(in.numNormalAllocations());
            out.key("hugeAllocation");
            out.numberValue(in.numHugeAllocations());
            out.key("deallocation");
            out.numberValue(in.numDeallocations());
            out.key("smallDeallocation");
            out.numberValue(in.numSmallDeallocations());
            out.key("normalDeallocation");
            out.numberValue(in.numNormalDeallocations());
            out.key("hugeDeallocation");
            out.numberValue(in.numHugeDeallocations());
            out.key("activeAllocation");
            out.numberValue(in.numActiveAllocations());
            out.key("activeSmallAllocation");
            out.numberValue(in.numActiveSmallAllocations());
            out.key("activeNormalAllocation");
            out.numberValue(in.numActiveNormalAllocations());
            out.key("activeHugeAllocation");
            out.numberValue(in.numActiveHugeAllocations());
            out.key("activeByte");
            out.numberValue(in.numActiveBytes());
        }
        out.closeObject();
    }
}
