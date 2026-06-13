package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2022/9/1", project = "Ultramarine", name = "GraphicsConfigurationValueSerializer")
@CodeHistory(date = "2026/5/2")
public class GraphicsConfigurationTransformer implements JsonContextTransformer<GraphicsConfiguration> {

    public static final GraphicsConfigurationTransformer INSTANCE = new GraphicsConfigurationTransformer();

    @Override
    public void transform(GraphicsConfiguration in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("colorModel");
            ColorModelTransformer.INSTANCE.transform(in.getColorModel(), out, context);
            out.stringKey("bounds");
            RectangleTransformer.INSTANCE.transform(in.getBounds(), out, context);
            out.stringKey("translucency");
            out.booleanValue(in.isTranslucencyCapable());
        }
        out.closeObject();
    }
}
