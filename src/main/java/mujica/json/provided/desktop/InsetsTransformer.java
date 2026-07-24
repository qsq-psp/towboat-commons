package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.UIResource;
import java.awt.*;

@CodeHistory(date = "2026/6/2")
public class InsetsTransformer implements JsonContextTransformer<Insets> {

    public static final InsetsTransformer INSTANCE = new InsetsTransformer();

    static final FastString TOP = new FastString("top");

    static final FastString LEFT = new FastString("left");

    static final FastString BOTTOM = new FastString("bottom");

    static final FastString RIGHT = new FastString("right");

    static final FastString UI_RESOURCE = new FastString("uiResource");

    @Override
    public void transform(@NotNull Insets insets, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(TOP);
            out.numberValue(insets.top);
            out.key(LEFT);
            out.numberValue(insets.left);
            out.key(BOTTOM);
            out.numberValue(insets.bottom);
            out.key(RIGHT);
            out.numberValue(insets.right);
        }
        if (insets instanceof UIResource) {
            out.key(UI_RESOURCE);
            out.booleanValue(true);
        }
        out.closeObject();
    }
}
