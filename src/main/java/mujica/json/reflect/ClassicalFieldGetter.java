package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@CodeHistory(date = "2022/6/19", project = "Ultramarine", name = "FieldReflectGetter")
@CodeHistory(date = "2025/11/15")
class ClassicalFieldGetter extends Getter {

    @NotNull
    final Field field;

    ClassicalFieldGetter(@NotNull Field field) {
        super();
        this.field = field;
    }

    @Override
    protected Object invoke(@Nullable Object self) {
        try {
            return field.get(self);
        } catch (ReflectiveOperationException e) {
            // logger
            return super.invoke(self);
        }
    }
}
