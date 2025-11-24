package mujica.json.reflect;

import mujica.json.entity.JsonConstant;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@CodeHistory(date = "2022/6/19", project = "Ultramarine", name = "FieldReflectGetter")
@CodeHistory(date = "2025/11/18")
class ClassicalFieldSetter extends Setter {

    @NotNull
    final Field field;

    ClassicalFieldSetter(@NotNull Field field) {
        super();
        this.field = field;
    }

    @Override
    protected void invoke(@Nullable Object self, @Nullable Object value) {
        if (value == JsonConstant.UNDEFINED) {
            return;
        }
        try {
            field.set(self, value);
        } catch (ReflectiveOperationException e) {
            // logger
        }
    }
}
