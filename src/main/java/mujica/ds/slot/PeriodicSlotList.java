package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/8/22")
public abstract class PeriodicSlotList<S, A> extends VersionSlotList<S, A> {

    int size;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int newSize) {
        size = newSize;
    }
}
