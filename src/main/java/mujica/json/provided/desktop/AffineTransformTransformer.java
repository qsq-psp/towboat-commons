package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.AffineTransform;

/**
 * Created on 2026/5/15.
 */
public class AffineTransformTransformer implements JsonContextTransformer<AffineTransform> {

    public static final AffineTransformTransformer INSTANCE = new AffineTransformTransformer();

    @Override
    public void transform(@NotNull AffineTransform in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("scaleX");
            out.numberValue(in.getScaleX()); // m00
            out.key("scaleY");
            out.numberValue(in.getScaleY()); // m11
            out.key("shearX");
            out.numberValue(in.getShearX()); // m01
            out.key("shearY");
            out.numberValue(in.getShearY()); // m10
            out.key("translateX");
            out.numberValue(in.getTranslateX()); // m02
            out.key("translateY");
            out.numberValue(in.getTranslateY()); // m12
        }
        out.closeObject();
    }
}
