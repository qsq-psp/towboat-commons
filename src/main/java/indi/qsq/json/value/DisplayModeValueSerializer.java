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
public class DisplayModeValueSerializer implements ValueSerializer<DisplayMode> {

    public static final DisplayModeValueSerializer INSTANCE = new DisplayModeValueSerializer();

    void serialize(String key, DisplayMode value, @NotNull JsonConsumer jc) {
        if (value == null) {
            return; // this ValueSerializer is special; it checks null value
        }
        jc.optionalKey(key);
        jc.openObject();
        jc.key("width");
        jc.numberValue(value.getWidth());
        jc.key("height");
        jc.numberValue(value.getHeight());
        jc.key("bitDepth");
        jc.numberValue(value.getBitDepth());
        jc.key("refreshRate");
        jc.numberValue(value.getRefreshRate());
        jc.closeObject();
    }

    @Override
    public void serialize(String key, DisplayMode value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }
}
