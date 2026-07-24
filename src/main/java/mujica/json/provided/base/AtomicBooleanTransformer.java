package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

@CodeHistory(date = "2026/5/3")
public class AtomicBooleanTransformer implements JsonContextTransformer<AtomicBoolean> {

    public static final AtomicBooleanTransformer INSTANCE = new AtomicBooleanTransformer();

    @Override
    public void transform(@NotNull AtomicBoolean slot, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.booleanValue(slot.get());
    }
}
