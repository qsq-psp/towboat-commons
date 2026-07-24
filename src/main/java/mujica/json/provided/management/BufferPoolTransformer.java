package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.BufferPoolMXBean;

@CodeHistory(date = "2026/5/5")
public class BufferPoolTransformer implements JsonContextTransformer<BufferPoolMXBean> {

    public static final BufferPoolTransformer INSTANCE = new BufferPoolTransformer();

    static final FastString NAME = new FastString("name");

    static final FastString COUNT = new FastString("count");

    static final FastString TOTAL_CAPACITY = new FastString("totalCapacity");

    static final FastString MEMORY_USED = new FastString("memoryUsed");

    @Override
    public void transform(@NotNull BufferPoolMXBean bean, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(NAME);
            out.stringValue(bean.getName());
            out.key(COUNT);
            out.numberValue(bean.getCount());
            out.key(TOTAL_CAPACITY);
            out.numberValue(bean.getTotalCapacity());
            out.key(MEMORY_USED);
            out.numberValue(bean.getMemoryUsed());
        }
        out.closeObject();
    }
}
