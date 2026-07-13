package mujica.json.provided.management;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.MemoryManagerMXBean;

/**
 * Created on 2026/6/14.
 */
@CodeHistory(date = "2026/6/14")
public class MemoryManagerTransformer implements JsonContextTransformer<MemoryManagerMXBean> {

    @Override
    public void transform(@NotNull MemoryManagerMXBean bean, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.NAME);
            out.stringValue(bean.getName());
            out.key(GarbageCollectorTransformer.VALID);
            out.booleanValue(bean.isValid());
            out.key(GarbageCollectorTransformer.POOLS);
            out.openArray();
            for (String poolName : bean.getMemoryPoolNames()) {
                out.stringValue(poolName);
            }
            out.closeArray();
        }
        out.closeObject();
    }
}
