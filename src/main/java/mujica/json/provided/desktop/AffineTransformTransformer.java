package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.AffineTransform;

@CodeHistory(date = "2026/5/15")
public class AffineTransformTransformer implements JsonContextTransformer<AffineTransform> {

    public static final AffineTransformTransformer INSTANCE = new AffineTransformTransformer();

    @Override
    public void transform(@NotNull AffineTransform affineTransform, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("scaleX");
            out.numberValue(affineTransform.getScaleX()); // m00
            out.key("scaleY");
            out.numberValue(affineTransform.getScaleY()); // m11
            out.key("shearX");
            out.numberValue(affineTransform.getShearX()); // m01
            out.key("shearY");
            out.numberValue(affineTransform.getShearY()); // m10
            out.key("translateX");
            out.numberValue(affineTransform.getTranslateX()); // m02
            out.key("translateY");
            out.numberValue(affineTransform.getTranslateY()); // m12
        }
        out.closeObject();
    }
}
