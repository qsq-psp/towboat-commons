package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.event.FocusEvent;

/**
 * Created on 2026/5/23.
 */
@CodeHistory(date = "2026/5/23")
public class FocusEventTransformer implements JsonContextTransformer<FocusEvent> {

    @Override
    public void transform(@NotNull FocusEvent in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(InputEventTransformer.ID);
            out.numberValue(in.getID());
            out.stringKey("temporary");
            out.booleanValue(in.isTemporary());
            out.stringKey("cause");
            out.stringValue(in.getCause().toString());
        }
        out.closeObject();
    }
}
