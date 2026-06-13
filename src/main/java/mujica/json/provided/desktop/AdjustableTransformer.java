package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Created on 2026/6/3.
 */
public class AdjustableTransformer implements JsonContextTransformer<Adjustable> {

    public static final AdjustableTransformer INSTANCE = new AdjustableTransformer();

    @Override
    public void transform(@NotNull Adjustable in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey("min");
            out.numberValue(in.getMinimum());
            out.stringKey("max");
            out.numberValue(in.getMaximum());
            out.stringKey("unit");
            out.numberValue(in.getUnitIncrement());
            out.stringKey("block");
            out.numberValue(in.getBlockIncrement());
            out.stringKey("visible");
            out.numberValue(in.getVisibleAmount());
            out.stringKey("value");
            out.numberValue(in.getValue());
        }
        out.closeObject();
    }
}
