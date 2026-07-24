package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.Kernel;

@CodeHistory(date = "2026/5/28")
public class KernelTransformer implements JsonContextTransformer<Kernel> {

    @Override
    public void transform(@NotNull Kernel kernel, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(RectangleTransformer.WIDTH);
            out.numberValue(kernel.getWidth());
            out.key(RectangleTransformer.HEIGHT);
            out.numberValue(kernel.getHeight());
            out.key(RectangleTransformer.X);
            out.numberValue(kernel.getXOrigin());
            out.key(RectangleTransformer.Y);
            out.numberValue(kernel.getYOrigin());
        }
        {
            float[] data = kernel.getKernelData(null);
            out.key("data");
            out.openArray();
            for (float value : data) {
                out.numberValue(value);
            }
            out.closeArray();
        }
        out.closeObject();
    }
}
