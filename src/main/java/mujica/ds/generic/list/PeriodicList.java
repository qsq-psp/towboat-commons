package mujica.ds.generic.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.AbstractList;

@CodeHistory(date = "2025/5/28", project = "Ultramarine", name = "Repeat")
@CodeHistory(date = "2025/12/20")
public abstract class PeriodicList<E> extends AbstractList<E> implements Serializable {

    private static final long serialVersionUID = 0x38B47C5A2714302CL;

    protected int size;

    @Override
    public int size() {
        return size;
    }

    @NotNull
    public PeriodicList<E> size(int newSize) {
        size = newSize;
        return this;
    }

    public int times() {
        return size / period();
    }

    public PeriodicList<E> times(int newTimes) {
        return size(newTimes * period());
    }

    public abstract int period();
}
