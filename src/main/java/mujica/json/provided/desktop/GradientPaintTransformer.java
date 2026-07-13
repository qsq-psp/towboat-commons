package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created on 2026/5/5.
 */
@CodeHistory(date = "2026/5/5")
public class GradientPaintTransformer implements JsonContextTransformer<GradientPaint> {

    public static final GradientPaintTransformer INSTANCE = new GradientPaintTransformer();

    @Override
    public void transform(@NotNull GradientPaint in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            Point2D.Float p = (Point2D.Float) in.getPoint1();
            out.key("x1");
            out.numberValue(p.x);
            out.key("y1");
            out.numberValue(p.y);
            out.key("color1");
            ColorTransformer.INSTANCE.transform(in.getColor1(), out, context);
            p = (Point2D.Float) in.getPoint2();
            out.key("x2");
            out.numberValue(p.x);
            out.key("y2");
            out.numberValue(p.y);
            out.key("color2");
            ColorTransformer.INSTANCE.transform(in.getColor2(), out, context);
            out.key("cyclic");
            out.booleanValue(in.isCyclic());
        }
        out.closeObject();
    }
}
