package mujica.ds;

import mujica.ds.of_int.list.IntOrdering;
import mujica.ds.of_int.list.*;
import mujica.ds.of_int.map.CompatibleIntSlotMap;
import mujica.ds.of_long.list.LongOrdering;
import mujica.ds.of_long.list.*;
import mujica.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@CodeHistory(date = "2025/3/24")
public class SortingAlgorithmTest implements OrderingConstants {

    private static final int REPEAT = 222;

    private static final int SIZE = 555;

    private final FuzzyContext fc = new FuzzyContext();

    private void fuzzInt(@NotNull SortingAlgorithm<int[]> algorithm) {
        final int orderingComposition = algorithm.orderingComposition();
        final CompatibleIntSlotMap before = new CompatibleIntSlotMap();
        final CompatibleIntSlotMap after = new CompatibleIntSlotMap();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int[] array = fc.nextIntArray(fc.nextInt(SIZE));
            before.clear();
            for (int value : array) {
                before.addInt(value, 1);
            }
            algorithm.apply(array, 0, array.length);
            after.clear();
            for (int value : array) {
                after.addInt(value, 1);
            }
            Assert.assertEquals(before, after);
            int orderingFlags = IntOrdering.INSTANCE.orderingFlags(array);
            Assert.assertEquals(orderingComposition, orderingComposition | (1 << orderingFlags));
        }
    }

    @Test
    public void fuzzInt() {
        fuzzInt(IntAscendingBubbleSort.INSTANCE);
        fuzzInt(IntDescendingBubbleSort.INSTANCE);
        fuzzInt(IntAscendingInsertionSort.INSTANCE);
        fuzzInt(IntDescendingInsertionSort.INSTANCE);
        fuzzInt(IntAscendingSelectionSort.INSTANCE);
        fuzzInt(IntDescendingSelectionSort.INSTANCE);
        fuzzInt(IntAscendingShellSort.INSTANCE);
        fuzzInt(IntDescendingShellSort.INSTANCE);
        fuzzInt(IntAscendingHeapSort.INSTANCE);
        fuzzInt(IntDescendingHeapSort.INSTANCE);
        fuzzInt(new IntAscendingMergeSort());
        fuzzInt(new IntDescendingMergeSort());
        fuzzInt(new IntAscendingQuickSort(SelectFirstIntAsPivot.INSTANCE));
        fuzzInt(new IntAscendingQuickSort(SelectTwoIntMeanAsPivot.INSTANCE));
        fuzzInt(new IntAscendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        fuzzInt(new IntDescendingQuickSort(SelectFirstIntAsPivot.INSTANCE));
        fuzzInt(new IntDescendingQuickSort(SelectTwoIntMeanAsPivot.INSTANCE));
        fuzzInt(new IntDescendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
    }

    private void reverse(@NotNull long[] array) {
        int start = 0;
        int end = array.length;
        while (true) {
            end--;
            if (start >= end) {
                break;
            }
            long temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
        }
    }

    private void fuzzLong(@NotNull SortingAlgorithm<long[]> algorithm) {
        final int orderingComposition = algorithm.orderingComposition();
        final boolean ascending = (orderingComposition & (1 << STRICT_ASCENDING)) != 0;
        final boolean descending = (orderingComposition & (1 << STRICT_DESCENDING)) != 0;
        Assert.assertTrue(ascending ^ descending);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long[] expected = fc.nextLongArray(fc.nextInt(SIZE));
            long[] actual = expected.clone();
            try {
                algorithm.apply(actual, 0, actual.length);
                int orderingFlags = LongOrdering.INSTANCE.orderingFlags(actual);
                Assert.assertEquals(orderingComposition, orderingComposition | (1 << orderingFlags));
                Arrays.sort(expected);
                if (descending) {
                    reverse(expected);
                }
                Assert.assertArrayEquals(expected, actual);
            } catch (Throwable e) {
                System.out.println(Arrays.toString(expected));
                System.out.println(Arrays.toString(actual));
                throw e;
            }
        }
    }

    @Test
    public void fuzzLong() {
        fuzzLong(LongAscendingBubbleSort.INSTANCE);
        fuzzLong(LongDescendingBubbleSort.INSTANCE);
        fuzzLong(LongAscendingInsertionSort.INSTANCE);
        fuzzLong(LongDescendingInsertionSort.INSTANCE);
        fuzzLong(LongAscendingSelectionSort.INSTANCE);
        fuzzLong(LongDescendingSelectionSort.INSTANCE);
        fuzzLong(LongAscendingShellSort.INSTANCE);
        fuzzLong(LongDescendingShellSort.INSTANCE);
        fuzzLong(new LongAscendingQuickSort(SelectFirstLongAsPivot.INSTANCE));
        fuzzLong(new LongAscendingQuickSort(SelectTwoLongMeanAsPivot.INSTANCE));
        fuzzLong(new LongAscendingQuickSort(SelectThreeLongMedianAsPivot.INSTANCE));
    }
}
