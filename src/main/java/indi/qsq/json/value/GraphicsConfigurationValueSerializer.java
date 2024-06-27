package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2022/9/1.
 */
public class GraphicsConfigurationValueSerializer implements ValueSerializer<GraphicsConfiguration> {

    public static final GraphicsConfigurationValueSerializer INSTANCE = new GraphicsConfigurationValueSerializer();

    void serialize(String key, GraphicsConfiguration value, @NotNull JsonConsumer jc) {
        if (value == null) {
            return; // this ValueSerializer is special; it checks null value
        }
        jc.optionalKey(key);
        jc.openObject();
        jc.key("translucency");
        jc.booleanValue(value.isTranslucencyCapable());
        ColorModelValueSerializer.INSTANCE.serialize("colorModel", value.getColorModel(), jc);
        Rectangle bounds = value.getBounds();
        if (bounds != null) {
            jc.key("x");
            jc.numberValue(bounds.x);
            jc.key("y");
            jc.numberValue(bounds.y);
            jc.key("width");
            jc.numberValue(bounds.width);
            jc.key("height");
            jc.numberValue(bounds.height);
        }
        ImageCapabilities imageCapabilities = value.getImageCapabilities();
        if (imageCapabilities != null) {
            jc.key("accelerated");
            jc.booleanValue(imageCapabilities.isAccelerated());
            jc.key("trueVolatile");
            jc.booleanValue(imageCapabilities.isTrueVolatile());
        }
        jc.closeObject();
    }

    @Override
    public void serialize(String key, GraphicsConfiguration value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }
}
