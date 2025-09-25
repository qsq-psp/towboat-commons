package mujica.math.algebra.discrete;


import mujica.ds.generic.LeftRightIteratorState;
import mujica.ds.of_int.list.IntList;
import mujica.ds.of_int.list.IntListHandle;
import mujica.ds.of_int.list.PrivateIntList;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Created on 2025/3/13.
 */
public class CombinationList extends AbstractList<IntList> {

    private class CombinationIterator implements ListIterator<IntList> {

        @NotNull
        private int[] left = new int[m];

        @NotNull
        private int[] right = new int[m];

        @NotNull
        private final IntListHandle handle = new IntListHandle();

        @NotNull
        private LeftRightIteratorState state = LeftRightIteratorState.LEFT_UNKNOWN;

        private int index; // let it overflow

        CombinationIterator() {
            super();
            for (int i = 0; i < m; i++) {
                right[i] = i;
            }
        }

        /**
         * Right configuration
         */
        CombinationIterator(int index, @NotNull int[] array) {
            super();
            this.index = index;
            System.arraycopy(array, 0, right, 0, m);
        }

        /**
         * Left configuration
         */
        CombinationIterator(@NotNull int[] array, int index) {
            super();
            this.index = index;
            System.arraycopy(array, 0, left, 0, m);
        }

        @Override
        public boolean hasNext() {
            switch (state) {
                case BOTH_READY:
                case LEFT_UNKNOWN:
                case LEFT_FAIL:
                    return true;
                case RIGHT_FAIL:
                    return false;
                case RIGHT_UNKNOWN:
                default:
                    System.arraycopy(left, 0, right, 0, m);
                    if (CombinationList.this.next(right)) {
                        state = LeftRightIteratorState.BOTH_READY;
                        return true;
                    } else {
                        state = LeftRightIteratorState.RIGHT_FAIL;
                        return false;
                    }
            }
        }

