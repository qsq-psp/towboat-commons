package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.modifier.JsonEmpty;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.json.reflect.ReflectConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Map;

/**
 * Created on 2026/6/23.
 */
@CodeHistory(date = "2026/6/23")
public class UIDefaultsTransformer implements JsonContextTransformer<UIDefaults>, JsonStructure {

    public static final UIDefaultsTransformer INSTANCE = new UIDefaultsTransformer();

    @Override
    public void transform(UIDefaults map, @NotNull JsonHandler out, JsonContext context) {
        if (context == null) {
            context = (new JsonContext()).loadProvidedDesktop();
        }
        if (context.addContainerObject(map)) {
            try {
                out.openObject();
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    if (!(entry.getKey() instanceof String)) {
                        context.getLogger().info("ui defaults key {}", entry);
                        continue;
                    }
                    out.key(entry.getKey().toString());
                    context.transform(entry.getValue(), out);
                }
                out.closeObject();
            } finally {
                context.removeContainerObject(map);
            }
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.UNDEFINED_SHIFT)) {
            context.getLogger().debug("ui defaults loop to undefined");
            out.skippedValue();
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.NULL_SHIFT)) {
            context.getLogger().debug("ui defaults loop to null");
            out.nullValue();
        } else {
            throw new RuntimeException("loop");
        }
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(UIManager.getDefaults(), jh, null);
    }
}
