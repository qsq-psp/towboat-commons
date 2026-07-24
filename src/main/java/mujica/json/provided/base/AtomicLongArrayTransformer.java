package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicLongArray;

@CodeHistory(date = "2026/5/16")
public class AtomicLongArrayTransformer implements JsonContextTransformer<AtomicLongArray> {

    public static final AtomicLongArrayTransformer INSTANCE = new AtomicLongArrayTransformer();

    @Override
    public void transform(@NotNull AtomicLongArray array, @NotNull JsonHandler out, @Nullable JsonContext context) {
        final int length = array.length();
        if (length == 0) {
            out.emptyArrayValue();
            return;
        }
        out.openArray();
        for (int index = 0; index < length; index++) {
            out.numberValue(array.get(index));
        }
        out.closeArray();
    }
}
