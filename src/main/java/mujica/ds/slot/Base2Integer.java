package mujica.ds.slot;

import mujica.ds.bit.BitSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/5")
public interface Base2Integer extends Rational {

    @NotNull
    @Override
    RealIterator realIterator();

    @NotNull
    @Override
    Fraction<?> getFraction();

    @NotNull
    BitSlot signBit();

    @NotNull
    ReadOnlyBase2Iterator readMagnitude(boolean msbFirst); // the default order is LSB first

    @NotNull
    Base2Iterator magnitude(); // the only order is LSB first
}
