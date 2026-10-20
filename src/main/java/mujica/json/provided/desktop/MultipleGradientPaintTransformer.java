package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2026/9/29")
public class MultipleGradientPaintTransformer implements JsonContextTransformer<MultipleGradientPaint> {

    public static final MultipleGradientPaintTransformer INSTANCE = new MultipleGradientPaintTransformer();

    @Override
    public void transform(MultipleGradientPaint in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(PathIteratorTransformer.TYPE);
            out.stringValue(in.getClass().getSimpleName());
            out.key("fractions");
            out.arrayValue(in.getFractions());
            out.key("colors");
            out.openArray();
            for (Color color : in.getColors()) {
                ColorTransformer.INSTANCE.transform(color, out, context);
            }
            out.closeArray();
            out.key("cycleMethod");
            out.stringValue(in.getCycleMethod().name());
            out.key("colorSpace");
            out.stringValue(in.getColorSpace().name());
            out.key(FontMetricsTransformer.TRANSFORM);
            AffineTransformTransformer.INSTANCE.transform(in.getTransform(), out, context);
        }
        {
            int transparency = in.getTransparency();
            out.key("transparency");
            switch (transparency) {
                case Paint.OPAQUE:
                    out.stringValue("opaque");
                    break;
                case Paint.BITMASK:
                    out.stringValue("bitmask");
                    break;
                case Paint.TRANSLUCENT:
                    out.stringValue("translucent");
                    break;
                default:
                    out.numberValue(transparency);
                    break;
            }
        }
        out.closeObject();
    }
}
