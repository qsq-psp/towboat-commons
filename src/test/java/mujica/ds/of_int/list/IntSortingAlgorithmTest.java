package mujica.ds.of_int.list;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.PublicIntSlot;
import mujica.ds.of_int.set.IntSet;
import mujica.ds.of_int.set.LinearClosedHashIntSet;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@CodeHistory(date = "2025/3/24", name = "SortingAlgorithmTest")
@CodeHistory(date = "2026/2/2")
public class IntSortingAlgorithmTest {

    private static final int REPEAT_COUNT = 76;

    private static final int MAIN_LENGTH = 533;

    private static final int ASIDE_LENGTH = 21;

    private final FuzzyContext fc = new FuzzyContext();

    private void checkAscending(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int length = fc.nextInt(2, MAIN_LENGTH);
            int[] array = fc.shuffledCardinal(length);
            algorithm.sort(array);
            for (int index = 0; index < length; index++) {
                Assert.assertEquals(index, array[index]);
            }
        }
    }

    @Test
    public void checkAscending() {
        checkAscending(IntAscendingBubbleSort.INSTANCE);
        checkAscending(new IntAscendingBucketSort(IntAscendingSelectionSort.INSTANCE, 10, 20, 25));
        checkAscending(IntAscendingHeapSort.INSTANCE);
        checkAscending(IntAscendingInsertionSort.INSTANCE);
        checkAscending(new IntAscendingMergeSort(true));
        checkAscending(new IntAscendingQuickSort(SelectFirstIntAsPivot.INSTANCE));
        checkAscending(new IntAscendingQuickSort(SelectTwoIntMeanAsPivot.INSTANCE));
        checkAscending(new IntAscendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        checkAscending(new IntAscendingRadixSort(10));
        checkAscending(IntAscendingSelectionSort.INSTANCE);
        checkAscending(new IntAscendingTournamentSort());
    }

    private void checkAscendingPart(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int inLength = fc.nextInt(2, MAIN_LENGTH);
            int outLength = fc.nextInt(inLength - 1);
            int[] array = fc.shuffledCardinal(inLength);
            algorithm.sortPart(array, 0, outLength, inLength);
            for (int index = 0; index < outLength; index++) {
                Assert.assertEquals(index, array[index]);
            }
        }
    }

    @Test
    public void checkAscendingPart() {
        checkAscendingPart(IntAscendingBubbleSort.INSTANCE);
        checkAscendingPart(IntAscendingHeapSort.INSTANCE);
        checkAscendingPart(new IntAscendingMergeSort(true));
        checkAscendingPart(IntAscendingSelectionSort.INSTANCE);
        checkAscendingPart(new IntAscendingTournamentSort());
    }

    private void checkDescending(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.DESCENDING, algorithm.monotonicity());
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int length = fc.nextInt(2, MAIN_LENGTH);
            int[] array = fc.shuffledCardinal(length);
            algorithm.sort(array);
            for (int index = 0; index < length; index++) {
                Assert.assertEquals(length - 1 - index, array[index]);
            }
        }
    }

    @Test
    public void checkDescending() {
        checkDescending(IntDescendingBubbleSort.INSTANCE);
        checkDescending(IntDescendingHeapSort.INSTANCE);
        checkDescending(IntDescendingInsertionSort.INSTANCE);
        checkDescending(new IntDescendingMergeSort(true));
        checkDescending(new IntDescendingQuickSort(SelectFirstIntAsPivot.INSTANCE));
        checkDescending(new IntDescendingQuickSort(SelectTwoIntMeanAsPivot.INSTANCE));
        checkDescending(new IntDescendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        checkDescending(new IntDescendingRadixSort(10));
        checkDescending(IntDescendingSelectionSort.INSTANCE);
        checkDescending(new IntDescendingTournamentSort());
    }

    private void checkDescendingPart(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.DESCENDING, algorithm.monotonicity());
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int inLength = fc.nextInt(2, MAIN_LENGTH);
            int outLength = fc.nextInt(inLength - 1);
            int[] array = fc.shuffledCardinal(inLength);
            algorithm.sortPart(array, 0, outLength, inLength);
            for (int index = 0; index < outLength; index++) {
                Assert.assertEquals(inLength - 1 - index, array[index]);
            }
        }
    }

    @Test
    public void checkDescendingPart() {
        checkDescendingPart(IntDescendingBubbleSort.INSTANCE);
        checkDescendingPart(IntDescendingHeapSort.INSTANCE);
        checkDescendingPart(new IntDescendingMergeSort(true));
        checkDescendingPart(IntDescendingSelectionSort.INSTANCE);
        checkDescendingPart(new IntDescendingTournamentSort());
    }

    private void fuzzAscending(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int mainLength = fc.nextInt(2, MAIN_LENGTH);
            int prefixLength = fc.nextInt(ASIDE_LENGTH);
            int[] expected = fc.nextIntArray(prefixLength + mainLength + fc.nextInt(ASIDE_LENGTH));
            int[] actual = expected.clone();
            Arrays.sort(expected, prefixLength, prefixLength + mainLength);
            algorithm.sort(actual, prefixLength, prefixLength + mainLength);
            Assert.assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void fuzzAscending() {
        fuzzAscending(IntAscendingBubbleSort.INSTANCE);
        fuzzAscending(new IntAscendingBucketSort(IntAscendingSelectionSort.INSTANCE, Integer.MIN_VALUE, 0x10000000, 0x10));
        fuzzAscending(IntAscendingHeapSort.INSTANCE);
        fuzzAscending(IntAscendingInsertionSort.INSTANCE);
        fuzzAscending(new IntAscendingMergeSort(true));
        fuzzAscending(new IntAscendingQuickSort(SelectFirstIntAsPivot.INSTANCE));
        fuzzAscending(new IntAscendingQuickSort(SelectTwoIntMeanAsPivot.INSTANCE));
        fuzzAscending(new IntAscendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        fuzzAscending(new IntAscendingRadixSort(10));
        fuzzAscending(IntAscendingSelectionSort.INSTANCE);
        fuzzAscending(new IntAscendingTournamentSort());
    }

    private void fuzzAscendingUnique(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        final PublicIntSlot slot = new PublicIntSlot();
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int mainLength = fc.nextInt(2, MAIN_LENGTH);
            int prefixLength = fc.nextInt(ASIDE_LENGTH);
            int[] array = fc.nextIntArray(prefixLength + mainLength + fc.nextInt(ASIDE_LENGTH));
            IntSet set = new LinearClosedHashIntSet(null);
            for (int index = 0; index < mainLength; index++) {
                set.add(array[prefixLength + index]);
            }
            slot.setInt(prefixLength + mainLength);
            algorithm.sortUnique(array, prefixLength, slot);
            int endIndex = slot.getInt();
            Assert.assertTrue(prefixLength < endIndex);
            Assert.assertTrue(endIndex <= prefixLength + mainLength);
            for (int index = prefixLength + 1; index < endIndex; index++) {
                Assert.assertTrue(array[index - 1] < array[index]);
            }
            for (int index = prefixLength; index < endIndex; index++) {
                Assert.assertTrue(set.contains(array[index]));
            }
            Assert.assertEquals(set.intLength(), endIndex - prefixLength);
        }
    }

    @Test
    public void fuzzAscendingUnique() {
        fuzzAscendingUnique(IntAscendingBubbleSort.INSTANCE);
        fuzzAscendingUnique(IntAscendingHeapSort.INSTANCE);
        fuzzAscendingUnique(IntAscendingInsertionSort.INSTANCE);
        fuzzAscendingUnique(new IntAscendingMergeSort(true));
        fuzzAscendingUnique(new IntAscendingQuickSort(SelectFirstIntAsPivot.INSTANCE));
        fuzzAscendingUnique(new IntAscendingQuickSort(SelectTwoIntMeanAsPivot.INSTANCE));
        fuzzAscendingUnique(new IntAscendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        fuzzAscendingUnique(new IntAscendingRadixSort(10));
        fuzzAscendingUnique(IntAscendingSelectionSort.INSTANCE);
    }

    private void fuzzDescending(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.DESCENDING, algorithm.monotonicity());
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int mainLength = fc.nextInt(2, MAIN_LENGTH);
            int prefixLength = fc.nextInt(ASIDE_LENGTH);
            int[] expected = fc.nextIntArray(prefixLength + mainLength + fc.nextInt(ASIDE_LENGTH));
            int[] actual = expected.clone();
            Arrays.sort(expected, prefixLength, prefixLength + mainLength);
            algorithm.sort(actual, prefixLength, prefixLength + mainLength);
            for (int index = 0; index < mainLength; index++) {
                Assert.assertEquals(expected[prefixLength + mainLength - 1 - index], actual[prefixLength + index]);
            }
        }
    }

    @Test
    public void fuzzDescending() {
        fuzzDescending(IntDescendingBubbleSort.INSTANCE);
        fuzzDescending(IntDescendingHeapSort.INSTANCE);
        fuzzDescending(IntDescendingInsertionSort.INSTANCE);
        fuzzDescending(new IntDescendingMergeSort(true));
        fuzzDescending(new IntDescendingQuickSort(SelectFirstIntAsPivot.INSTANCE));
        fuzzDescending(new IntDescendingQuickSort(SelectTwoIntMeanAsPivot.INSTANCE));
        fuzzDescending(new IntDescendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        fuzzDescending(new IntDescendingRadixSort(10));
        fuzzDescending(IntDescendingSelectionSort.INSTANCE);
        fuzzDescending(new IntDescendingTournamentSort());
    }

    private void fuzzDescendingUnique(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.DESCENDING, algorithm.monotonicity());
        final PublicIntSlot slot = new PublicIntSlot();
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int mainLength = fc.nextInt(2, MAIN_LENGTH);
            int prefixLength = fc.nextInt(ASIDE_LENGTH);
            int[] array = fc.nextIntArray(prefixLength + mainLength + fc.nextInt(ASIDE_LENGTH));
            IntSet set = new LinearClosedHashIntSet(null);
            for (int index = 0; index < mainLength; index++) {
                set.add(array[prefixLength + index]);
            }
            slot.setInt(prefixLength + mainLength);
            algorithm.sortUnique(array, prefixLength, slot);
            int endIndex = slot.getInt();
            Assert.assertTrue(prefixLength < endIndex);
            Assert.assertTrue(endIndex <= prefixLength + mainLength);
            for (int index = prefixLength + 1; index < endIndex; index++) {
                Assert.assertTrue(array[index - 1] > array[index]);
            }
            for (int index = prefixLength; index < endIndex; index++) {
                Assert.assertTrue(set.contains(array[index]));
            }
            Assert.assertEquals(set.intLength(), endIndex - prefixLength);
        }
    }

    @Test
    public void fuzzDescendingUnique() {
        fuzzDescendingUnique(IntDescendingBubbleSort.INSTANCE);
        fuzzDescendingUnique(IntDescendingHeapSort.INSTANCE);
        fuzzDescendingUnique(IntDescendingInsertionSort.INSTANCE);
        fuzzDescendingUnique(new IntDescendingMergeSort(true));
        fuzzDescendingUnique(new IntDescendingQuickSort(SelectFirstIntAsPivot.INSTANCE));
        fuzzDescendingUnique(new IntDescendingQuickSort(SelectTwoIntMeanAsPivot.INSTANCE));
        fuzzDescendingUnique(new IntDescendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        fuzzDescendingUnique(new IntDescendingRadixSort(10));
        fuzzDescendingUnique(IntDescendingSelectionSort.INSTANCE);
    }
}
