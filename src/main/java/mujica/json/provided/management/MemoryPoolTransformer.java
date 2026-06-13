package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

/**
 * Created on 2026/6/11.
 */
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
    public void transform(@NotNull MemoryPoolMXBean in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey(BufferPoolTransformer.NAME);
            out.stringValue(in.getName());
            out.stringKey(TYPE);
            out.stringValue(in.getType().name());
        }
        {
            MemoryUsage usage = in.getUsage();
            if (usage != null) {
                out.stringKey(CURRENT);
                MemoryUsageTransformer.INSTANCE.transform(usage, out, context);
            }
        }
        {
            MemoryUsage usage = in.getPeakUsage();
            if (usage != null) {
                out.stringKey(PEAK);
                MemoryUsageTransformer.INSTANCE.transform(usage, out, context);
            }
        }
        {
            MemoryUsage usage = in.getCollectionUsage();
            if (usage != null) {
                out.stringKey(COLLECTION);
                MemoryUsageTransformer.INSTANCE.transform(usage, out, context);
            }
        }
        {
            out.stringKey(GarbageCollectorTransformer.VALID);
            out.booleanValue(in.isValid());
        }
        {
            String[] managerNames = in.getMemoryManagerNames();
            out.stringKey(MANAGER);
            out.openArray();
            for (String managerName : managerNames) {
                out.stringValue(managerName);
            }
            out.closeArray();
        }
        if (in.isUsageThresholdSupported()) {
            out.stringKey(THRESHOLD);
            out.numberValue(in.getUsageThreshold());
            out.stringKey(THRESHOLD_EXCEEDED);
            out.booleanValue(in.isUsageThresholdExceeded());
            out.stringKey(THRESHOLD_COUNT);
            out.numberValue(in.getUsageThresholdCount());
        }
        if (in.isCollectionUsageThresholdSupported()) {
            out.stringKey(COLLECTION_THRESHOLD);
            out.numberValue(in.getCollectionUsageThreshold());
            out.stringKey(COLLECTION_THRESHOLD_EXCEEDED);
            out.booleanValue(in.isCollectionUsageThresholdExceeded());
            out.stringKey(COLLECTION_THRESHOLD_COUNT);
            out.numberValue(in.getCollectionUsageThresholdCount());
        }
        out.closeObject();
    }
}
