package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created on 2026/5/22.
 */
public class ShapeTransformer implements JsonContextTransformer<Shape> {

    public static final ShapeTransformer INSTANCE = new ShapeTransformer();

    static final FastString BOUND = new FastString("bound");

    @Override
    public void transform(@NotNull Shape in, @NotNull JsonHandler out, JsonContext context) {
        if (in instanceof Rectangle2D) {
            RectangleTransformer.INSTANCE.transform((Rectangle2D) in, out, context);
        } else {
            out.openObject();
            {
                out.stringKey(BOUND);
                RectangleTransformer.INSTANCE.transform(in.getBounds2D(), out, context);
                PathIteratorTransformer.transformExposed(in.getPathIterator(null), out);
            }
            out.closeObject();
        }
    }
}
