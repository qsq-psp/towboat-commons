package mujica.ds.bit;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;

import java.io.Serializable;

@CodeHistory(date = "2018/11/12", project = "TubeM", name = "MtBoolean")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/5/27", name = "PublicBitSlot")
@CodeHistory(date = "2026/7/23")
@FieldOrder("value") // only "value", not "bit"
public class Bit implements BitSlot, Cloneable, Serializable {

    private static final long serialVersionUID = 0xbf54c7e30e0d41adL;

    public boolean value; // public access for convenience

    public Bit() {
        super();
    }

    public Bit(boolean value) {
        super();
        this.value = value;
    }

    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
    @Override
    protected Bit clone() {
        return new Bit(value);
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
        return this.getClass() == obj.getClass() && this.value == ((Bit) obj).value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @CodeHistory(date = "2026/7/21")
    public static class Node1 extends Bit {

        private static final long serialVersionUID = 0x9A038731A41F8689L;

        Node1 first;
    }

    @CodeHistory(date = "2026/7/21")
    public static class Node2 extends Bit {

        private static final long serialVersionUID = 0x33C5297F13DF0E2EL;

        Node2 first, second;
    }

    @CodeHistory(date = "2026/7/21")
    public static class Node3 extends Bit {

        private static final long serialVersionUID = 0xEFA4EC12189ADCA1L;

        Node3 first, second, third;
    }
}
