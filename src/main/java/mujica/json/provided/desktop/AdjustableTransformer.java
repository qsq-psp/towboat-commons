package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2026/6/3")
public class AdjustableTransformer implements JsonContextTransformer<Adjustable> {

    public static final AdjustableTransformer INSTANCE = new AdjustableTransformer();

    @Override
    public void transform(@NotNull Adjustable adjustable, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("min");
            out.numberValue(adjustable.getMinimum());
            out.key("max");
            out.numberValue(adjustable.getMaximum());
            out.key("unit");
            out.numberValue(adjustable.getUnitIncrement());
            out.key("block");
            out.numberValue(adjustable.getBlockIncrement());
            out.key("visible");
            out.numberValue(adjustable.getVisibleAmount());
            out.key("value");
            out.numberValue(adjustable.getValue());
        }
        out.closeObject();
    }
}
