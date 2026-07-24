package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2022/9/1", project = "Ultramarine", name = "GraphicsConfigurationValueSerializer")
@CodeHistory(date = "2026/5/2")
public class GraphicsConfigurationTransformer implements JsonContextTransformer<GraphicsConfiguration> {

    public static final GraphicsConfigurationTransformer INSTANCE = new GraphicsConfigurationTransformer();

    @Override
    public void transform(@NotNull GraphicsConfiguration graphicsConfiguration, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("colorModel");
            ColorModelTransformer.INSTANCE.transform(graphicsConfiguration.getColorModel(), out, context);
            out.key("bounds");
            RectangleTransformer.INSTANCE.transform(graphicsConfiguration.getBounds(), out, context);
            out.key("translucency");
            out.booleanValue(graphicsConfiguration.isTranslucencyCapable());
        }
        out.closeObject();
    }
}
