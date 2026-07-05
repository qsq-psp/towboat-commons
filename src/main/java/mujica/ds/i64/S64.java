package mujica.ds.i64;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.Base2Iterator;
import mujica.ds.slot.ReadOnlyBase2Iterator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/8")
public class S64 extends Base2I64Slot implements Comparable<S64> {

    private static final long serialVersionUID = 0xCEC8B436EDE6D694L;

    public S64() {
        super();
    }

    public S64(long value) {
        super(value);
    }

    @NotNull
    @Override
    public S64 clone() {
        return new S64(value);
    }

    @NotNull
    @Override
    public BitSlot signBit() {
        return new BitSlot() {

            @Override
            public boolean getBit() {
                return value < 0L;
            }

            @Override
            public void setBit(boolean newValue) {
                if (value < 0L) {
                    if (!newValue) {
                        if (value == Long.MIN_VALUE) {
                            throw new ArithmeticException();
                        }
                        value = -value;
                    }
                } else {
                    if (newValue) {
                        value = -value;
                    }
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

    @Override
    public int compareTo(@NotNull S64 that) {
        return Long.compare(this.value, that.value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((S64) obj).value;
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + value + ")";
    }
}
