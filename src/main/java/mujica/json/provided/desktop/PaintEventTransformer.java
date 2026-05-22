package mujica.json.provided.desktop;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.PaintEvent;

/**
 * Created on 2026/5/17.
 */
public class PaintEventTransformer implements JsonContextTransformer<PaintEvent> {

    public static final PaintEventTransformer INSTANCE = new PaintEventTransformer();

    @Override
    public void transform(@NotNull PaintEvent in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(InputEventTransformer.ID);
            out.numberValue(in.getID());
        }
        {
            Rectangle rectangle = in.getUpdateRect();
            if (rectangle != null) {
                out.stringKey("rectangle");
                RectangleTransformer.INSTANCE.transform(rectangle, out, context);
            }
        }
        out.closeObject();
    }
}
