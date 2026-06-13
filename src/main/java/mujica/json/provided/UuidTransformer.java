package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created on 2026/4/28.
 */
public class UuidTransformer implements JsonContextTransformer<UUID>, JsonStructure {

    public static final UuidTransformer INSTANCE = new UuidTransformer();

    @Override
    public void transform(UUID in, @NotNull JsonHandler out, JsonContext context) {
        out.stringValue(in.toString());
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(UUID.randomUUID(), jh, null);
    }
}
