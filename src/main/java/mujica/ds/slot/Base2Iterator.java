package mujica.ds.slot;

import mujica.ds.bit.BitSlot;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/6/30")
public interface Base2Iterator extends ReadOnlyBase2Iterator, BitSlot {

    @Override
    boolean next();

    @Override
    boolean getBit();

    @Override
    void setBit(boolean newValue);
}
