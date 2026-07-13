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
    public void transform(@NotNull Shape shape, @NotNull JsonHandler out, JsonContext context) {
        if (shape instanceof Rectangle2D) {
            RectangleTransformer.INSTANCE.transform((Rectangle2D) shape, out, context);
        } else {
            out.openObject();
            {
                out.key(BOUND);
                RectangleTransformer.INSTANCE.transform(shape.getBounds2D(), out, context);
                PathIteratorTransformer.transformExposed(shape.getPathIterator(null), out);
            }
            out.closeObject();
        }
    }
}
