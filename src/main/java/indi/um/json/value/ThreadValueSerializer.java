package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import io.netty.util.concurrent.FastThreadLocal;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/16.
 */
@SuppressWarnings("unused")
public class ThreadValueSerializer implements ValueSerializer<Thread>, JsonStructure {

    public static final ThreadValueSerializer INSTANCE = new ThreadValueSerializer();

    void serialize(String key, Thread value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("class");
        jc.stringValue(value.getClass().getName());
        jc.key("name");
        jc.stringValue(value.getName());
        jc.key("id");
        jc.numberValue(value.getId());
        jc.key("state");
        jc.stringValue(value.getState().name());
        jc.key("daemon");
        jc.booleanValue(value.isDaemon());
        jc.key("priority");
        jc.numberValue(value.getPriority());
        jc.key("classLoader");
        jc.stringValue(value.getContextClassLoader().getClass().getName());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, Thread value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, Thread.currentThread(), jc);
    }
}
