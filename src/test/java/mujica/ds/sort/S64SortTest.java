package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i64.S64Arithmetic;
import mujica.ds.i64.S64;
import mujica.ds.i64.I64SlotArrayAllocator;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@CodeHistory(date = "2026/7/27")
public class S64SortTest {

    private static final int REPEAT_COUNT = 48;

    private static final int MAIN_LENGTH = 272;

    private static final int ASIDE_LENGTH = 19;

    private final FuzzyContext fc = new FuzzyContext();

    private void fuzzSortAll(@NotNull Sort<long[]> algorithm) {
        long[] expected = I64SlotArrayAllocator.EMPTY_ARRAY;
        long[] actual = I64SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                expected = fc.nextLongArray(fc.nextInt(2, MAIN_LENGTH));
                actual = expected.clone();
                Arrays.sort(expected);
                algorithm.sort(actual);
                if (algorithm.isDescending()) {
                    I64SlotArrayAllocator.INSTANCE.reverse(expected);
                    Assert.assertTrue(S64Arithmetic.INSTANCE.isDescending(actual));
                } else {
                    Assert.assertTrue(S64Arithmetic.INSTANCE.isAscending(actual));
                }
                Assert.assertArrayEquals(expected, actual);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void fuzzSortAll(@NotNull SlotArrayComparator<S64, long[]> comparator) {
        fuzzSortAll(new BubbleSort<>(comparator));
        fuzzSortAll(new SelectionSort<>(comparator));
        fuzzSortAll(new InsertionSort<>(comparator));
        fuzzSortAll(new MergeSort<>(comparator, false));
        fuzzSortAll(new MergeSort<>(comparator, true));
    }

    @Test
    public void fuzzSortAll() {
        final S64Arithmetic arithmetic = S64Arithmetic.INSTANCE;
        fuzzSortAll(arithmetic);
    }
}
