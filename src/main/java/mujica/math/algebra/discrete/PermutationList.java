package mujica.math.algebra.discrete;

import mujica.ds.generic.LeftRightIteratorState;
import mujica.ds.of_int.list.IntList;
import mujica.ds.of_int.list.IntListHandle;
import mujica.ds.of_int.list.PrivateIntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.*;

@CodeHistory(date = "2018/7/5", project = "existence", name = "FullArrangement")
@CodeHistory(date = "2025/3/12")
public class PermutationList extends AbstractList<IntList> {

    private class PermutationIterator implements ListIterator<IntList> {

        @NotNull
        private int[] left = new int[n];

        @NotNull
        private int[] right = new int[n];

        @NotNull
        private final IntListHandle handle = new IntListHandle();

        @NotNull
        private LeftRightIteratorState state = LeftRightIteratorState.LEFT_UNKNOWN;

        private int index; // let it overflow

        PermutationIterator() {
            super();
            for (int i = 0; i < n; i++) {
                right[i] = i;
            }
        }

        /**
         * Right configuration
         */
        PermutationIterator(int index, @NotNull int[] array) {
            super();
            this.index = index;
            System.arraycopy(array, 0, right, 0, n);
        }

        /**
         * Left configuration
         */
        PermutationIterator(@NotNull int[] array, int index) {
            super();
            this.index = index;
            System.arraycopy(array, 0, left, 0, n);
        }

        private boolean leftToRight() {
            for (int splitIndex = n - 1; splitIndex > 0; splitIndex--) {
                int splitLeft = left[splitIndex - 1];
                int splitRight = left[splitIndex];
                if (splitLeft >= splitRight) {
                    continue;
                }
                int minIndex = splitIndex;
                for (int index = splitIndex + 1; index < n; index++) {
                    int value = left[index];
                    if (splitLeft < value && value < splitRight) {
                        minIndex = index;
                        splitRight = value;
                    }
                }
                System.arraycopy(left, 0, right, 0, n);
                right[splitIndex - 1] = splitRight;
                right[minIndex] = splitLeft;
                Arrays.sort(right, splitIndex, n);
                return true;
            }
            return false;
        }

