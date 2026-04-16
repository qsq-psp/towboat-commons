package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/8/12", project = "Ultramarine", name = "JsonEnumType")
@CodeHistory(date = "2026/4/11")
class JavaEnumType extends JsonType {

    private static final long serialVersionUID = 0x43e916a99d6cce1dL;

    protected transient Object value;

    JavaEnumType(long flags) {
        super(flags);
    }

    @NotNull
    @Override
    JsonType collectType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        collectConfig(clazz, false);
        return this;
    }
}
