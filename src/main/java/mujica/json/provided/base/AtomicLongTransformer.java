package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created on 2026/5/6.
 */
public class AtomicLongTransformer implements JsonContextTransformer<AtomicLong> {

    public static final AtomicLongTransformer INSTANCE = new AtomicLongTransformer();

    @Override
    public void transform(@NotNull AtomicLong in, @NotNull JsonHandler out, JsonContext context) {
        out.numberValue(in.get());
    }
}
