package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.PaintEvent;

@CodeHistory(date = "2026/5/17")
public class PaintEventTransformer implements JsonContextTransformer<PaintEvent> {

    public static final PaintEventTransformer INSTANCE = new PaintEventTransformer();

    @Override
    public void transform(@NotNull PaintEvent event, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(InputEventTransformer.ID);
            out.numberValue(event.getID());
        }
        {
            Rectangle rectangle = event.getUpdateRect();
            if (rectangle != null) {
                out.key("rectangle");
                RectangleTransformer.INSTANCE.transform(rectangle, out, context);
            }
        }
        out.closeObject();
    }
}
