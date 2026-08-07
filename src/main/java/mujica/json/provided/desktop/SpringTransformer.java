package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@CodeHistory(date = "2026/6/16")
public class SpringTransformer implements JsonContextTransformer<Spring> {

    @Override
    public void transform(@NotNull Spring spring, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("minimum");
            out.numberValue(spring.getMinimumValue());
            out.key("preferred");
            out.numberValue(spring.getPreferredValue());
            out.key("maximum");
            out.numberValue(spring.getMaximumValue());
            out.key("value");
            out.numberValue(spring.getValue());
        }
        out.closeObject();
    }
}
