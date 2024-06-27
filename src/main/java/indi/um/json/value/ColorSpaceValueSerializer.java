package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import indi.um.util.value.EnumMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.color.ColorSpace;

/**
 * Created on 2022/9/3.
 */
public class ColorSpaceValueSerializer implements ValueSerializer<ColorSpace> {

    public static final ColorSpaceValueSerializer INSTANCE = new ColorSpaceValueSerializer();

    private static final String TYPE_PREFIX = "TYPE_";

    private static final int TYPE_PREFIX_LENGTH = TYPE_PREFIX.length();

    public static final EnumMapping TYPE = (new EnumMapping()).addFromClass(
            ColorSpace.class,
            name -> name.startsWith(TYPE_PREFIX),
            name -> name.substring(TYPE_PREFIX_LENGTH)
    );

    void serialize(String key, ColorSpace value, @NotNull JsonConsumer jc) {
        if (value == null) {
            return; // this ValueSerializer is special; it checks null value; null to undefined
        }
        jc.optionalKey(key);
        jc.openObject();
        jc.key("type");
        jc.stringValue(TYPE.forKey(value.getType()));
        jc.key("components");
        jc.openArray();
        final int componentCount = value.getNumComponents();
        for (int index = 0; index < componentCount; index++) {
            jc.openObject();
            jc.key("name");
            jc.stringValue(value.getName(index));
            jc.key("min");
            jc.numberValue(value.getMinValue(index));
            jc.key("max");
            jc.numberValue(value.getMaxValue(index));
            jc.closeObject();
        }
        jc.closeArray();
        jc.closeObject();
    }

    @Override
    public void serialize(String key, ColorSpace value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }
}
