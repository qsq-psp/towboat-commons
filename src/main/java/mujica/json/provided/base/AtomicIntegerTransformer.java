package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

@CodeHistory(date = "2026/5/2")
public class AtomicIntegerTransformer implements JsonContextTransformer<AtomicInteger> {

    public static final AtomicIntegerTransformer INSTANCE = new AtomicIntegerTransformer();

    @Override
    public void transform(@NotNull AtomicInteger slot, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.numberValue(slot.get());
    }
}
