package mujica.json.reflect;

import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/9.
 */
@CodeHistory(date = "2026/4/9")
@DirectSubclass({ClassicalMethodTransformer.class, MethodHandleTransformer.class})
class ReflectedTransformer implements JsonContextTransformer<Object> {

    ReflectedTransformer() {
        super();
    }

    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        out.skippedValue();
    }
}
