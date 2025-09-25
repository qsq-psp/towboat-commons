package mujica.math.algebra.discrete;

import mujica.ds.generic.LeftRightIteratorState;
import mujica.ds.of_int.list.IntList;
import mujica.ds.of_int.list.IntListHandle;
import mujica.ds.of_int.list.PrivateIntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

@CodeHistory(date = "2018/7/5", project = "existence", name = "Arrangement")
@CodeHistory(date = "2025/3/13")
public class ArrangementList extends AbstractList<IntList> {

    private class ArrangementIterator implements ListIterator<IntList> {

        @NotNull
        private int[] left = new int[m];

        @NotNull
        private int[] right = new int[m];

        @NotNull
        private final IntListHandle handle = new IntListHandle();

        @NotNull
        private LeftRightIteratorState state = LeftRightIteratorState.LEFT_UNKNOWN;

        private int index; // let it overflow

        ArrangementIterator() {
            super();
            for (int i = 0; i < m; i++) {
                right[i] = i;
            }
        }

        /**
         * Right configuration
         */
        ArrangementIterator(int index, @NotNull int[] array) {
            super();
            this.index = index;
            System.arraycopy(array, 0, right, 0, m);
        }

        /**
         * Left configuration
         */
        ArrangementIterator(@NotNull int[] array, int index) {
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
                    if (ArrangementList.this.next(right)) {
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
                    if (ArrangementList.this.next(left)) {
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
                    if (ArrangementList.this.previous(left)) {
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
                    if (ArrangementList.this.previous(right)) {
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
    private final boolean[] used;

    @NotNull
    private final int[] array;

    @NotNull
    private final IntList item;

    public ArrangementList(int n, int m) throws ArithmeticException {
        super();
        this.n = n;
        this.m = m;
        this.intSizeClamped = ClampedMath.INSTANCE.arrangement(n, m); // parameter n, m checked inside, throws ArithmeticException
        this.used = new boolean[n];
        this.array = new int[m];
        this.item = new PrivateIntList(array);
    }

    @Override
    public int size() {
        return intSizeClamped;
    }

    @NotNull
    @Override
    public IntList get(int index) {
        getChoiceArray(index);
        choiceToItem();
        return item;
    }

    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    public IntList getChoiceArray(int index) {
        if (index < 0 || index > intSizeClamped) { // not index >= intSize, in case that intSize == Integer.MAX_VALUE
            throw new IndexOutOfBoundsException();
        }
        int nmi = n - m + 1;
        for (int mi = m - 1; mi >= 0; mi--) {
            array[mi] = index % nmi;
            index /= nmi;
            nmi++;
        }
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return item;
    }

    @NotNull
    public IntList get(@NotNull BigInteger index) {
        getChoiceArray(index);
        choiceToItem();
        return item;
    }

    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    public IntList getChoiceArray(@NotNull BigInteger index) {
        if (index.signum() < 0) {
            throw new IndexOutOfBoundsException();
        }
        int mi = m;
        if (mi <= 0) {
            return item;
        }
        BigInteger nmi = BigInteger.valueOf(n - m + 1);
        while (true) {
            BigInteger[] result = index.divideAndRemainder(nmi);
            array[--mi] = result[1].intValueExact();
            index = result[0];
            if (mi <= 0) {
                break;
            }
            nmi = nmi.add(BigInteger.ONE);
        }
        if (index.signum() != 0) {
            throw new IndexOutOfBoundsException();
        }
        return item;
    }

    @Override
    public int lastIndexOf(Object obj) {
        return indexOf(obj); // there is no identical items in the list, so the first index is the last index
    }

    /**
     * @return true if malformed input
     */
    private boolean itemToChoice() {
        Arrays.fill(used, false);
        return false;
    }

    private void choiceToItem() throws IndexOutOfBoundsException {
        Arrays.fill(used, false);
        MIL:
        for (int mi = 0; mi < m; mi++) {
            int count = array[mi];
            for (int ni = 0; ni < n; ni++) {
                if (used[ni]) {
                    continue;
                }
                if (count-- <= 0) {
                    used[ni] = true;
                    array[mi] = ni;
                    continue MIL;
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public Iterator<IntList> iterator() {
        return new ArrangementIterator();
    }

    @Override
    @NotNull
    public ListIterator<IntList> listIterator() {
        return new ArrangementIterator();
    }

    @Override
    @NotNull
    public ListIterator<IntList> listIterator(int index) {
        if (index == intSizeClamped) {
            getChoiceArray(index - 1);
            choiceToItem();
            return new ArrangementIterator(array, index);
        } else {
            getChoiceArray(index);
            choiceToItem();
            return new ArrangementIterator(index, array);
        }
    }

    private boolean next(@NotNull int[] array) {
        Arrays.fill(used, false);
        for (int value : array) {
            used[value] = true;
        }
        for (int mi = m - 1; mi >= 0; mi--) {
            int ni = array[mi];
            used[ni] = false;
            for (ni++; ni < n; ni++) {
                if (used[ni]) {
                    continue;
                }
                array[mi] = ni;
                used[ni] = true;
                ni = -1;
                for (mi++; mi < m; mi++) {
                    for (ni++; ni < n; ni++) {
                        if (used[ni]) {
                            continue;
                        }
                        array[mi] = ni;
                        // used[ni] = true;
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean previous(@NotNull int[] array) {
        Arrays.fill(used, false);
        for (int value : array) {
            used[value] = true;
        }
        for (int mi = m - 1; mi >= 0; mi--) {
            int ni = array[mi];
            used[ni] = false;
            for (ni--; ni >= 0; ni--) {
                if (used[ni]) {
                    continue;
                }
                array[mi] = ni;
                used[ni] = true;
                ni = n;
                for (mi++; mi < m; mi++) {
                    for (ni--; ni >= 0; ni--) {
                        if (used[ni]) {
                            continue;
                        }
                        array[mi] = ni;
                        // used[ni] = true;
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Arrangement[" + n + ", " + m + "]";
    }
}
