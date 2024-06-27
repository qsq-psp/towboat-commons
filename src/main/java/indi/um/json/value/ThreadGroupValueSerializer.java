package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2022/7/19.
 */
@SuppressWarnings("unused")
public class ThreadGroupValueSerializer implements ValueSerializer<ThreadGroup>, JsonStructure {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadGroupValueSerializer.class);

    public static final ThreadGroupValueSerializer INSTANCE = new ThreadGroupValueSerializer();

    void serialize(String key, ThreadGroup value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.objectEntry("name", value.getName()); // name may be null
        jc.key("maxPriority");
        jc.numberValue(value.getMaxPriority());
        jc.key("daemon");
        jc.booleanValue(value.isDaemon());
        jc.key("destroyed");
        jc.booleanValue(value.isDestroyed());
        try {
            ThreadGroup parent = value.getParent();
            if (parent != null) {
                serialize("parent", parent, jc);
            }
        } catch (Throwable e) {
            LOGGER.warn("parent", e);
        }
        jc.closeObject();
    }

    @Override
    public void serialize(String key, ThreadGroup value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, Thread.currentThread().getThreadGroup(), jc); // The current thread is not dead, so the ThreadGroup value is always not null
    }
}
