package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;

@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/11/14")
class Getter {

    static final Getter NOP = new Getter();

    Getter() {
        super();
    }

    protected Object get(Object self) throws Throwable {
        return null;
    }
    
    protected boolean getBoolean(Object self) throws Throwable {
        return (Boolean) get(self);
    }

    protected byte getByte(Object self) throws Throwable {
        return (Byte) get(self);
    }

    protected char getChar(Object self) throws Throwable {
        return (Character) get(self);
    }

    protected short getShort(Object self) throws Throwable {
        return (Short) get(self);
    }
    
    protected int getInt(Object self) throws Throwable {
        return (Integer) get(self);
    }
    
    protected long getLong(Object self) throws Throwable {
        return (Long) get(self);
    }
    
    protected float getFloat(Object self) throws Throwable {
        return (Float) get(self);
    }
    
    protected double getDouble(Object self) throws Throwable {
        return (Double) get(self);
    }

    @NotNull
    protected Getter unreflect(@NotNull MethodHandles.Lookup lookup) throws Throwable {
        return this;
    }
}
