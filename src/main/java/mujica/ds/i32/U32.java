package mujica.ds.i32;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.Base2Iterator;
import mujica.ds.slot.ReadOnlyBase2Iterator;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * Created on 2026/7/1.
 */
public class U32 extends Base2I32Slot {

    private static final long serialVersionUID = 0xF919C433CBD61776L;

    public U32() {
        super();
    }

    public U32(@DataType("u32") int value) {
        super(value);
    }

    @NotNull
    @Override
    public U32 clone() {
        return new U32(value);
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
        if (msbFirst) {
            return new ReadOnlyBase2Iterator() {

                int index = Integer.SIZE;

                @Override
                public boolean next() {
                    return index-- > 0;
                }

                @Override
                public boolean getBit() {
                    if (index < 0  || index >= Integer.SIZE) {
                        throw new NoSuchElementException();
                    }
                    return (value & (1 << index)) != 0;
                }
            };
        } else {
            return new ReadOnlyBase2Iterator() {

                int index = -1;

                @Override
                public boolean next() {
                    return index++ < Integer.SIZE;
                }

                @Override
                public boolean getBit() {
                    if (index < 0  || index >= Integer.SIZE) {
                        throw new NoSuchElementException();
                    }
                    return (value & (1 << index)) != 0;
                }
            };
        }
    }

    @NotNull
    @Override
    public Base2Iterator magnitude() {
        return new Base2Iterator() {

            int index = -1;

            @Override
            public boolean next() {
                return index++ < Integer.SIZE;
            }

            @Override
            public boolean getBit() {
                if (index < 0  || index >= Integer.SIZE) {
                    throw new NoSuchElementException();
                }
                return (value & (1 << index)) != 0;
            }

            @Override
            public void setBit(boolean newValue) {
                if (index < 0  || index >= Integer.SIZE) {
                    throw new NoSuchElementException();
                }
                if (newValue) {
                    value |= 1 << index;
                } else {
                    value &= ~(1 << index);
                }
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((U32) obj).value;
    }

    @Override
    @NotNull
    public String toString() {
        return "(" + (0xffffffffL & value) + ")";
    }
}
