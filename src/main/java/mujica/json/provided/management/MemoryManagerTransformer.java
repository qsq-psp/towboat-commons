package mujica.json.provided.management;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.MemoryManagerMXBean;

/**
 * Created on 2026/6/14.
 */
public class MemoryManagerTransformer implements JsonContextTransformer<MemoryManagerMXBean> {

    @Override
    public void transform(@NotNull MemoryManagerMXBean in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey(BufferPoolTransformer.NAME);
            out.stringValue(in.getName());
            out.stringKey(GarbageCollectorTransformer.VALID);
            out.booleanValue(in.isValid());
            out.stringKey(GarbageCollectorTransformer.POOLS);
            out.openArray();
            for (String poolName : in.getMemoryPoolNames()) {
                out.stringValue(poolName);
            }
            out.closeArray();
        }
        out.closeObject();
    }
}
