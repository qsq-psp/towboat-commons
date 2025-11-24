package mujica.ds;

import mujica.ds.of_int.IntOrdering;
import mujica.ds.of_int.list.*;
import mujica.ds.of_int.map.CompatibleIntSlotMap;
import mujica.ds.of_long.LongOrdering;
import mujica.ds.of_long.list.*;
import mujica.math.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2025/3/24")
public class SortingAlgorithmTest {

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
    }

    private void fuzzLong(@NotNull SortingAlgorithm<long[]> algorithm) {
        final int orderingComposition = algorithm.orderingComposition();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long[] array = fc.nextLongArray(fc.nextInt(SIZE));
            algorithm.apply(array, 0, array.length);
            int orderingFlags = LongOrdering.INSTANCE.orderingFlags(array);
            Assert.assertEquals(orderingComposition, orderingComposition | (1 << orderingFlags));
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
    }
}
