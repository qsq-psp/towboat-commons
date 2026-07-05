package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.InputEvent;

/**
 * Created on 2026/6/15.
 */
public class KeyStrokeTransformer implements JsonContextTransformer<KeyStroke> {

    public static final KeyStrokeTransformer INSTANCE = new KeyStrokeTransformer();

    static final FastString ON_KEY_RELEASE = new FastString("onKeyRelease");

    @Override
    public void transform(@NotNull KeyStroke in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey(KeyEventTransformer.KEY_CHAR);
            out.stringValue(String.valueOf(in.getKeyChar()));
            out.stringKey(KeyEventTransformer.KEY_CODE);
            out.numberValue(in.getKeyCode());
        }
        {
            int modifiers = in.getModifiers();
            if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0) {
                out.stringKey(InputEventTransformer.SHIFT);
                out.booleanValue(true);
            }
            if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0) {
                out.stringKey(InputEventTransformer.CONTROL);
                out.booleanValue(true);
            }
            if ((modifiers & InputEvent.META_DOWN_MASK) != 0) {
                out.stringKey(InputEventTransformer.META);
                out.booleanValue(true);
            }
            if ((modifiers & InputEvent.ALT_DOWN_MASK) != 0) {
                out.stringKey(InputEventTransformer.ALT);
                out.booleanValue(true);
            }
            if ((modifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0) {
                out.stringKey(InputEventTransformer.ALT_GRAPH);
                out.booleanValue(true);
            }
        }
        {
            out.stringKey(ON_KEY_RELEASE);
            out.booleanValue(in.isOnKeyRelease());
        }
        out.closeObject();
    }
}
