package mujica.ds.of_boolean.list;

import mujica.ds.InvariantException;
import mujica.ds.of_int.list.IntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2026/4/8.
 */
@CodeHistory(date = "2026/4/8")
public class MaskedIntListAsBooleanList extends AbstractBooleanList {

    private static final long serialVersionUID = 0xDDC4D159E9199C8DL;

    @NotNull
    IntList intList;

    @NotNull
    public IntList getIntList() {
        return intList;
    }

    public void setIntList(@NotNull IntList intList) {
        this.intList = intList;
    }

    int mask;

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public MaskedIntListAsBooleanList(@NotNull IntList intList, int mask) {
        super();
        this.intList = intList;
        this.mask = mask;
    }

    public MaskedIntListAsBooleanList(@NotNull IntList intList) {
        this(intList, 1);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        intList.checkHealth(consumer);
        if (mask == 0) {
            consumer.accept(new InvariantException("mask = 0"));
        }
    }

    @Override
    public boolean isEmpty() {
        return intList.isEmpty();
    }

    @Override
    public boolean isFull() {
        return intList.isFull();
    }

    @Override
    public int booleanLength() {
        return intList.intLength();
    }

    @Override
    public boolean getBoolean(int i) {
        return (intList.getInt(i) & mask) != 0;
    }

    @Override
    public boolean offerAt(int i, boolean t) {
        return intList.offerAt(i, t ? mask : 0);
    }

    @Override
    public boolean offerFirst(boolean t) {
        return intList.offerFirst(t ? mask : 0);
    }

    @Override
    public boolean offerLast(boolean t) {
        return intList.offerLast(t ? mask : 0);
    }

    @Override
    public boolean removeAt(int i) {
        return (intList.removeAt(i) & mask) != 0;
    }

    @Override
    public boolean removeFirst() {
        return (intList.removeFirst() & mask) != 0;
    }

    @Override
    public boolean removeLast() {
        return (intList.removeLast() & mask) != 0;
    }

    @Override
    public void removeRange(int startIndex, int endIndex) {
        intList.removeRange(startIndex, endIndex);
    }

    @Override
    public boolean setBoolean(int i, boolean t) {
        final int s = intList.getInt(i);
        if (t) {
            intList.setInt(i, s | mask);
        } else {
            intList.setInt(i, s & ~mask);
        }
        return (s & mask) != 0;
    }

    @Override
    public boolean flipBoolean(int i) {
        int s = intList.getInt(i);
        final boolean t = (s & mask) != 0;
        if (t) {
            s &= ~mask;
        } else {
            s |= mask;
        }
        intList.setInt(i, s);
        return t;
    }

    @Override
    public void swap(int i, int j) {
        intList.swap(i, j);
    }

    @Override
    public void clear() {
        intList.clear();
    }

    @Override
    public void reverse() {
        intList.reverse();
    }

    @Override
    public void rotate(int d) {
        intList.rotate(d);
    }
}
