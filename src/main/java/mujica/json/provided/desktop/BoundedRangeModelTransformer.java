package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created on 2026/6/3.
 */
@CodeHistory(date = "2026/6/3")
public class BoundedRangeModelTransformer implements JsonContextTransformer<BoundedRangeModel> {

    public static final BoundedRangeModelTransformer INSTANCE = new BoundedRangeModelTransformer();

    @Override
    public void transform(@NotNull BoundedRangeModel in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("min");
            out.numberValue(in.getMinimum());
            out.key("max");
            out.numberValue(in.getMaximum());
            out.key("value");
            out.numberValue(in.getValue());
            out.key("adjusting");
            out.booleanValue(in.getValueIsAdjusting());
            out.key("extent");
            out.numberValue(in.getExtent());
        }
        out.closeObject();
    }
}
