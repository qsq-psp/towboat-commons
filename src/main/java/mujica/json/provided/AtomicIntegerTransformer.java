package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2026/5/2.
 */
@CodeHistory(date = "2026/5/2")
public class AtomicIntegerTransformer implements JsonContextTransformer<AtomicInteger> {

    public static final AtomicIntegerTransformer INSTANCE = new AtomicIntegerTransformer();

    @Override
    public void transform(@NotNull AtomicInteger in, @NotNull JsonHandler out, JsonContext context) {
        out.numberValue(in.get());
    }
}
