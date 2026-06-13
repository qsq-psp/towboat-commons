package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.image.Kernel;

/**
 * Created on 2026/5/28.
 */
@CodeHistory(date = "2026/5/28")
public class KernelTransformer implements JsonContextTransformer<Kernel> {

    @Override
    public void transform(@NotNull Kernel in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(RectangleTransformer.WIDTH);
            out.numberValue(in.getWidth());
            out.stringKey(RectangleTransformer.HEIGHT);
            out.numberValue(in.getHeight());
            out.stringKey(RectangleTransformer.X);
            out.numberValue(in.getXOrigin());
            out.stringKey(RectangleTransformer.Y);
            out.numberValue(in.getYOrigin());
        }
        {
            float[] data = in.getKernelData(null);
            out.stringKey("data");
            out.openArray();
            for (float value : data) {
                out.numberValue(value);
            }
            out.closeArray();
        }
        out.closeObject();
    }
}