        private boolean rightToLeft() {
            for (int splitIndex = n - 1; splitIndex > 0; splitIndex--) {
                int splitLeft = right[splitIndex - 1];
                int splitRight = right[splitIndex];
                if (splitLeft <= splitRight) {
                    continue;
                }
                int maxIndex = splitIndex;
                for (int index = splitIndex + 1; index < n; index++) {
                    int value = right[index];
                    if (splitRight < value && value < splitLeft) {
                        maxIndex = index;
                        splitRight = value;
                    }
                }
                System.arraycopy(right, 0, left, 0, n);
                left[splitIndex - 1] = splitRight;
                left[maxIndex] = splitLeft;
                Arrays.sort(left, splitIndex, n); // sort and then reverse
                int highIndex = n - 1;
                while (splitIndex < highIndex) {
                    int temp = left[splitIndex];
                    left[splitIndex] = left[highIndex];
                    left[highIndex] = temp;
                    splitIndex++;
                    highIndex--;
                }
                return true;
            }
            return false;
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
                    if (leftToRight()) {
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
                default:
                case RIGHT_UNKNOWN:
                    if (!leftToRight()) {
                        throw new NoSuchElementException();
                    }
                    // no break here
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
                    if (rightToLeft()) {
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
                default:
                case LEFT_UNKNOWN:
                    if (!rightToLeft()) {
                        throw new NoSuchElementException();
                    }
                    // no break here
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

    private final int n, intSizeClamped;

    @NotNull
    private final boolean[] used;

    @NotNull
    private final int[] array;

    @NotNull
    private final IntList item;

    public PermutationList(int n) throws ArithmeticException {
        super();
        this.n = n;
        this.intSizeClamped = ClampedMath.INSTANCE.factorial(n); // parameter n checked inside, throws ArithmeticException
        this.used = new boolean[n];
        this.array = new int[n];
        this.item = new PrivateIntList(array);
    }

    @Override
    public int size() {
        return intSizeClamped;
    }

    @Override
    @NotNull
    public IntList get(int index) {
        getChoiceArray(index);
        choiceToItem();
        return item;
    }

    @SuppressWarnings("UnusedReturnValue")
    @NotNull
    public IntList getChoiceArray(int index) {
        if (index > intSizeClamped) {
            throw new IndexOutOfBoundsException();
        }
        int arrayIndex = n;
        if (arrayIndex > 0) {
            array[--arrayIndex] = 0;
        } else {
            return item;
        }
        if (arrayIndex > 0) {
            array[--arrayIndex] = index % 2;
        } else {
            return item;
        }
        final int factorialLimit = Math.min(n, 13);
        int factorialValue = 1;
        for (int factorialIndex = 2; factorialIndex < factorialLimit;) {
            factorialValue *= factorialIndex;
            factorialIndex++;
            int quotient = index / factorialValue;
            if (quotient == 0) {
                break;
            }
            array[--arrayIndex] = quotient % factorialIndex;
        }
        while (arrayIndex > 0) {
            array[--arrayIndex] = 0;
        }
        return item;
    }

    @NotNull
    public IntList get(@NotNull BigInteger index) {
        getChoiceArray(index);
        choiceToItem();
        return item;
    }

    @SuppressWarnings("UnusedReturnValue")
    @NotNull
    public IntList getChoiceArray(@NotNull BigInteger index) {
        int arrayIndex = n;
        if (arrayIndex > 0) {
            array[--arrayIndex] = 0;
        } else {
            return item;
        }
        BigInteger factorialValue = BigInteger.ONE;
        for (int factorialIndex = 1; factorialIndex < n;) {
            factorialValue = factorialValue.multiply(BigInteger.valueOf(factorialIndex));
            factorialIndex++;
            BigInteger quotient = index.divide(factorialValue);
            if (quotient.signum() == 0) {
                break;
            }
            array[--arrayIndex] = quotient.mod(BigInteger.valueOf(factorialIndex)).intValue();
        }
        while (arrayIndex > 0) {
            array[--arrayIndex] = 0;
        }
        return item;
    }

    @Override
    public int indexOf(Object obj) {
        if (!(obj instanceof IntList)) {
            return -1;
        }
        final IntList item = (IntList) obj;
        if (item.intLength() != n) {
            return -1;
        }
        item.getAll(array, 0);
        if (itemToChoice()) {
            return -1;
        }
        return choiceToIndex();
    }

    @Override
    public int lastIndexOf(Object obj) {
        return indexOf(obj); // there is no identical items in the list, so the first index is the last index
    }

    @Nullable
    public BigInteger bigIndexOf(Object obj) {
        if (!(obj instanceof IntList)) {
            return null;
        }
        final IntList item = (IntList) obj;
        if (item.intLength() != n) {
            return null;
        }
        item.getAll(array, 0);
        if (itemToChoice()) {
            return null;
        }
        return choiceToBigIndex();
    }

    @NotNull
    public BigInteger inverse(@NotNull BigInteger index) {
        getChoiceArray(index);
        choiceToItem();
        inverse();
        if (itemToChoice()) {
            throw new RuntimeException("Internal error");
        }
        return choiceToBigIndex();
    }

    /**
     * @return true if malformed input
     */
    private boolean itemToChoice() {
        Arrays.fill(used, false);
        for (int i = 0; i < n; i++) {
            int unusedIndex = 0;
            int j = array[i];
            if (j < 0 || j >= n || used[j]) {
                return true;
            }
            used[j] = true;
            for (j--; j >= 0; j--) {
                if (used[j]) {
                    continue;
                }
                unusedIndex++;
            }
            array[i] = unusedIndex;
        }
        return false;
    }

    private void choiceToItem() {
        Arrays.fill(used, false);
        for (int index = 0; index < n; index++) {
            array[index] = use(array[index]);
        }
    }

    private int use(int unusedIndex) {
        for (int index = 0; index < n; index++) {
            if (used[index]) {
                continue;
            }
            if (unusedIndex-- <= 0) {
                used[index] = true;
                return index;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    private int choiceToIndex() {
        final ClampedMath math = ClampedMath.INSTANCE;
        int sum = 0;
        int factorialValue = 1;
        int factorialIndex = 1;
        for (int arrayIndex = n - 2; arrayIndex >= 0; arrayIndex--) {
            factorialValue = math.multiply(factorialValue, factorialIndex);
            factorialIndex++;
            sum = math.add(sum, math.multiply(factorialValue, array[arrayIndex]));
            if (sum == Integer.MAX_VALUE) {
                break;
            }
        }
        return sum;
    }

    @NotNull
    private BigInteger choiceToBigIndex() {
        BigInteger sum = BigInteger.ZERO;
        BigInteger factorialValue = BigInteger.ONE;
        BigInteger factorialIndex = BigInteger.ONE;
        for (int arrayIndex = n - 2; arrayIndex >= 0; arrayIndex--) {
            factorialValue = factorialValue.multiply(factorialIndex);
            factorialIndex = factorialIndex.add(BigInteger.ONE);
            sum = sum.add(factorialValue.multiply(BigInteger.valueOf(array[arrayIndex])));
        }
        return sum;
    }

    private void inverse() {
        Arrays.fill(used, false);
        for (int i0 = 0; i0 < n; i0++) {
            if (used[i0]) {
                continue;
            }
            used[i0] = true;
            int i1 = i0;
            int i2 = array[i1];
            while (!used[i2]) {
                used[i2] = true;
                int i3 = array[i2];
                array[i2] = i1;
                i1 = i2;
                i2 = i3;
            }
            assert i0 == i2;
            array[i2] = i1;
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public Iterator<IntList> iterator() {
        return new PermutationIterator();
    }

    @NotNull
    @Override
    public ListIterator<IntList> listIterator() {
        return new PermutationIterator();
    }

    @Override
    @NotNull
    public ListIterator<IntList> listIterator(int index) {
        if (index == intSizeClamped) {
            getChoiceArray(index - 1);
            choiceToItem();
            return new PermutationIterator(array, index);
        } else {
            getChoiceArray(index);
            choiceToItem();
            return new PermutationIterator(index, array);
        }
    }

    @Override
    public String toString() {
        return "Permutation[" + n + "]";
    }
}
