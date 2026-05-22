package mujica.json.provided.desktop;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
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
    public void transform(DisplayMode in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(RectangleTransformer.WIDTH);
            out.numberValue(in.getWidth());
            out.stringKey(RectangleTransformer.HEIGHT);
            out.numberValue(in.getHeight());
            out.stringKey("bitDepth");
            out.numberValue(in.getBitDepth());
            out.stringKey("refreshRate");
            out.numberValue(in.getRefreshRate());
        }
        out.closeObject();
    }
}
