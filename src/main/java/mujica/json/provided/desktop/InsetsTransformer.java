package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Created on 2026/6/2.
 */
public class InsetsTransformer implements JsonContextTransformer<Insets> {

    public static final InsetsTransformer INSTANCE = new InsetsTransformer();

    static final FastString TOP = new FastString("top");

    static final FastString LEFT = new FastString("left");

    static final FastString BOTTOM = new FastString("bottom");

    static final FastString RIGHT = new FastString("right");

    static final FastString UI_RESOURCE = new FastString("uiResource");

    @Override
    public void transform(@NotNull Insets in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey(TOP);
            out.numberValue(in.top);
            out.stringKey(LEFT);
            out.numberValue(in.left);
            out.stringKey(BOTTOM);
            out.numberValue(in.bottom);
            out.stringKey(RIGHT);
            out.numberValue(in.right);
        }
        if (in instanceof UIResource) {
            out.stringKey(UI_RESOURCE);
            out.booleanValue(true);
        }
        out.closeObject();
    }
}
