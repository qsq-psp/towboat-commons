package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/18.
 */
@SuppressWarnings("unused")
public class ClassLoaderValueSerializer implements ValueSerializer<ClassLoader>, JsonStructure {

    public static final ClassLoaderValueSerializer INSTANCE = new ClassLoaderValueSerializer();

    void serialize(String key, ClassLoader value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        ClassValueSerializer.INSTANCE.serialize("class", value.getClass(), jc);
        jc.key("name");
        jc.stringValue(value.getName());
        jc.key("parent");
        ClassLoader parent = value.getParent();
        if (parent != null) {
            jc.stringValue(parent.getName());
        } else {
            jc.nullValue();
        }
        jc.closeObject();
    }

    @Override
    public void serialize(String key, ClassLoader value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, getClass().getClassLoader(), jc);
    }
}
