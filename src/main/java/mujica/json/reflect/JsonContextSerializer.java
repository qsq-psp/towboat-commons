package mujica.json.reflect;

import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/2.
 */
@CodeHistory(date = "2026/4/2")
@FunctionalInterface
public interface JsonContextSerializer {

    void serialize(Object in, @NotNull JsonHandler out, @NotNull JsonContext context);
}
