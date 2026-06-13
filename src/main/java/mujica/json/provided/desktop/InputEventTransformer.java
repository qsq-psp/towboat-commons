package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.event.InputEvent;

/**
 * Created on 2026/4/28.
 */
public class InputEventTransformer implements JsonContextTransformer<InputEvent> {

    public static final InputEventTransformer INSTANCE = new InputEventTransformer();

    static final FastString ID = new FastString("id");

    static final FastString CONSUMED = new FastString("consumed");

    static final FastString WHEN = new FastString("when");

    static final FastString SHIFT = new FastString("shift");

    static final FastString CONTROL = new FastString("control");

    static final FastString META = new FastString("meta");

    static final FastString ALT = new FastString("alt");

    static final FastString ALT_GRAPH = new FastString("altGraph");

    static void transformExposed(@NotNull InputEvent in, @NotNull JsonHandler out) {
        out.stringKey(ID);
        out.numberValue(in.getID());
        out.stringKey(CONSUMED);
        out.booleanValue(in.isConsumed());
        out.stringKey(WHEN);
        out.numberValue(in.getWhen());
        if (in.isShiftDown()) {
            out.stringKey(SHIFT);
            out.booleanValue(true);
        }
        if (in.isControlDown()) {
            out.stringKey(CONTROL);
            out.booleanValue(true);
        }
        if (in.isMetaDown()) {
            out.stringKey(META);
            out.booleanValue(true);
        }
        if (in.isAltDown()) {
            out.stringKey(ALT);
            out.booleanValue(true);
        }
        if (in.isAltGraphDown()) {
            out.stringKey(ALT_GRAPH);
            out.booleanValue(true);
        }
    }

    @Override
    public void transform(@NotNull InputEvent in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        transformExposed(in, out);
        out.closeObject();
    }
}
