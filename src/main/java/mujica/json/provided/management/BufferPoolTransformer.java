package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.management.BufferPoolMXBean;

@CodeHistory(date = "2026/5/5")
public class BufferPoolTransformer implements JsonContextTransformer<BufferPoolMXBean> {

    public static final BufferPoolTransformer INSTANCE = new BufferPoolTransformer();

    static final FastString NAME = new FastString("name");

    static final FastString COUNT = new FastString("count");

    static final FastString TOTAL_CAPACITY = new FastString("totalCapacity");

    static final FastString MEMORY_USED = new FastString("memoryUsed");

    @Override
    public void transform(@NotNull BufferPoolMXBean in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(NAME);
            out.stringValue(in.getName());
            out.stringKey(COUNT);
            out.numberValue(in.getCount());
            out.stringKey(TOTAL_CAPACITY);
            out.numberValue(in.getTotalCapacity());
            out.stringKey(MEMORY_USED);
            out.numberValue(in.getMemoryUsed());
        }
        out.closeObject();
    }
}
