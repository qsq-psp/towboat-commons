package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created on 2026/5/3.
 */
@CodeHistory(date = "2026/5/3")
public class AtomicBooleanTransformer implements JsonContextTransformer<AtomicBoolean> {

    public static final AtomicBooleanTransformer INSTANCE = new AtomicBooleanTransformer();

    @Override
    public void transform(@NotNull AtomicBoolean in, @NotNull JsonHandler out, JsonContext context) {
        out.booleanValue(in.get());
    }
}
