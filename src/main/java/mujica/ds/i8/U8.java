package mujica.ds.i8;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.Base2Iterator;
import mujica.ds.slot.ReadOnlyBase2Iterator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/8/26")
public class U8 extends Base2I8Slot implements Comparable<U8> {

    public U8() {
        super();
    }

    public U8(byte value) {
        super(value);
    }

    @NotNull
    @Override
    public U8 clone() {
        return new U8();
    }

    @NotNull
    @Override
    public BitSlot signBit() {
        return null;
    }

    @NotNull
    @Override
    public ReadOnlyBase2Iterator readMagnitude(boolean msbFirst) {
        return null;
    }

    @NotNull
    @Override
    public Base2Iterator magnitude() {
        return null;
    }
    @Override
    public int compareTo(@NotNull U8 that) {
        return Byte.compareUnsigned(this.value, that.value);
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + (0xff & value) + ")";
    }
}
