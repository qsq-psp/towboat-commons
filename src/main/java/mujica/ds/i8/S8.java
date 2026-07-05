package mujica.ds.i8;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.Base2Iterator;
import mujica.ds.slot.ReadOnlyBase2Iterator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

@CodeHistory(date = "2026/7/5")
public class S8 extends Base2I8Slot implements Comparable<S8> {

    private static final long serialVersionUID = 0x91C68966F8780F38L;

    public S8() {
        super();
    }

    public S8(byte value) {
        super(value);
    }

    @NotNull
    @Override
    public S8 clone() {
        return new S8(value);
    }

    @NotNull
    @Override
    public BitSlot signBit() {
        return new BitSlot() {

            @Override
            public boolean getBit() {
                return value < 0;
            }

            @Override
            public void setBit(boolean newValue) {
                if (value < 0) {
                    if (!newValue) {
                        if (value == Byte.MIN_VALUE) {
                            throw new ArithmeticException();
                        }
                        value = (byte) -value;
                    }
                } else {
                    if (newValue) {
                        value = (byte) -value;
                    }
                }
            }
        };
    }

    @NotNull
    @Override
    public ReadOnlyBase2Iterator readMagnitude(boolean msbFirst) {
        if (msbFirst) {
            return new ReadOnlyBase2Iterator() {

                int index = (value == Byte.MIN_VALUE) ? Byte.SIZE : Byte.SIZE - 1;

                @Override
                public boolean next() {
                    return index-- > 0;
                }

                @Override
                public boolean getBit() {
                    if (index < 0  || index >= Byte.SIZE || index == Byte.SIZE - 1 && value != Byte.MIN_VALUE) {
                        throw new NoSuchElementException();
                    }
                    int magnitude = value;
                    if (magnitude < 0) {
                        magnitude = -magnitude;
                    }
                    return (magnitude & (1 << index)) != 0;
                }
            };
        } else {
            return new ReadOnlyBase2Iterator() {

                int index = -1;

                @Override
                public boolean next() {
                    if (value == Byte.MIN_VALUE) {
                        return index++ < Byte.SIZE;
                    } else {
                        return index++ < Byte.SIZE - 1;
                    }
                }

                @Override
                public boolean getBit() {
                    if (index < 0  || index >= Byte.SIZE || index == Byte.SIZE - 1 && value != Byte.MIN_VALUE) {
                        throw new NoSuchElementException();
                    }
                    int magnitude = value;
                    if (magnitude < 0) {
                        magnitude = -magnitude;
                    }
                    return (magnitude & (1 << index)) != 0;
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
                if (value == 0 || value == Byte.MIN_VALUE) {
                    return index++ < Byte.SIZE;
                } else {
                    return index++ < Byte.SIZE - 1;
                }
            }

            @Override
            public boolean getBit() {
                if (index < 0  || index >= Byte.SIZE || index == Byte.SIZE - 1 && value != 0 && value != Byte.MIN_VALUE) {
                    throw new NoSuchElementException();
                }
                int magnitude = value;
                if (magnitude < 0) {
                    magnitude = -magnitude;
                }
                return (magnitude & (1 << index)) != 0;
            }

            @Override
            public void setBit(boolean newValue) {
                if (index < 0  || index >= Byte.SIZE || index == Byte.SIZE - 1 && value != 0 && value != Byte.MIN_VALUE) {
                    throw new NoSuchElementException();
                }
                int magnitude = value;
                if (value < 0) {
                    magnitude = -magnitude;
                }
                if (newValue) {
                    magnitude |= 1 << index;
                } else {
                    magnitude &= ~(1 << index);
                }
                if (value < 0) {
                    magnitude = -magnitude;
                }
                value = (byte) magnitude;
            }
        };
    }

    @Override
    public int compareTo(@NotNull S8 that) {
        return Byte.compare(this.value, that.value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((S8) obj).value;
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + value + ")";
    }
}
