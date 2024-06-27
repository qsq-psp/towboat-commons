package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.reflect.JsonSerializer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/12.
 */
public class ClassValueSerializer implements ValueSerializer<Class<?>> {

    public static final ClassValueSerializer INSTANCE = new ClassValueSerializer();

    public void serialize(String key, Class<?> value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        int array = 0;
        while (value.isArray()) {
            array++;
            value = value.getComponentType();
        }
        String module = value.getModule().getName();
        if (module != null) {
            jc.key("module");
            jc.stringValue(module);
        }
        if (array != 0) {
            jc.key("array");
            jc.numberValue(array);
        }
        jc.key("class");
        jc.stringValue(value.getName());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, Class<?> value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }
}
