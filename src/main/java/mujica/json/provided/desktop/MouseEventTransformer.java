package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;

/**
 * Created on 2026/5/13.
 */
@CodeHistory(date = "2026/5/13")
public class MouseEventTransformer implements JsonContextTransformer<MouseEvent> {

    static final FastString X = new FastString("x");

    static final FastString Y = new FastString("x");

    static final FastString CLICK_COUNT = new FastString("clickCount");

    static final FastString BUTTON = new FastString("button");

    @Override
    public void transform(@NotNull MouseEvent in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            InputEventTransformer.transformExposed(in, out);
            out.stringKey(X);
            out.numberValue(in.getX());
            out.stringKey(Y);
            out.numberValue(in.getY());
            out.stringKey(CLICK_COUNT);
            out.numberValue(in.getClickCount());
            out.stringKey(BUTTON);
            out.numberValue(in.getButton());
        }
        out.closeObject();
    }
}
