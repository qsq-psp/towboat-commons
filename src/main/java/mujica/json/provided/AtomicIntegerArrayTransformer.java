package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created on 2026/5/13.
 */
public class AtomicIntegerArrayTransformer implements JsonContextTransformer<AtomicIntegerArray> {

    public static final AtomicIntegerArrayTransformer INSTANCE = new AtomicIntegerArrayTransformer();

    @Override
    public void transform(@NotNull AtomicIntegerArray in, @NotNull JsonHandler out, JsonContext context) {
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
