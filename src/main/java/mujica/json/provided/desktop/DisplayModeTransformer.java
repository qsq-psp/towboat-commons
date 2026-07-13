package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2026/4/27.
 */
public class DisplayModeTransformer implements JsonContextTransformer<DisplayMode> {

    public static final DisplayModeTransformer INSTANCE = new DisplayModeTransformer();

    @Override
    public void transform(@NotNull DisplayMode in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(RectangleTransformer.WIDTH);
            out.numberValue(in.getWidth());
            out.key(RectangleTransformer.HEIGHT);
            out.numberValue(in.getHeight());
            out.key("bitDepth");
            out.numberValue(in.getBitDepth());
            out.key("refreshRate");
            out.numberValue(in.getRefreshRate());
        }
        out.closeObject();
    }
}
