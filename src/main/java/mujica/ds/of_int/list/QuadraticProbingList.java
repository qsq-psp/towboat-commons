package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.PrimitiveIterator;

@CodeHistory(date = "2025/6/30")
public class QuadraticProbingList extends AbstractIntList {

    private static final long serialVersionUID = 0x441a2613c30e8e18L;

    private static final int MAX_MODULO = 46340; // MAX_MODULO * (MAX_MODULO + 1) <= Integer.MAX_VALUE

    private int modulo;

    private int base;

    private class ReusableIterator implements PrimitiveIterator.OfInt {

        private int index;

        @NotNull
        ReusableIterator reuse() {
            index = 0;
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < modulo;
        }

        @Override
        public int nextInt() {
            return getInt(index++);
        }
    }

    private transient ReusableIterator reusableIterator;

    public QuadraticProbingList(int modulo) {
        super();
        setModulo(modulo);
        renewIterator();
    }

    private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        renewIterator();
    }

    private void renewIterator() {
        reusableIterator = new ReusableIterator();
    }

    @NotNull
    @Override
    public QuadraticProbingList clone() {
        try {
            QuadraticProbingList that = (QuadraticProbingList) super.clone();
            that.renewIterator();
            return that;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @NotNull
    @Override
    public QuadraticProbingList duplicate() {
        return (new QuadraticProbingList(modulo)).setBase(base);
    }

    public void setModulo(int modulo) {
        assert 0 < modulo && modulo <= MAX_MODULO;
        this.modulo = modulo;
    }

    @NotNull
    public QuadraticProbingList setBase(int base) {
        this.base = (base & Integer.MAX_VALUE) % modulo;
        return this;
    }

    @Override
    public int intLength() {
        return modulo;
    }

    @Override
    public int getInt(int i) {
        if ((i & 1) == 0) {
            i >>= 1; // [0, 1, 2, 3, 4, ...] -> [0, _, 1, _, 2, ...]
            i = (base - i * i) % modulo;
            if (i < 0) {
                i += modulo;
            }
            return i;
        } else {
            i = (i + 1) >> 1; // [0, 1, 2, 3, 4, ...] -> [_, 1, _, 2, _, ...]
            return (base + i * i) % modulo;
        }
    }

    @NotNull
    @Override
    public PrimitiveIterator.OfInt iterator() {
        return reusableIterator.reuse();
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<modulo = " + modulo + ", hash = " + base + ">";
    }
}
