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

    static void transformProperty(@NotNull String key, @NotNull Action in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        final Object value = in.getValue(key);
        if (value == null) {
            return;
        }
        if (value instanceof Boolean) {
            out.stringKey(key);
            out.booleanValue((Boolean) value);
        } else if (value instanceof Integer) {
            out.stringKey(key);
            out.numberValue((Integer) value);
        } else if (value instanceof String) {
            out.stringKey(key);
            out.stringValue((String) value);
        } else if (value instanceof KeyStroke) {
            out.stringKey(key);
            KeyStrokeTransformer.INSTANCE.transform((KeyStroke) value, out, context);
        }
    }

    @Override
    public void transform(@NotNull Action in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            transformProperty(Action.NAME, in, out, context);
            transformProperty(Action.SHORT_DESCRIPTION, in, out, context);
            transformProperty(Action.LONG_DESCRIPTION, in, out, context);
            transformProperty(Action.ACTION_COMMAND_KEY, in, out, context);
            transformProperty(Action.ACCELERATOR_KEY, in, out, context);
            transformProperty(Action.MNEMONIC_KEY, in, out, context);
            transformProperty(Action.SELECTED_KEY, in, out, context);
            transformProperty(Action.DISPLAYED_MNEMONIC_INDEX_KEY, in, out, context);
            out.stringKey("enabled");
            out.booleanValue(in.isEnabled());
        }
        out.closeObject();
    }
}