        @Override
        public IntList next() {
            switch (state) {
                case BOTH_READY:
                case LEFT_UNKNOWN:
                case LEFT_FAIL: {
                    int[] array = right;
                    right = left;
                    left = array;
                    handle.set(array);
                    state = LeftRightIteratorState.RIGHT_UNKNOWN;
                    index++;
                    return handle.getIntList();
                }
                default:
                case RIGHT_UNKNOWN:
                    if (CombinationList.this.next(left)) {
                        handle.set(left);
                        index++;
                        return handle.getIntList();
                    }
                    // no break here
                case RIGHT_FAIL:
                    throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            switch (state) {
                case BOTH_READY:
                case RIGHT_UNKNOWN:
                case RIGHT_FAIL:
                    return true;
                case LEFT_FAIL:
                    return false;
                case LEFT_UNKNOWN:
                default:
                    System.arraycopy(right, 0, left, 0, m);
                    if (CombinationList.this.previous(left)) {
                        state = LeftRightIteratorState.BOTH_READY;
                        return true;
                    } else {
                        state = LeftRightIteratorState.LEFT_FAIL;
                        return false;
                    }
            }
        }

        @Override
        public IntList previous() {
            switch (state) {
                case BOTH_READY:
                case RIGHT_UNKNOWN:
                case RIGHT_FAIL: {
                    int[] array = left;
                    left = right;
                    right = array;
                    handle.set(array);
                    state = LeftRightIteratorState.LEFT_UNKNOWN;
                    index--;
                    return handle.getIntList();
                }
                default:
                case LEFT_UNKNOWN:
                    if (CombinationList.this.previous(right)) {
                        handle.set(right);
                        index--;
                        return handle.getIntList();
                    }
                    // no break here
                case LEFT_FAIL:
                    throw new NoSuchElementException();
            }
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
        public void set(IntList list) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(IntList list) {
            throw new UnsupportedOperationException();
        }
    }

    private final int n, m, intSizeClamped;

    @NotNull
    private final int[] array;

    @NotNull
    private final IntList item;

    @NotNull
    private final int[] intChoiceCount;

    @NotNull
    private final BigInteger[] bigChoiceCount;

    public CombinationList(int n, int m) throws ArithmeticException {
        super();
        this.n = n;
        this.m = m;
        this.intSizeClamped = ClampedMath.INSTANCE.combination(n, m); // parameter n, m checked inside, throws ArithmeticException
        this.array = new int[m];
        this.item = new PrivateIntList(array);
        final int mnp = n - m + 1;
        this.intChoiceCount = new int[mnp];
        this.bigChoiceCount = new BigInteger[mnp];
    }

    @Override
    public int size() {
        return intSizeClamped;
    }

    @Override
    public IntList get(int index) {
        if (index < 0 || index > intSizeClamped) { // not index >= intSize, in case that intSize == Integer.MAX_VALUE
            throw new IndexOutOfBoundsException();
        }
        if (m == 0) {
            return item;
        }
        int offset = 0;
        int range = n - m;
        MI:
        for (int mi = 1; mi < m; mi++) {
            intChoiceCount[0] = 1;
            for (int ri = 1; ri <= range; ri++) {
                intChoiceCount[ri] = intChoiceCount[ri - 1] * (m - mi + ri) / ri;
            }
            for (int ri = range; ri >= 0; ri--) {
                int choiceCount = intChoiceCount[ri];
                if (index < choiceCount) {
                    int value = range - ri;
                    array[mi - 1] = offset + value;
                    offset = offset + value + 1;
                    range -= value;
                    continue MI;
                } else {
                    index -= choiceCount;
                }
            }
            throw new IndexOutOfBoundsException();
        }
        {
            int value = 0;
            if (m >= 2) {
                value = array[m - 2] + 1;
            }
            value += index;
            if (!(0 <= value && value < n)) {
                throw new IndexOutOfBoundsException();
            }
            array[m - 1] = value;
        }
        return item;
    }

    @NotNull
    public IntList get(@NotNull BigInteger index) {
        if (index.signum() < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (m == 0) {
            return item;
        }
        final int mn = n - m;
        for (int mi = 1; mi < m; mi++) {
            {
                BigInteger childCombination = BigInteger.ONE;
                bigChoiceCount[0] = childCombination;
                for (int mni = 1; mni <= mn; mni++) {
                    childCombination = childCombination
                            .multiply(BigInteger.valueOf(m - mi + mni))
                            .divide(BigInteger.valueOf(mni));
                    bigChoiceCount[mni] = childCombination;
                }
            }
            {
                int mni = mn - 1;
                BigInteger sum0 = BigInteger.ZERO;
                BigInteger sum1 = bigChoiceCount[mni];
                while (true) {
                    if (index.compareTo(sum1) < 0) {
                        index = index.subtract(sum0);
                        array[mi - 1] = mn - 1 - mni;
                        break;
                    }
                    if (mni > 0) {
                        mni--;
                        sum0 = sum1;
                        sum1 = sum1.add(bigChoiceCount[mni]);
                    } else {
                        throw new IndexOutOfBoundsException();
                    }
                }
            }
        }
        {
            int value = 0;
            if (m >= 2) {
                value = array[m - 2] + 1;
            }
            value += index.intValueExact();
            if (!(0 <= value && value < n)) {
                throw new IndexOutOfBoundsException();
            }
            array[m - 1] = value;
        }
        return item;
    }

    @Override
    @NotNull
    public Iterator<IntList> iterator() {
        return new CombinationIterator();
    }

    @Override
    @NotNull
    public ListIterator<IntList> listIterator() {
        return new CombinationIterator();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    @NotNull
    public ListIterator<IntList> listIterator(int index) {
        if (index == intSizeClamped) {
            get(index - 1);
            return new CombinationIterator(array, index);
        } else {
            get(index);
            return new CombinationIterator(index, array);
        }
    }

    private boolean next(@NotNull int[] array) {
        final int mn = n - m;
        for (int mi = m - 1; mi >= 0; mi--) {
            int ni = array[mi];
            assert mi <= ni;
            if (ni < mi + mn) {
                for (; mi < m; mi++) {
                    array[mi] = ++ni;
                }
                return true;
            }
            assert ni == mi + mn;
        }
        return false;
    }

    private boolean previous(@NotNull int[] array) {
        final int mn = n - m;
        for (int mi = m - 1; mi >= 0; mi--) {
            int ni = array[mi];
            assert ni <= mi + mn;
            if (mi < ni) {
                array[mi] = ni - 1;
                for (mi++; mi < m; mi++) {
                    array[mi] = mi + mn;
                }
                return true;
            }
            assert mi == ni;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Combination[" + n + ", " + m + "]";
    }
}
