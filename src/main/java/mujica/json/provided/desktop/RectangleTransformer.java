package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created on 2026/5/2.
 */
public class RectangleTransformer implements JsonContextTransformer<Rectangle2D> {

    public static final RectangleTransformer INSTANCE = new RectangleTransformer();

    static final FastString X = new FastString("x");

    static final FastString Y = new FastString("y");

    static final FastString WIDTH = new FastString("width");

    static final FastString HEIGHT = new FastString("height");

    @Override
    public void transform(@NotNull Rectangle2D in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        if (in instanceof Rectangle) {
            Rectangle r = (Rectangle) in;
            out.stringKey(X);
            out.numberValue(r.x);
            out.stringKey(Y);
            out.numberValue(r.y);
            out.stringKey(WIDTH);
            out.numberValue(r.width);
            out.stringKey(HEIGHT);
            out.numberValue(r.height);
        } else if (in instanceof Rectangle2D.Float) {
            Rectangle2D.Float rf = (Rectangle2D.Float) in;
            out.stringKey(X);
            out.numberValue(rf.x);
            out.stringKey(Y);
            out.numberValue(rf.y);
            out.stringKey(WIDTH);
            out.numberValue(rf.width);
            out.stringKey(HEIGHT);
            out.numberValue(rf.height);
        } else {
            out.stringKey(X);
            out.numberValue(in.getX());
            out.stringKey(Y);
            out.numberValue(in.getY());
            out.stringKey(WIDTH);
            out.numberValue(in.getWidth());
            out.stringKey(HEIGHT);
            out.numberValue(in.getHeight());
        }
        out.closeObject();
    }
}
