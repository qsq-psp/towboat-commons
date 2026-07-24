package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2026/6/30")
public class ImageCapabilitiesTransformer implements JsonContextTransformer<ImageCapabilities> {

    @Override
    public void transform(@NotNull ImageCapabilities imageCapabilities, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("accelerated");
            out.booleanValue(imageCapabilities.isAccelerated());
            out.key("trueVolatile");
            out.booleanValue(imageCapabilities.isTrueVolatile());
        }
        out.closeObject();
    }
}
