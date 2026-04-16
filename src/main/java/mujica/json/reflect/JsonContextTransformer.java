package mujica.json.reflect;

import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "ValueSerializer")
@CodeHistory(date = "2026/4/2", name = "JsonContextSerializer")
@CodeHistory(date = "2026/4/6")
@FunctionalInterface
public interface JsonContextTransformer<T> {

    void transform(T in, @NotNull JsonHandler out, JsonContext context);
}
