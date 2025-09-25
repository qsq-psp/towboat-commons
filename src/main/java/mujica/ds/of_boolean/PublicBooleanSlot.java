package mujica.ds.of_boolean;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/5/27")
public class PublicBooleanSlot implements BooleanSlot, Cloneable, Serializable {

    private static final long serialVersionUID = 0xbf54c7e30e0d41adL;

    public boolean value;

    public PublicBooleanSlot() {
        super();
    }

    public PublicBooleanSlot(boolean value) {
        super();
        this.value = value;
    }

    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
    @Override
    protected PublicBooleanSlot clone() {
        return new PublicBooleanSlot(value);
    }

    @Override
    public boolean getBoolean() {
        return value;
    }

    @Override
    public boolean setBoolean(boolean newValue) {
        final boolean oldValue = value;
        value = newValue;
        return oldValue;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((PublicBooleanSlot) obj).value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
