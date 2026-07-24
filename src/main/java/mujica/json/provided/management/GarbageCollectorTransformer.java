package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.GarbageCollectorMXBean;

@CodeHistory(date = "2026/4/28")
public class GarbageCollectorTransformer implements JsonContextTransformer<GarbageCollectorMXBean> {

    public static final GarbageCollectorTransformer INSTANCE = new GarbageCollectorTransformer();

    static final FastString VALID = new FastString("valid");

    static final FastString POOLS = new FastString("pools");

    static final FastString TIME = new FastString("time");

    @Override
    public void transform(@NotNull GarbageCollectorMXBean bean, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.NAME);
            out.stringValue(bean.getName());
            out.key(VALID);
            out.booleanValue(bean.isValid());
            out.key(POOLS);
            out.openArray();
            for (String poolName : bean.getMemoryPoolNames()) {
                out.stringValue(poolName);
            }
            out.closeArray();
            out.key(BufferPoolTransformer.COUNT);
            out.numberValue(bean.getCollectionCount());
            out.key(TIME);
            out.numberValue(bean.getCollectionTime());
        }
        out.closeObject();
    }
}
