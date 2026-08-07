package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i8.S8;
import mujica.ds.i8.SlotArrayAllocatorI8;
import mujica.ds.i8.SlotArrayComparatorS8;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@CodeHistory(date = "2026/7/23")
public class S8SortTest {

    private static final int REPEAT_COUNT = 35;

    private static final int MAIN_LENGTH = 998;

    private static final int ASIDE_LENGTH = 74;

    private final FuzzyContext fc = new FuzzyContext();

    private void fuzzSortAll(@NotNull Sort<byte[]> algorithm) {
        byte[] expected = SlotArrayAllocatorI8.EMPTY_ARRAY;
        byte[] actual = SlotArrayAllocatorI8.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                expected = fc.nextByteArray(fc.nextInt(2, MAIN_LENGTH));
                actual = expected.clone();
                Arrays.sort(expected);
                algorithm.sort(actual);
                if (algorithm.isDescending()) {
                    SlotArrayAllocatorI8.INSTANCE.reverse(expected);
                    Assert.assertTrue(SlotArrayComparatorS8.INSTANCE.isDescending(actual));
                } else {
                    Assert.assertTrue(SlotArrayComparatorS8.INSTANCE.isAscending(actual));
                }
                Assert.assertArrayEquals(expected, actual);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void fuzzSortAll(@NotNull SlotArrayComparator<S8, byte[]> comparator) {
        fuzzSortAll(new BubbleSort<>(comparator));
        fuzzSortAll(new SelectionSort<>(comparator));
        fuzzSortAll(new InsertionSort<>(comparator));
        fuzzSortAll(new MergeSort<>(comparator, false));
        fuzzSortAll(new MergeSort<>(comparator, true));
        // fuzzSortAll(new QuickSort<>(comparator, new SelectFirstAsPivot<>(comparator)));
        // fuzzSortAll(new QuickSort<>(comparator, new SelectThreeMedianAsPivot<>(comparator)));
        // fuzzSortAll(new QuickSort<>(comparator, new SelectMedianOfMedianAsPivot<>(comparator)));
        // fuzzSortAll(new QuickSort<>(comparator, new SelectRandomAsPivot<>(comparator, fc)));
    }

    @Test
    public void fuzzSortAll() {
        fuzzSortAll(new S8AscendingCountSort());
        fuzzSortAll(SlotArrayComparatorS8.INSTANCE);
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
