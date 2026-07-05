package mujica.ds.i64;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.Base2Iterator;
import mujica.ds.slot.ReadOnlyBase2Iterator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/18")
public class U64 extends Base2I64Slot {

    private static final long serialVersionUID = 0x382C04E5DC974B5AL;

    public U64() {
        super();
    }

    public U64(@DataType("u64") long value) {
        super(value);
    }

    @NotNull
    @Override
    public U64 clone() {
        return new U64(value);
    }

    @NotNull
    @Override
    public BitSlot signBit() {
        return new BitSlot() {

            @Override
            public boolean getBit() {
                return false;
            }

            @Override
            public void setBit(boolean newValue) {
                if (newValue && value != 0) {
                    throw new ArithmeticException();
                }
            }
        };
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
}
