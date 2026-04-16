package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
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
    protected void set(Object self, @Nullable Object value) throws Throwable {
        field.set(self, value);
    }

    protected void setBoolean(Object self, boolean value) throws Throwable {
        field.setBoolean(self, value);
    }

    protected void setByte(Object self, byte value) throws Throwable {
        field.setByte(self, value);
    }

    protected void setChar(Object self, char value) throws Throwable {
        field.setChar(self, value);
    }

    protected void setShort(Object self, short value) throws Throwable {
        field.setShort(self, value);
    }

    protected void setInt(Object self, int value) throws Throwable {
        field.setInt(self, value);
    }

    protected void setLong(Object self, long value) throws Throwable {
        field.setLong(self, value);
    }

    protected void setFloat(Object self, float value) throws Throwable {
        field.setFloat(self, value);
    }

    protected void setDouble(Object self, double value) throws Throwable {
        field.setDouble(self, value);
    }

    @NotNull
    @Override
    protected Setter unreflect(@NotNull MethodHandles.Lookup lookup) throws Throwable {
        return new MethodHandleSetter(lookup.unreflectSetter(field));
    }
}
