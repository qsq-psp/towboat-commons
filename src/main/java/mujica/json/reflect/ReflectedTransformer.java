package mujica.json.reflect;

import mujica.json.entity.JsonHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/9.
 */
class ReflectedTransformer implements JsonContextTransformer<Object> {

    ReflectedTransformer() {
        super();
    }

    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        out.skippedValue();
    }
}
