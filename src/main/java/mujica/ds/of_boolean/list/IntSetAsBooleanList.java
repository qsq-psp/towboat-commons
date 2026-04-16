package mujica.ds.of_boolean.list;

import mujica.ds.of_int.set.IntSet;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/3/16")
public class IntSetAsBooleanList extends AbstractBooleanList {

    private static final long serialVersionUID = 0x6112F206434BB066L;

    @NotNull
    IntSet intSet;

    @NotNull
    public IntSet getIntSet() {
        return intSet;
    }

    public void setIntSet(@NotNull IntSet intSet) {
        this.intSet = intSet;
    }

    public IntSetAsBooleanList(@NotNull IntSet intSet) {
        super();
        this.intSet = intSet;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public int booleanLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean getBoolean(int i) {
        return intSet.contains(i);
    }

    @Override
    public boolean setBoolean(int i, boolean t) {
        if (t) {
            return !intSet.add(i);
        } else {
            return intSet.remove(i);
        }
    }

    @Override
    public boolean flipBoolean(int i) {
        if (intSet.contains(i)) {
            intSet.remove(i);
            return true;
        } else {
            intSet.add(i);
            return false;
        }
    }

    @Override
    public void clear() {
        intSet.clear();
    }

    @Override
    public void reverse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rotate(int d) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(boolean t) {
        throw new UnsupportedOperationException();
    }
}
