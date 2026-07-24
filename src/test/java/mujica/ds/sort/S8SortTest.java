package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i8.S8;
import mujica.ds.i8.SlotArrayComparatorS8;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2026/7/23")
public class S8SortTest {

    private static final int MAIN_LENGTH = 998;

    private static final int ASIDE_LENGTH = 74;

    private final FuzzyContext fc = new FuzzyContext();

    private void fuzzSortAll(@NotNull Sort<byte[]> algorithm) {
        final int length = fc.nextInt(2, MAIN_LENGTH);
        final byte[] array = fc.nextByteArray(length);
        algorithm.sort(array);
        if (algorithm.isDescending()) {
            for (int index = 1; index < length; index++) {
                Assert.assertTrue(array[index - 1] >= array[index]);
            }
        } else {
            for (int index = 1; index < length; index++) {
                Assert.assertTrue(array[index - 1] <= array[index]);
            }
        }
    }

    private void fuzzSortAll(@NotNull SlotArrayComparator<S8, byte[]> comparator) {
        fuzzSortAll(new BubbleSort<>(comparator));
        fuzzSortAll(new InsertionSort<>(comparator));
        fuzzSortAll(new MergeSort<>(comparator, false));
        fuzzSortAll(new MergeSort<>(comparator, true));
        fuzzSortAll(new QuickSort<>(comparator, new SelectFirstAsPivot<>(comparator)));
        fuzzSortAll(new QuickSort<>(comparator, new SelectThreeMedianAsPivot<>(comparator)));
        fuzzSortAll(new QuickSort<>(comparator, new SelectMedianOfMedianAsPivot<>(comparator)));
        fuzzSortAll(new QuickSort<>(comparator, new SelectRandomAsPivot<>(comparator, fc)));
    }

    @Test
    public void fuzzSortAll() {
        fuzzSortAll(new SlotArrayComparatorS8());
        fuzzSortAll(new S8AscendingCountSort());
    }

    @Test
    public void fuzzSortRange() {
        //
    }

    @Test
    public void fuzzSortPart() {
        //
    }

    @Test
    public void fuzzSortUnique() {
        //
    }
}
