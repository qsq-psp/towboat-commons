package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Created on 2026/5/4.
 */
@CodeHistory(date = "2022/9/1", project = "Ultramarine", name = "GraphicsDeviceValueSerializer")
@CodeHistory(date = "2026/5/4")
public class GraphicsDeviceTransformer implements JsonContextTransformer<GraphicsDevice>, JsonStructure {

    public static final GraphicsDeviceTransformer INSTANCE = new GraphicsDeviceTransformer();

    @Override
    public void transform(@Nullable GraphicsDevice in, @NotNull JsonHandler out, JsonContext context) {
        if (in == null) {
            out.nullValue();
            return;
        }
        out.openObject();
        {
            int type = in.getType();
            out.key("type");
            switch (type) {
                case GraphicsDevice.TYPE_RASTER_SCREEN:
                    out.stringValue("rasterScreen");
                    break;
                case GraphicsDevice.TYPE_PRINTER:
                    out.stringValue("printer");
                    break;
                case GraphicsDevice.TYPE_IMAGE_BUFFER:
                    out.stringValue("imageBuffer");
                    break;
                default:
                    out.numberValue(type);
                    break;
            }
        }
        {
            out.key("id");
            out.stringValue(in.getIDstring());
            out.key("configuration");
            GraphicsConfigurationTransformer.INSTANCE.transform(in.getDefaultConfiguration(), out, context);
            out.key("fullScreenSupported");
            out.booleanValue(in.isFullScreenSupported());
            out.key("displayChangeSupported");
            out.booleanValue(in.isDisplayChangeSupported());
            out.key("displayMode");
            DisplayModeTransformer.INSTANCE.transform(in.getDisplayMode(), out, context);
            out.key("memory");
            out.numberValue(in.getAvailableAcceleratedMemory());
            out.key("windowShapingSupported");
            out.booleanValue(in.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT));
            out.key("windowOpacitySupported");
            out.booleanValue(in.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT));
            out.key("windowPerpixelTranslucencySupported");
            out.booleanValue(in.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT));
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(getDefaultScreenDevice(), jh, null);
    }

    @Nullable
    public static GraphicsDevice getDefaultScreenDevice() {
        try {
            return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        } catch (Exception | AWTError ignore) {
            return null;
        }
    }
}
