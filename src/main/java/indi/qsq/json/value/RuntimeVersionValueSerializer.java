package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.reflect.JsonSerializer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created on 2022/7/12.
 */
@SuppressWarnings("unused")
public class RuntimeVersionValueSerializer implements ValueSerializer<Runtime.Version>, JsonStructure {

    public static final RuntimeVersionValueSerializer INSTANCE = new RuntimeVersionValueSerializer();

    void serialize(String key, Runtime.Version value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        final List<Integer> list = value.version();
        final int size = list.size();
        if (size > 0) {
            jc.key("feature");
            jc.numberValue(list.get(0));
            if (size > 1) {
                jc.key("interim");
                jc.numberValue(list.get(1));
                if (size > 2) {
                    jc.key("update");
                    jc.numberValue(list.get(2));
                    if (size > 3) {
                        jc.key("patch");
                        jc.numberValue(list.get(3));
                    }
                }
            }
        }
        jc.optionalStringEntry("pre", value.pre());
        jc.optionalIntegerEntry("build", value.build());
        jc.optionalStringEntry("optional", value.optional());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, Runtime.Version value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, Runtime.version(), jc);
    }
}
