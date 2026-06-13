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
            out.stringKey("threadCache");
            out.numberValue(in.numThreadCaches());
            out.stringKey("allocation");
            out.numberValue(in.numAllocations());
            out.stringKey("smallAllocation");
            out.numberValue(in.numSmallAllocations());
            out.stringKey("normalAllocation");
            out.numberValue(in.numNormalAllocations());
            out.stringKey("hugeAllocation");
            out.numberValue(in.numHugeAllocations());
            out.stringKey("deallocation");
            out.numberValue(in.numDeallocations());
            out.stringKey("smallDeallocation");
            out.numberValue(in.numSmallDeallocations());
            out.stringKey("normalDeallocation");
            out.numberValue(in.numNormalDeallocations());
            out.stringKey("hugeDeallocation");
            out.numberValue(in.numHugeDeallocations());
            out.stringKey("activeAllocation");
            out.numberValue(in.numActiveAllocations());
            out.stringKey("activeSmallAllocation");
            out.numberValue(in.numActiveSmallAllocations());
            out.stringKey("activeNormalAllocation");
            out.numberValue(in.numActiveNormalAllocations());
            out.stringKey("activeHugeAllocation");
            out.numberValue(in.numActiveHugeAllocations());
            out.stringKey("activeByte");
            out.numberValue(in.numActiveBytes());
        }
        out.closeObject();
    }
}
