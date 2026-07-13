package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * Created on 2026/5/18.
 */
@CodeHistory(date = "2026/5/18")
public class ActionEventTransformer implements JsonContextTransformer<ActionEvent> {

    public static final ActionEventTransformer INSTANCE = new ActionEventTransformer();

    static final FastString COMMAND = new FastString("command");

    @Override
    public void transform(@NotNull ActionEvent event, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(InputEventTransformer.ID);
            out.numberValue(event.getID());
            out.key(InputEventTransformer.WHEN);
            out.numberValue(event.getWhen());
        }
        {
            String command = event.getActionCommand();
            if (command != null) {
                out.key(COMMAND);
                out.stringValue(command);
            }
        }
        out.closeObject();
    }
}
