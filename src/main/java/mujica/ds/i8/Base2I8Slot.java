package mujica.ds.i8;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/5")
public abstract class Base2I8Slot extends I8 implements Base2Integer {

    private static final long serialVersionUID = 0xD0FA74259AB54D38L;

    protected Base2I8Slot() {
        super();
    }

    protected Base2I8Slot(byte value) {
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
                return Base2I8Slot.this;
            }

            @NotNull
            @Override
            public Rational higherBound() {
                return Base2I8Slot.this;
            }
        };
    }

    @NotNull
    @Override
    public Fraction<?> getFraction() {
        final Base2I8Slot one = clone();
        one.value = 1;
        return new Fraction<>(this, one);
    }

    @NotNull
    @Override
    public abstract Base2I8Slot clone();

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
