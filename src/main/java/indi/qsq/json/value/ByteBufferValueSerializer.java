package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Created on 2022/7/18.
 */
public class ByteBufferValueSerializer implements ValueSerializer<ByteBuffer> {
    
    @Override
    public void serialize(String key, ByteBuffer value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        ClassValueSerializer.INSTANCE.serialize("class", value.getClass(), jc, cc, js);
        jc.key("capacity");
        jc.numberValue(value.capacity());
        jc.key("position");
        jc.numberValue(value.position());
        jc.key("limit");
        jc.numberValue(value.limit());
        jc.key("readOnly");
        jc.booleanValue(value.isReadOnly());
        jc.key("hasArray");
        jc.booleanValue(value.hasArray());
        jc.key("direct");
        jc.booleanValue(value.isDirect());
        jc.closeObject();
    }
}
