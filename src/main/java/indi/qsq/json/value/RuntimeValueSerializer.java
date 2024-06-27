package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.reflect.JsonSerializer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/12.
 */
@SuppressWarnings("unused")
public class RuntimeValueSerializer implements ValueSerializer<Runtime>, JsonStructure {

    public static final RuntimeValueSerializer INSTANCE = new RuntimeValueSerializer();

    void serialize(String key, Runtime value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("availableProcessors");
        jc.numberValue(value.availableProcessors());
        jc.key("freeMemory");
        jc.numberValue(value.freeMemory());
        jc.key("totalMemory");
        jc.numberValue(value.totalMemory());
        jc.key("maxMemory");
        jc.numberValue(value.maxMemory());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, Runtime value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, Runtime.getRuntime(), jc);
    }
}
