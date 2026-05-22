package mujica.json.provided.desktop;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created on 2026/5/5.
 */
public class GradientPaintTransformer implements JsonContextTransformer<GradientPaint> {

    public static final GradientPaintTransformer INSTANCE = new GradientPaintTransformer();

    @Override
    public void transform(@NotNull GradientPaint in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            Point2D.Float p = (Point2D.Float) in.getPoint1();
            out.stringKey("x1");
            out.numberValue(p.x);
            out.stringKey("y1");
            out.numberValue(p.y);
            // color1
            p = (Point2D.Float) in.getPoint2();
            out.stringKey("x2");
            out.numberValue(p.x);
            out.stringKey("y2");
            out.numberValue(p.y);
            // color2
            out.stringKey("cyclic");
            out.booleanValue(in.isCyclic());
        }
        out.closeObject();
    }
}
