package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.InputEvent;

@CodeHistory(date = "2026/6/15")
public class KeyStrokeTransformer implements JsonContextTransformer<KeyStroke> {

    public static final KeyStrokeTransformer INSTANCE = new KeyStrokeTransformer();

    static final FastString ON_KEY_RELEASE = new FastString("onKeyRelease");

    @Override
    public void transform(@NotNull KeyStroke keyStroke, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(KeyEventTransformer.KEY_CHAR);
            out.stringValue(String.valueOf(keyStroke.getKeyChar()));
            out.key(KeyEventTransformer.KEY_CODE);
            out.numberValue(keyStroke.getKeyCode());
        }
        {
            int modifiers = keyStroke.getModifiers();
            if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0) {
                out.key(InputEventTransformer.SHIFT);
                out.booleanValue(true);
            }
            if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0) {
                out.key(InputEventTransformer.CONTROL);
                out.booleanValue(true);
            }
            if ((modifiers & InputEvent.META_DOWN_MASK) != 0) {
                out.key(InputEventTransformer.META);
                out.booleanValue(true);
            }
            if ((modifiers & InputEvent.ALT_DOWN_MASK) != 0) {
                out.key(InputEventTransformer.ALT);
                out.booleanValue(true);
            }
            if ((modifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0) {
                out.key(InputEventTransformer.ALT_GRAPH);
                out.booleanValue(true);
            }
        }
        {
            out.key(ON_KEY_RELEASE);
            out.booleanValue(keyStroke.isOnKeyRelease());
        }
        out.closeObject();
    }
}
