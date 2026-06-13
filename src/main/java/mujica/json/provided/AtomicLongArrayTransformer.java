package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Created on 2026/5/16.
 */
public class AtomicLongArrayTransformer implements JsonContextTransformer<AtomicLongArray> {

    public static final AtomicLongArrayTransformer INSTANCE = new AtomicLongArrayTransformer();

    @Override
    public void transform(@NotNull AtomicLongArray in, @NotNull JsonHandler out, JsonContext context) {
        final int length = in.length();
        if (length == 0) {
            out.emptyArrayValue();
            return;
        }
        out.openArray();
        for (int index = 0; index < length; index++) {
            out.numberValue(in.get(index));
        }
        out.closeArray();
    }
}
