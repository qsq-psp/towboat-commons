package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
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
    protected Object get(Object self) throws Throwable {
        return field.get(self);
    }

    protected boolean getBoolean(Object self) throws Throwable {
        return field.getBoolean(self);
    }

    protected byte getByte(Object self) throws Throwable {
        return field.getByte(self);
    }

    protected char getChar(Object self) throws Throwable {
        return field.getChar(self);
    }

    protected short getShort(Object self) throws Throwable {
        return field.getShort(self);
    }

    protected int getInt(Object self) throws Throwable {
        return field.getInt(self);
    }

    protected long getLong(Object self) throws Throwable {
        return field.getLong(self);
    }

    protected float getFloat(Object self) throws Throwable {
        return field.getFloat(self);
    }

    protected double getDouble(Object self) throws Throwable {
        return field.getDouble(self);
    }

    @NotNull
    @Override
    protected Getter unreflect(@NotNull MethodHandles.Lookup lookup) throws Throwable {
        return new MethodHandleGetter(lookup.unreflectGetter(field));
    }
}
