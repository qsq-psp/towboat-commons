package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

@CodeHistory(date = "2022/6/19", project = "Ultramarine", name = "FieldReflectGetter")
@CodeHistory(date = "2025/11/15")
class ClassicalFieldGetter extends Getter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassicalFieldGetter.class);

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
            LOGGER.warn("{}", field, e);
            return super.invoke(self);
        }
    }

    @NotNull
    @Override
    protected Getter tryUnreflect(@NotNull MethodHandles.Lookup lookup) {
        try {
            return new MethodHandleGetter(lookup.unreflectGetter(field));
        } catch (Exception e) {
            LOGGER.warn("{}", field, e);
            return this;
        }
    }
}
