package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;

/**
 * Created on 2022/11/29.
 */
@SuppressWarnings("unused")
public class SaxAttributesValueSerializer implements ValueSerializer<Attributes> {

    @Override
    public void serialize(String key, Attributes value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        final int length = value.getLength();
        jc.openArray();
        for (int index = 0; index < length; index++) {
            jc.openObject();
            jc.nonEmptyStringEntry("uri", value.getURI(index));
            jc.nonEmptyStringEntry("localName", value.getLocalName(index));
            jc.optionalStringEntry("qualifiedName", value.getQName(index));
            jc.nonEmptyStringEntry("type", value.getType(index));
            jc.optionalStringEntry("value", value.getValue(index));
            jc.closeObject();
        }
        jc.closeArray();
    }
}
