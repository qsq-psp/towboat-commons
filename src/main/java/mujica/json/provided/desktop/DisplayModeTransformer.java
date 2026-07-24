package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2026/4/27")
public class DisplayModeTransformer implements JsonContextTransformer<DisplayMode> {

    public static final DisplayModeTransformer INSTANCE = new DisplayModeTransformer();

    @Override
    public void transform(@NotNull DisplayMode displayMode, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(RectangleTransformer.WIDTH);
            out.numberValue(displayMode.getWidth());
            out.key(RectangleTransformer.HEIGHT);
            out.numberValue(displayMode.getHeight());
            out.key("bitDepth");
            out.numberValue(displayMode.getBitDepth());
            out.key("refreshRate");
            out.numberValue(displayMode.getRefreshRate());
        }
        out.closeObject();
    }
}
