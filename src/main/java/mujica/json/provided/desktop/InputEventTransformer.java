package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.InputEvent;

@CodeHistory(date = "2026/4/28")
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

    static void transformExposed(@NotNull InputEvent inputEvent, @NotNull JsonHandler out) {
        out.key(ID);
        out.numberValue(inputEvent.getID());
        out.key(CONSUMED);
        out.booleanValue(inputEvent.isConsumed());
        out.key(WHEN);
        out.numberValue(inputEvent.getWhen());
        if (inputEvent.isShiftDown()) {
            out.key(SHIFT);
            out.booleanValue(true);
        }
        if (inputEvent.isControlDown()) {
            out.key(CONTROL);
            out.booleanValue(true);
        }
        if (inputEvent.isMetaDown()) {
            out.key(META);
            out.booleanValue(true);
        }
        if (inputEvent.isAltDown()) {
            out.key(ALT);
            out.booleanValue(true);
        }
        if (inputEvent.isAltGraphDown()) {
            out.key(ALT_GRAPH);
            out.booleanValue(true);
        }
    }

    @Override
    public void transform(@NotNull InputEvent inputEvent, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        transformExposed(inputEvent, out);
        out.closeObject();
    }
}
