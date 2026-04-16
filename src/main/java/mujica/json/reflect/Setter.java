package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;

@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/11/14")
class Setter {

    static final Setter NOP = new Setter();

    Setter() {
        super();
    }

    protected void set(Object self, @Nullable Object value) throws Throwable {
        // pass
    }

    protected void setBoolean(Object self, boolean value) throws Throwable {
        set(self, value);
    }

    protected void setByte(Object self, byte value) throws Throwable {
        set(self, value);
    }

    protected void setChar(Object self, char value) throws Throwable {
        set(self, value);
    }

    protected void setShort(Object self, short value) throws Throwable {
        set(self, value);
    }

    protected void setInt(Object self, int value) throws Throwable {
        set(self, value);
    }

    protected void setLong(Object self, long value) throws Throwable {
        set(self, value);
    }

    protected void setFloat(Object self, float value) throws Throwable {
        set(self, value);
    }

    protected void setDouble(Object self, double value) throws Throwable {
        set(self, value);
    }

    @NotNull
    protected Setter unreflect(@NotNull MethodHandles.Lookup lookup) throws Throwable {
        return this;
    }

    @NotNull
    ReflectedTransformer bind(@NotNull JsonContextTransformer<?> instance) {
        throw new UnsupportedOperationException();
    }
}
