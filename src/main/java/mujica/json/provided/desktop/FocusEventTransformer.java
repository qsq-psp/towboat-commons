package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.event.FocusEvent;

@CodeHistory(date = "2026/5/23")
public class FocusEventTransformer implements JsonContextTransformer<FocusEvent> {

    @Override
    public void transform(@NotNull FocusEvent in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(InputEventTransformer.ID);
            out.numberValue(in.getID());
            out.key("temporary");
            out.booleanValue(in.isTemporary());
            out.key("cause");
            out.stringValue(in.getCause().toString());
        }
        out.closeObject();
    }
}
