package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@CodeHistory(date = "2026/6/3")
public class BoundedRangeModelTransformer implements JsonContextTransformer<BoundedRangeModel> {

    public static final BoundedRangeModelTransformer INSTANCE = new BoundedRangeModelTransformer();

    @Override
    public void transform(@NotNull BoundedRangeModel model, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("min");
            out.numberValue(model.getMinimum());
            out.key("max");
            out.numberValue(model.getMaximum());
            out.key("value");
            out.numberValue(model.getValue());
            out.key("adjusting");
            out.booleanValue(model.getValueIsAdjusting());
            out.key("extent");
            out.numberValue(model.getExtent());
        }
        out.closeObject();
    }
}
