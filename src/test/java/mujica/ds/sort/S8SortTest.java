package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i32.I32SlotArrayAllocator;
import mujica.ds.i32.S32Arithmetic;
import mujica.ds.i8.S8;
import mujica.ds.i8.I8SlotArrayAllocator;
import mujica.ds.i8.S8SlotArrayComparator;
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
        byte[] expected = I8SlotArrayAllocator.EMPTY_ARRAY;
        byte[] actual = I8SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                expected = fc.nextByteArray(fc.nextInt(2, MAIN_LENGTH));
                actual = expected.clone();
                Arrays.sort(expected);
                algorithm.sort(actual);
                if (algorithm.isDescending()) {
                    I8SlotArrayAllocator.INSTANCE.reverse(expected);
                    Assert.assertTrue(S8SlotArrayComparator.INSTANCE.isDescending(actual));
                } else {
                    Assert.assertTrue(S8SlotArrayComparator.INSTANCE.isAscending(actual));
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
        fuzzSortAll(new ShellSort<>(comparator));
        fuzzSortAll(new HeapSort<>(comparator));
        fuzzSortAll(new MergeSort<>(comparator, true));
        fuzzSortAll(new MergeSort<>(comparator, false));
        // fuzzSortAll(new QuickSort<>(comparator, new SelectFirstAsPivot<>(comparator)));
        // fuzzSortAll(new QuickSort<>(comparator, new SelectThreeMedianAsPivot<>(comparator)));
        // fuzzSortAll(new QuickSort<>(comparator, new SelectMedianOfMedianAsPivot<>(comparator)));
        // fuzzSortAll(new QuickSort<>(comparator, new SelectRandomAsPivot<>(comparator, fc)));
    }

    @Test
    public void fuzzSortAll() {
        fuzzSortAll(new S8AscendingCountSort());
        fuzzSortAll(new S8DescendingCountSort());
        fuzzSortAll(S8SlotArrayComparator.INSTANCE);
    }

    private void fuzzSortRange(@NotNull Sort<byte[]> algorithm) {
        byte[] expected = I8SlotArrayAllocator.EMPTY_ARRAY;
        byte[] actual = I8SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int mainLength = fc.nextInt(2, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                expected = fc.nextByteArray(leftLength + mainLength + rightLength);
                actual = expected.clone();
                Arrays.sort(expected, leftLength, leftLength + mainLength);
                algorithm.sort(actual, leftLength, leftLength + mainLength);
                if (algorithm.isDescending()) {
                    I8SlotArrayAllocator.INSTANCE.reverse(expected, leftLength, leftLength + mainLength);
                    Assert.assertTrue(S8SlotArrayComparator.INSTANCE.isDescending(actual, leftLength, leftLength + mainLength));
                } else {
                    Assert.assertTrue(S8SlotArrayComparator.INSTANCE.isAscending(actual, leftLength, leftLength + mainLength));
                }
                Assert.assertArrayEquals(expected, actual);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void fuzzSortRange(@NotNull SlotArrayComparator<S8, byte[]> comparator) {
        fuzzSortRange(new BubbleSort<>(comparator));
        fuzzSortRange(new SelectionSort<>(comparator));
        fuzzSortRange(new InsertionSort<>(comparator));
        fuzzSortRange(new ShellSort<>(comparator));
        fuzzSortRange(new HeapSort<>(comparator));
        fuzzSortRange(new MergeSort<>(comparator, true));
        fuzzSortRange(new MergeSort<>(comparator, false));
    }

    @Test
    public void fuzzSortRange() {
        fuzzSortRange(new S8AscendingCountSort());
        fuzzSortRange(new S8DescendingCountSort());
        fuzzSortRange(S8SlotArrayComparator.INSTANCE);
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
