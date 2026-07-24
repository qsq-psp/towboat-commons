package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2022/9/1", project = "Ultramarine", name = "GraphicsDeviceValueSerializer")
@CodeHistory(date = "2026/5/4")
public class GraphicsDeviceTransformer implements JsonContextTransformer<GraphicsDevice>, JsonStructure {

    public static final GraphicsDeviceTransformer INSTANCE = new GraphicsDeviceTransformer();

    @Override
    public void transform(@Nullable GraphicsDevice graphicsDevice, @NotNull JsonHandler out, @Nullable JsonContext context) {
        if (graphicsDevice == null) {
            out.nullValue();
            return;
        }
        out.openObject();
        {
            int type = graphicsDevice.getType();
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
            out.stringValue(graphicsDevice.getIDstring());
            out.key("configuration");
            GraphicsConfigurationTransformer.INSTANCE.transform(graphicsDevice.getDefaultConfiguration(), out, context);
            out.key("fullScreenSupported");
            out.booleanValue(graphicsDevice.isFullScreenSupported());
            out.key("displayChangeSupported");
            out.booleanValue(graphicsDevice.isDisplayChangeSupported());
            out.key("displayMode");
            DisplayModeTransformer.INSTANCE.transform(graphicsDevice.getDisplayMode(), out, context);
            out.key("memory");
            out.numberValue(graphicsDevice.getAvailableAcceleratedMemory());
            out.key("windowShapingSupported");
            out.booleanValue(graphicsDevice.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT));
            out.key("windowOpacitySupported");
            out.booleanValue(graphicsDevice.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT));
            out.key("windowPerpixelTranslucencySupported");
            out.booleanValue(graphicsDevice.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT));
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
