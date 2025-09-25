package mujica.math.algebra.discrete;

import mujica.ds.of_int.list.AbstractIntList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Created in infrastructure on 2022/4/20, named AddressDistributionResult.
 * Recreated in Ultramarine on 2023/3/24, named BitCount32.
 * Recreated on 2025/3/17.
 */
public class Bit32CombinationList extends AbstractIntList {

    public static int next(int value) {
        int index0 = 0;
        while (index0 < Integer.SIZE) {
            if ((value & (1 << index0)) != 0) {
                break;
            }
            index0++;
        }
        int index1 = index0 + 1;
        while (index1 < Integer.SIZE) {
            if ((value & (1 << index1)) == 0) {
                break;
            }
            index1++;
        }
        if (index1 < Integer.SIZE) {
            value &= -1 << index1;
            value |= 1 << index1;
            value |= (1 << (index1 - index0 - 1)) - 1;
        }
        return value;
    }

    public static int previous(int value) {
        int index0 = 0;
        while (index0 < Integer.SIZE) {
            if ((value & (1 << index0)) == 0) {
                break;
            }
            index0++;
        }
        int index1 = index0 + 1;
        while (index1 < Integer.SIZE) {
            if ((value & (1 << index1)) != 0) {
                break;
            }
            index1++;
        }
        if (index1 < Integer.SIZE) {
            if (index1 == Integer.SIZE - 1) {
                value = 0;
            } else {
                value &= -1 << (index1 + 1);
            }
            value |= (1 << index1) - (1 << (index1 - index0 - 1));
        }
        return value;
    }

    private class Bit32CombinationIterator implements ListIterator<Integer> {

        private int index, value;

        Bit32CombinationIterator(int index) {
            super();
            this.index = index;
            this.value = getInt(index);
        }

        @Override
        public boolean hasNext() {
            return value != (1 << n) - (1 << (n - m));
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            index++;
            assert index <= size;
            value = Bit32CombinationList.next(value);
            return value;
        }

        @Override
        public boolean hasPrevious() {
            return value != (1 << m) - 1;
        }

        @Override
        public Integer previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            index--;
            assert index >= 0;
            value = Bit32CombinationList.previous(value);
            return value;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(Integer i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(Integer i) {
            throw new UnsupportedOperationException();
        }
    }

    private final int n, m, size;

    public Bit32CombinationList(int n, int m) throws ArithmeticException {
        super();
        if (n > Integer.SIZE) {
            throw new ArithmeticException(); // extra check
        }
        this.n = n;
        this.m = m;
        this.size = ClampedMath.INSTANCE.combination(n, m); // parameter n, m checked inside, throws ArithmeticException
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    @NotNull
    @Override
    public Bit32CombinationList duplicate() {
        return new Bit32CombinationList(n, m);
    }

    @Override
    public int intLength() {
        return size; // size will never overflow, the maximum size = C(32, 16) = 601080390 < Integer.MAX_VALUE
    }

    @Override
    public int getInt(int index) {
        if (m == 0) {
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            return 0;
        }
        if (index <= 0) {
            if (index == 0) {
                return (1 << m) - 1;
            }
            throw new IndexOutOfBoundsException();
        }
        int total = m;
        long a = 1L;
        while (true) {
            total++;
            if (total > n) {
                throw new IndexOutOfBoundsException();
            }
            long b = a * total / (total - m); // C(n, m) = C(n - 1, m - 1) * n / m
            if (index < b) {
                index -= a;
                break;
            }
            a = b;
        }
        return (1 << (total - 1)) | getInt(index, m - 1);
    }

    /**
     * No bound check, recursive
     */
    private int getInt(int index, int ones) {
        if (ones == 0) {
            return 0;
        }
        if (index == 0) {
            return (1 << ones) - 1;
        }
        int zeros = 0;
        long a = 1;
        while (true) {
            zeros++;
            long b = a * (zeros + ones) / zeros;
            if (index < b) {
                index -= a;
                break;
            }
            a = b;
        }
        return (1 << (zeros + ones - 1)) | getInt(index, ones - 1);
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new Bit32CombinationIterator(0);
    }

    @NotNull
    @Override
    public ListIterator<Integer> listIterator(int i) {
        return new Bit32CombinationIterator(i);
    }

    @NotNull
    @Override
    public String detailToString() {
        return "Bit32CombinationList[" + n + ", " + m + "]";
    }
}
