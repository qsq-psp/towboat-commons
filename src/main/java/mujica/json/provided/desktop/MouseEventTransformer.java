package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created on 2026/5/13.
 */
@CodeHistory(date = "2026/5/13")
public class MouseEventTransformer implements JsonContextTransformer<MouseEvent> {

    static final FastString X = new FastString("x");

    static final FastString Y = new FastString("y");

    static final FastString CLICK_COUNT = new FastString("clickCount");

    static final FastString BUTTON = new FastString("button");

    @Override
    public void transform(@NotNull MouseEvent event, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            InputEventTransformer.transformExposed(event, out);
            out.key(X);
            out.numberValue(event.getX());
            out.key(Y);
            out.numberValue(event.getY());
            out.key(CLICK_COUNT);
            out.numberValue(event.getClickCount());
            out.key(BUTTON);
            out.numberValue(event.getButton());
        }
        if (event instanceof MouseWheelEvent) {
            MouseWheelEvent wheelEvent = (MouseWheelEvent) event;
            {
                int scrollType = wheelEvent.getScrollType();
                out.key("scrollType");
                switch (scrollType) {
                    case MouseWheelEvent.WHEEL_UNIT_SCROLL:
                        out.stringValue("unit");
                        break;
                    case MouseWheelEvent.WHEEL_BLOCK_SCROLL:
                        out.stringValue("block");
                        break;
                    default:
                        out.numberValue(scrollType);
                        break;
                }
            }
            out.key("scrollAmount");
            out.numberValue(wheelEvent.getScrollAmount());
            out.key("wheelRotation");
            out.numberValue(wheelEvent.getWheelRotation());
            out.key("preciseWheelRotation");
            out.numberValue(wheelEvent.getPreciseWheelRotation());
        }
        out.closeObject();
    }
}
