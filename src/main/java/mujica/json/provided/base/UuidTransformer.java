package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@CodeHistory(date = "2026/4/28")
public class UuidTransformer implements JsonContextTransformer<UUID>, JsonStructure {

    public static final UuidTransformer INSTANCE = new UuidTransformer();

    @Override
    public void transform(@NotNull UUID uuid, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.stringValue(uuid.toString());
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(UUID.randomUUID(), jh, null);
    }
}
