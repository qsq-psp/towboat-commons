package mujica.json.provided;

import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.reflect.ReflectConfig;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@CodeHistory(date = "2026/4/6")
public class IterableTransformer implements JsonContextTransformer<Iterable<?>> {

    public static final IterableTransformer INSTANCE = new IterableTransformer();

    @Override
    public void transform(Iterable<?> in, @NotNull JsonHandler out, JsonContext context) {
        if (context.addContainerObject(in)) {
            out.openArray();
            for (Iterator<?> iterator = in.iterator(); iterator.hasNext(); ) {
                context.transform(in, out);
            }
            out.closeArray();
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.UNDEFINED_SHIFT)) {
            context.getLogger().debug("loop to undefined");
            out.skippedValue();
        } else if (context.any(((long) JsonEmpty.FROM_LOOP_OBJECT) << ReflectConfig.NULL_SHIFT)) {
            context.getLogger().debug("loop to null");
            out.nullValue();
        } else {
            throw new RuntimeException("loop");
        }
    }
}
