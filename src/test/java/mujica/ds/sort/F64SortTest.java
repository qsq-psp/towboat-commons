package mujica.ds.sort;

import mujica.algebra.random.RandomContext;
import mujica.ds.f64.ArithmeticF64;
import mujica.ds.f64.F64;
import mujica.ds.f64.SlotArrayAllocatorF64;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@CodeHistory(date = "2026/7/31")
public class F64SortTest {

    private static final int REPEAT_COUNT = 45;

    private static final int MAIN_LENGTH = 266;

    private static final int ASIDE_LENGTH = 20;

    private final RandomContext rc = new RandomContext();

    private void checkSortAll(@NotNull Sort<double[]> algorithm) {
        double[] expected = SlotArrayAllocatorF64.EMPTY_ARRAY;
        double[] actual = SlotArrayAllocatorF64.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                expected = rc.nextDoubleArray(rc.nextInt(2, MAIN_LENGTH));
                actual = expected.clone();
                Arrays.sort(expected);
                algorithm.sort(actual);
                if (algorithm.isDescending()) {
                    SlotArrayAllocatorF64.INSTANCE.reverse(expected);
                    Assert.assertTrue(ArithmeticF64.INSTANCE.isDescending(actual));
                } else {
                    Assert.assertTrue(ArithmeticF64.INSTANCE.isAscending(actual));
                }
                Assert.assertArrayEquals(expected, actual, 0.0);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void checkSortAll(@NotNull SlotArrayComparator<F64, double[]> comparator) {
        checkSortAll(new BubbleSort<>(comparator));
        checkSortAll(new SelectionSort<>(comparator));
        checkSortAll(new InsertionSort<>(comparator));
        checkSortAll(new MergeSort<>(comparator, false));
        checkSortAll(new MergeSort<>(comparator, true));
    }

    @Test
    public void checkSortAll() {
        final ArithmeticF64 arithmetic = ArithmeticF64.INSTANCE;
        checkSortAll(arithmetic);
    }
}
