package mujica.ds.i64;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/6")
public abstract class Base2I64Slot extends I64 implements Base2Integer {

    protected Base2I64Slot() {
        super();
    }

    protected Base2I64Slot(long value) {
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
                return Base2I64Slot.this;
            }

            @NotNull
            @Override
            public Rational higherBound() {
                return Base2I64Slot.this;
            }
        };
    }

    @NotNull
    @Override
    public Fraction<?> getFraction() {
        final Base2I64Slot one = clone();
        one.value = 1L;
        return new Fraction<>(this, one);
    }

    @NotNull
    @Override
    public abstract Base2I64Slot clone();

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
