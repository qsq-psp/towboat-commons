package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2026/10/14.
 */
@CodeHistory(date = "2026/10/14")
public class DesktopTransformer implements JsonContextTransformer<Desktop>, JsonStructure {

    @Override
    public void transform(Desktop in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        for (Desktop.Action action : Desktop.Action.values()) {
            out.key(action.name());
            out.booleanValue(in.isSupported(action));
        }
        out.closeObject();
    }
    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Desktop.getDesktop(), jh, null);
    }
}
