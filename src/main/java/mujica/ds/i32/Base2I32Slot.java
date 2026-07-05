package mujica.ds.i32;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/5")
public abstract class Base2I32Slot extends I32 implements Base2Integer {

    private static final long serialVersionUID = 0x3F184AD890F64A21L;

    protected Base2I32Slot() {
        super();
    }

    protected Base2I32Slot(int value) {
        super(value);
    }

    @NotNull
    @Override
    public RealIterator realIterator() {
        return new RealIterator() {

            @Override
            public boolean next() {
                return false;
            }

            @NotNull
            @Override
            public Rational lowerBound() {
                return Base2I32Slot.this;
            }

            @NotNull
            @Override
            public Rational higherBound() {
                return Base2I32Slot.this;
            }
        };
    }

    @NotNull
    @Override
    public Fraction<?> getFraction() {
        final Base2I32Slot one = clone();
        one.value = 1;
        return new Fraction<>(this, one);
    }

    @NotNull
    @Override
    public abstract Base2I32Slot clone();

    @NotNull
    @Override
    public abstract BitSlot signBit();

    @NotNull
    @Override
    public abstract ReadOnlyBase2Iterator readMagnitude(boolean msbFirst);

    @NotNull
    @Override
    public abstract Base2Iterator magnitude();
}
