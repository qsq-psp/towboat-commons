package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import io.netty.util.internal.InternalThreadLocalMap;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/8/20.
 */
@SuppressWarnings("unused")
public class ThreadLocalMapValueSerializer implements ValueSerializer<InternalThreadLocalMap>, JsonStructure {

    public static final ThreadLocalMapValueSerializer INSTANCE = new ThreadLocalMapValueSerializer();

    void serialize(String key, InternalThreadLocalMap value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("lastVariableIndex");
        jc.numberValue(InternalThreadLocalMap.lastVariableIndex());
        jc.key("size");
        jc.numberValue(value.size());
        jc.key("futureListenerStackDepth");
        jc.numberValue(value.futureListenerStackDepth());
        jc.key("localChannelReaderStackDepth");
        jc.numberValue(value.localChannelReaderStackDepth());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, InternalThreadLocalMap value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, InternalThreadLocalMap.get(), jc);
    }
}
