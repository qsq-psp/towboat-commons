package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicLong;

@CodeHistory(date = "2026/5/6")
public class AtomicLongTransformer implements JsonContextTransformer<AtomicLong> {

    public static final AtomicLongTransformer INSTANCE = new AtomicLongTransformer();

    @Override
    public void transform(@NotNull AtomicLong slot, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.numberValue(slot.get());
    }
}
