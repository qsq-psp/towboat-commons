package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@CodeHistory(date = "2026/6/15")
public class ActionTransformer implements JsonContextTransformer<Action> {

    public static final ActionTransformer INSTANCE = new ActionTransformer();

    static void transformProperty(@NotNull String key, @NotNull Action in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        final Object value = in.getValue(key);
        if (value == null) {
            return;
        }
        if (value instanceof Boolean) {
            out.key(key);
            out.booleanValue((Boolean) value);
        } else if (value instanceof Integer) {
            out.key(key);
            out.numberValue((Integer) value);
        } else if (value instanceof String) {
            out.key(key);
            out.stringValue((String) value);
        } else if (value instanceof KeyStroke) {
            out.key(key);
            KeyStrokeTransformer.INSTANCE.transform((KeyStroke) value, out, context);
        }
    }

    @Override
    public void transform(@NotNull Action action, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            transformProperty(Action.NAME, action, out, context);
            transformProperty(Action.SHORT_DESCRIPTION, action, out, context);
            transformProperty(Action.LONG_DESCRIPTION, action, out, context);
            transformProperty(Action.ACTION_COMMAND_KEY, action, out, context);
            transformProperty(Action.ACCELERATOR_KEY, action, out, context);
            transformProperty(Action.MNEMONIC_KEY, action, out, context);
            transformProperty(Action.SELECTED_KEY, action, out, context);
            transformProperty(Action.DISPLAYED_MNEMONIC_INDEX_KEY, action, out, context);
            out.key("enabled");
            out.booleanValue(action.isEnabled());
        }
        out.closeObject();
    }
}
