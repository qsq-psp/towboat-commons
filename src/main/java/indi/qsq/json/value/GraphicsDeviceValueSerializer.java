package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import indi.um.util.value.EnumMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2022/9/1.
 */
@SuppressWarnings("unused")
public class GraphicsDeviceValueSerializer implements ValueSerializer<GraphicsDevice>, JsonStructure {

    public static final GraphicsDeviceValueSerializer INSTANCE = new GraphicsDeviceValueSerializer();

    public static final EnumMapping TYPE = (new EnumMapping())
            .addDefault("raster-screen", GraphicsDevice.TYPE_RASTER_SCREEN)
            .add("printer", GraphicsDevice.TYPE_PRINTER)
            .add("image-buffer", GraphicsDevice.TYPE_IMAGE_BUFFER);

    void serialize(String key, GraphicsDevice value, @NotNull JsonConsumer jc) {
        if (value == null) {
            return; // this ValueSerializer is special; it checks null value; null to undefined
        }
        jc.optionalKey(key);
        jc.openObject();
        jc.key("type");
        jc.stringValue(TYPE.forKey(value.getType()));
        jc.key("id");
        jc.stringValue(value.getIDstring());
        jc.key("fullScreenSupported");
        jc.booleanValue(value.isFullScreenSupported());
        jc.key("displayChangeSupported");
        jc.booleanValue(value.isDisplayChangeSupported());
        jc.key("memory");
        jc.numberValue(value.getAvailableAcceleratedMemory());
        GraphicsConfigurationValueSerializer.INSTANCE.serialize("configuration", value.getDefaultConfiguration(), jc);
        DisplayModeValueSerializer.INSTANCE.serialize("displayMode", value.getDisplayMode(), jc);
        jc.closeObject();
    }

    @Override
    public void serialize(String key, GraphicsDevice value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), jc);
    }
}
