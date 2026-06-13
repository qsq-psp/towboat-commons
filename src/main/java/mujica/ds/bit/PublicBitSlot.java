package mujica.ds.bit;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;

import java.io.Serializable;

@CodeHistory(date = "2018/11/12", project = "TubeM", name = "MtBoolean")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/5/27")
@FieldOrder("value") // only "value", not "boolean"
public class PublicBitSlot implements BitSlot, Cloneable, Serializable {

    private static final long serialVersionUID = 0xbf54c7e30e0d41adL;

    public boolean value;

    public PublicBitSlot() {
        super();
    }

    public PublicBitSlot(boolean value) {
        super();
        this.value = value;
    }

    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
    @Override
    protected PublicBitSlot clone() {
        return new PublicBitSlot(value);
    }

    @Override
    public boolean getBit() {
        return value;
    }

    @Override
    public void setBit(boolean newValue) {
        value = newValue;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((PublicBitSlot) obj).value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
