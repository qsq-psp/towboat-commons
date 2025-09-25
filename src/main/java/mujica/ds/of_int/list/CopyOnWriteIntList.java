package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/3/24", name = "CopyOnWriteIntArray")
@CodeHistory(date = "2025/6/1")
public class CopyOnWriteIntList extends AbstractIntList {

    @NotNull
    private transient volatile int[] array;

    protected CopyOnWriteIntList(@NotNull int[] array) {
        super();
        this.array = array;
    }

    public CopyOnWriteIntList() {
        this(PublicIntList.EMPTY.array);
    }

    @NotNull
    @Override
    public CopyOnWriteIntList duplicate() {
        return new CopyOnWriteIntList(array); // no array copy
    }

    @Override
    public int intLength() {
        return array.length;
    }

    @NotNull
    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(array, 0);
    }

    @NotNull
    @Override
    public int[] toArray() {
        return array.clone();
    }

    @Override
    public void getRange(int srcOffset, @NotNull int[] dst, int dstOffset, int length) {
        System.arraycopy(array, srcOffset, dst, dstOffset, length); // index check included
    }

    @Override
    public int getInt(int i) {
        return array[i];
    }

    @Override
    public boolean offerAt(int i, int t) {
        final int oldLength = array.length;
        final int[] newArray = new int[oldLength + 1];
        System.arraycopy(array, 0, newArray, 0, i);
        newArray[i] = t;
        System.arraycopy(array, i, newArray, i + 1, oldLength - i);
        array = newArray;
        return true;
    }

    @Override
    public boolean offerFirst(int t) {
        final int oldLength = array.length;
        final int[] newArray = new int[oldLength + 1];
        newArray[0] = t;
        System.arraycopy(array, 0, newArray, 1, oldLength);
        array = newArray;
        return true;
    }

    @Override
    public boolean offerLast(int t) {
        final int oldLength = array.length;
        final int[] newArray = Arrays.copyOf(array, oldLength + 1);
        newArray[oldLength] = t;
        array = newArray;
        return true;
    }

    @Override
    public int removeAt(int i) {
        final int oldLength = array.length;
        final int removed = array[i]; // index check included
        final int[] newArray = new int[oldLength - 1];
        System.arraycopy(array, 0, newArray, 0, i);
        System.arraycopy(array, i + 1, newArray, i, oldLength - i - 1);
        array = newArray;
        return removed;
    }

    @Override
    public int removeFirst() {
        final int oldLength = array.length;
        if (oldLength == 0) {
            throw new NoSuchElementException();
        }
        final int[] newArray = new int[oldLength - 1];
        final int removed = array[0];
        System.arraycopy(array, 1, newArray, 0, oldLength - 1);
        array = newArray;
        return removed;
    }

    @SuppressWarnings("NonAtomicOperationOnVolatileField")
    @Override
    public int removeLast() {
        final int oldLength = array.length;
        if (oldLength == 0) {
            throw new NoSuchElementException();
        }
        final int removed = array[oldLength - 1];
        array = Arrays.copyOf(array, oldLength - 1);
        return removed;
    }

    @Override
    public void removeRange(int fi, int ti) {
        final int oldLength = array.length;
        if (!(0 <= fi && fi <= ti && ti <= oldLength)) {
            throw new IndexOutOfBoundsException();
        }
        if (fi == ti) {
            return;
        }
        final int[] newArray = new int[oldLength + fi - ti];
        System.arraycopy(array, 0, newArray, 0, fi);
        System.arraycopy(array, ti, newArray, fi, oldLength - ti);
        array = newArray;
    }

    @Override
    public void removeIf(@NotNull IntPredicate filter) {
        final int oldLength = array.length;
        final int[] newArray = new int[oldLength];
        int newLength = 0;
        for (int t : array) {
            if (filter.test(t)) {
                continue; // remove it
            }
            newArray[newLength++] = t;
        }
        if (oldLength == newLength) {
            return;
        }
        array = Arrays.copyOf(newArray, newLength);
    }

    @Override
    public int setInt(int i, int t) {
        final int oldValue = array[i]; // index check included
        final int[] newArray = array.clone();
        newArray[i] = t;
        array = newArray;
        return oldValue;
    }

    @Override
    public void swap(int i, int j) {
        final int vi = array[i]; // index check included
        final int vj = array[j]; // index check included
        if (vi == vj) { // i == j or just vi == vj
            return;
        }
        final int[] newArray = array.clone();
        newArray[i] = vj;
        newArray[j] = vi;
        array = newArray;
    }

    @Override
    public void map(@NotNull IntUnaryOperator operator) {
        final int length = array.length;
        final int[] newArray = new int[length];
        for (int i = 0; i < length; i++) {
            newArray[i] = operator.applyAsInt(array[i]);
        }
        array = newArray;
    }

    @Override
    public void clear() {
        array = PublicIntList.EMPTY.array;
    }

    @Override
    public void reverse() {
        final int length = array.length;
        if (length <= 1) {
            return;
        }
        final int[] newArray = new int[length];
        int i = length;
        for (int t : array) {
            newArray[--i] = t;
        }
        array = newArray;
    }

    @Override
    public void rotate(int d) {
        final int n = array.length;
        if (n <= 1) {
            return;
        }
        d %= n;
        if (d == 0) {
            return;
        }
        if (d < 0) {
            d += n;
        }
        final int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[(i + d) % n] = array[i];
        }
        array = newArray;
    }

    @Override
    public void sort(boolean descending) {
        final int[] newArray = array.clone();
        Arrays.sort(newArray);
        if (descending) {
            int i = 0;
            int j = newArray.length - 1;
            while (i < j) {
                int t = newArray[i];
                newArray[i] = newArray[j];
                newArray[j] = t;
                i++;
                j--;
            }
        }
        array = newArray;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + array.length + ", identity = @" + Integer.toHexString(System.identityHashCode(array)) + ">";
    }
}
