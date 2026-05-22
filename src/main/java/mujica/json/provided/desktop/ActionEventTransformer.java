package mujica.json.provided.desktop;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * Created on 2026/5/18.
 */
public class ActionEventTransformer implements JsonContextTransformer<ActionEvent> {

    public static final ActionEventTransformer INSTANCE = new ActionEventTransformer();

    static final FastString COMMAND = new FastString("command");

    @Override
    public void transform(@NotNull ActionEvent in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(InputEventTransformer.ID);
            out.numberValue(in.getID());
            out.stringKey(InputEventTransformer.WHEN);
            out.numberValue(in.getWhen());
        }
        {
            String command = in.getActionCommand();
            if (command != null) {
                out.stringKey(COMMAND);
                out.stringValue(command);
            }
        }
        out.closeObject();
    }
}
