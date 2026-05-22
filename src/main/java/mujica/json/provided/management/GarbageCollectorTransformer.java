package mujica.json.provided.management;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.lang.management.GarbageCollectorMXBean;

/**
 * Created on 2026/4/28.
 */
public class GarbageCollectorTransformer implements JsonContextTransformer<GarbageCollectorMXBean> {

    public static final GarbageCollectorTransformer INSTANCE = new GarbageCollectorTransformer();

    static final FastString VALID = new FastString("valid");

    static final FastString POOLS = new FastString("pools");

    static final FastString TIME = new FastString("time");

    @Override
    public void transform(GarbageCollectorMXBean in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(BufferPoolTransformer.NAME);
            out.stringValue(in.getName());
            out.stringKey(VALID);
            out.booleanValue(in.isValid());
            out.stringKey(POOLS);
            out.openArray();
            for (String poolName : in.getMemoryPoolNames()) {
                out.stringValue(poolName);
            }
            out.closeArray();
            out.stringKey(BufferPoolTransformer.COUNT);
            out.numberValue(in.getCollectionCount());
            out.stringKey(TIME);
            out.numberValue(in.getCollectionTime());
        }
        out.closeObject();
    }
}
