package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicIntegerArray;

@CodeHistory(date = "2026/5/13")
public class AtomicIntegerArrayTransformer implements JsonContextTransformer<AtomicIntegerArray> {

    public static final AtomicIntegerArrayTransformer INSTANCE = new AtomicIntegerArrayTransformer();

    @Override
    public void transform(@NotNull AtomicIntegerArray array, @NotNull JsonHandler out, @Nullable JsonContext context) {
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
