package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created on 2026/6/16.
 */
public class SpringTransformer implements JsonContextTransformer<Spring> {

    @Override
    public void transform(@NotNull Spring in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("minimum");
            out.numberValue(in.getMinimumValue());
            out.key("preferred");
            out.numberValue(in.getPreferredValue());
            out.key("maximum");
            out.numberValue(in.getMaximumValue());
            out.key("value");
            out.numberValue(in.getValue());
        }
        out.closeObject();
    }
}
