package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

@CodeHistory(date = "2026/6/11")
public class MemoryPoolTransformer implements JsonContextTransformer<MemoryPoolMXBean> {

    public static final MemoryPoolTransformer INSTANCE = new MemoryPoolTransformer();

    static final FastString TYPE = new FastString("type");

    static final FastString CURRENT = new FastString("current");

    static final FastString PEAK = new FastString("peak");

    static final FastString COLLECTION = new FastString("collection");

    static final FastString MANAGER = new FastString("manager");

    static final FastString THRESHOLD = new FastString("threshold");

    static final FastString THRESHOLD_EXCEEDED = new FastString("thresholdExceeded");

    static final FastString THRESHOLD_COUNT = new FastString("thresholdCount");

    static final FastString COLLECTION_THRESHOLD = new FastString("collectionThreshold");

    static final FastString COLLECTION_THRESHOLD_EXCEEDED = new FastString("collectionThresholdExceeded");

    static final FastString COLLECTION_THRESHOLD_COUNT = new FastString("collectionThresholdCount");

    @Override
    public void transform(@NotNull MemoryPoolMXBean bean, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(BufferPoolTransformer.NAME);
            out.stringValue(bean.getName());
            out.key(TYPE);
            out.stringValue(bean.getType().name());
        }
        {
            MemoryUsage usage = bean.getUsage();
            if (usage != null) {
                out.key(CURRENT);
                MemoryUsageTransformer.INSTANCE.transform(usage, out, context);
            }
        }
        {
            MemoryUsage usage = bean.getPeakUsage();
            if (usage != null) {
                out.key(PEAK);
                MemoryUsageTransformer.INSTANCE.transform(usage, out, context);
            }
        }
        {
            MemoryUsage usage = bean.getCollectionUsage();
            if (usage != null) {
                out.key(COLLECTION);
                MemoryUsageTransformer.INSTANCE.transform(usage, out, context);
            }
        }
        {
            out.key(GarbageCollectorTransformer.VALID);
            out.booleanValue(bean.isValid());
        }
        {
            String[] managerNames = bean.getMemoryManagerNames();
            out.key(MANAGER);
            out.openArray();
            for (String managerName : managerNames) {
                out.stringValue(managerName);
            }
            out.closeArray();
        }
        if (bean.isUsageThresholdSupported()) {
            out.key(THRESHOLD);
            out.numberValue(bean.getUsageThreshold());
            out.key(THRESHOLD_EXCEEDED);
            out.booleanValue(bean.isUsageThresholdExceeded());
            out.key(THRESHOLD_COUNT);
            out.numberValue(bean.getUsageThresholdCount());
        }
        if (bean.isCollectionUsageThresholdSupported()) {
            out.key(COLLECTION_THRESHOLD);
            out.numberValue(bean.getCollectionUsageThreshold());
            out.key(COLLECTION_THRESHOLD_EXCEEDED);
            out.booleanValue(bean.isCollectionUsageThresholdExceeded());
            out.key(COLLECTION_THRESHOLD_COUNT);
            out.numberValue(bean.getCollectionUsageThresholdCount());
        }
        out.closeObject();
    }
}
