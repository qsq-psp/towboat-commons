package mujica.json.provided.desktop;

import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.image.ColorModel;

@CodeHistory(date = "2022/9/1", project = "Ultramarine", name = "ColorModelValueSerializer")
@CodeHistory(date = "2026/4/25")
public class ColorModelTransformer implements JsonContextTransformer<ColorModel>, JsonStructure {

    public static final ColorModelTransformer INSTANCE = new ColorModelTransformer();

    @Override
    public void transform(ColorModel in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            //
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(ColorModel.getRGBdefault(), jh, null);
    }
}
