package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.any.list.MonotonicityDirection;
import mujica.ds.any.list.SortingAlgorithm;
import mujica.ds.i32.S32;
import mujica.ds.i32.set.IntSet;
import mujica.ds.i32.set.LinearClosedHashIntSet;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@CodeHistory(date = "2025/3/24", name = "SortingAlgorithmTest")
@CodeHistory(date = "2026/2/2", name = "IntSortingAlgorithmTest")
@CodeHistory(date = "2026/7/13")
public class S32SortTest {

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
        // checkAscending(S32AscendingBubbleSort.INSTANCE);
        checkAscending(new IntAscendingBucketSort(IntAscendingSelectionSort.INSTANCE, 10, 20, 25));
        checkAscending(new IntAscendingCountSort(true));
        // checkAscending(S32AscendingHeapSort.INSTANCE);
        // checkAscending(S32AscendingInsertionSort.INSTANCE);
        checkAscending(new IntAscendingMergeSort(true));
        // checkAscending(new IntAscendingQuickSort(SelectFirstAsPivot.INSTANCE));
        // checkAscending(new IntAscendingQuickSort(SelectTwoMeanAsPivot.INSTANCE));
        // checkAscending(new IntAscendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        // checkAscending(new IntAscendingQuickSort(new SelectRandomIntAsPivot(fc)));
        checkAscending(new IntAscendingRadixSort(10));
        checkAscending(IntAscendingSelectionSort.INSTANCE);
        checkAscending(new IntAscendingTournamentSort(true));
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
        // checkAscendingPart(S32AscendingBubbleSort.INSTANCE);
        // checkAscendingPart(S32AscendingHeapSort.INSTANCE);
        checkAscendingPart(new IntAscendingMergeSort(true));
        checkAscendingPart(IntAscendingSelectionSort.INSTANCE);
        checkAscendingPart(new IntAscendingTournamentSort(true));
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
        // checkDescending(S32DescendingBubbleSort.INSTANCE);
        checkDescending(new IntDescendingBucketSort(IntDescendingSelectionSort.INSTANCE, 10, 20, 25));
        checkDescending(new IntDescendingCountSort(true));
        // checkDescending(S32DescendingHeapSort.INSTANCE);
        // checkDescending(S32DescendingInsertionSort.INSTANCE);
        checkDescending(new IntDescendingMergeSort(true));
        // checkDescending(new IntDescendingQuickSort(SelectFirstAsPivot.INSTANCE));
        // checkDescending(new IntDescendingQuickSort(SelectTwoMeanAsPivot.INSTANCE));
        // checkDescending(new IntDescendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        // checkDescending(new IntDescendingQuickSort(new SelectRandomIntAsPivot(fc)));
        checkDescending(new IntDescendingRadixSort(10));
        checkDescending(IntDescendingSelectionSort.INSTANCE);
        checkDescending(new IntDescendingTournamentSort(true));
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
        // checkDescendingPart(S32DescendingBubbleSort.INSTANCE);
        // checkDescendingPart(S32DescendingHeapSort.INSTANCE);
        checkDescendingPart(new IntDescendingMergeSort(true));
        checkDescendingPart(IntDescendingSelectionSort.INSTANCE);
        checkDescendingPart(new IntDescendingTournamentSort(true));
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
        // fuzzAscending(S32AscendingBubbleSort.INSTANCE);
        fuzzAscending(new IntAscendingBucketSort(IntAscendingSelectionSort.INSTANCE, Integer.MIN_VALUE, 0x10000000, 0x10));
        fuzzAscending(new IntAscendingCountSort(true));
        // fuzzAscending(S32AscendingHeapSort.INSTANCE);
        // fuzzAscending(S32AscendingInsertionSort.INSTANCE);
        fuzzAscending(new IntAscendingMergeSort(true));
        // fuzzAscending(new IntAscendingQuickSort(SelectFirstAsPivot.INSTANCE));
        // fuzzAscending(new IntAscendingQuickSort(SelectTwoMeanAsPivot.INSTANCE));
        // fuzzAscending(new IntAscendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        // fuzzAscending(new IntAscendingQuickSort(new SelectRandomIntAsPivot(fc)));
        fuzzAscending(new IntAscendingRadixSort(10));
        fuzzAscending(IntAscendingSelectionSort.INSTANCE);
        fuzzAscending(new IntAscendingTournamentSort(true));
    }

    private void fuzzAscendingUnique(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.ASCENDING, algorithm.monotonicity());
        final S32 slot = new S32();
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int mainLength = fc.nextInt(2, MAIN_LENGTH);
            int prefixLength = fc.nextInt(ASIDE_LENGTH);
            int[] array = fc.nextIntArray(prefixLength + mainLength + fc.nextInt(ASIDE_LENGTH));
            IntSet set = new LinearClosedHashIntSet(null);
            for (int index = 0; index < mainLength; index++) {
                set.add(array[prefixLength + index]);
            }
            slot.setI32(prefixLength + mainLength);
            algorithm.sortUnique(array, prefixLength, slot);
            int endIndex = slot.getI32();
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
        // fuzzAscendingUnique(S32AscendingBubbleSort.INSTANCE);
        fuzzAscendingUnique(new IntAscendingCountSort(true));
        // fuzzAscendingUnique(S32AscendingHeapSort.INSTANCE);
        // fuzzAscendingUnique(S32AscendingInsertionSort.INSTANCE);
        fuzzAscendingUnique(new IntAscendingMergeSort(true));
        // fuzzAscendingUnique(new IntAscendingQuickSort(SelectFirstAsPivot.INSTANCE));
        // fuzzAscendingUnique(new IntAscendingQuickSort(SelectTwoMeanAsPivot.INSTANCE));
        // fuzzAscendingUnique(new IntAscendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        // fuzzAscendingUnique(new IntAscendingQuickSort(new SelectRandomIntAsPivot(fc)));
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
        // fuzzDescending(S32DescendingBubbleSort.INSTANCE);
        fuzzDescending(new IntDescendingBucketSort(IntDescendingSelectionSort.INSTANCE, Integer.MIN_VALUE, 0x10000000, 0x10));
        fuzzDescending(new IntDescendingCountSort(true));
        // fuzzDescending(S32DescendingHeapSort.INSTANCE);
        // fuzzDescending(S32DescendingInsertionSort.INSTANCE);
        fuzzDescending(new IntDescendingMergeSort(true));
        // fuzzDescending(new IntDescendingQuickSort(SelectFirstAsPivot.INSTANCE));
        // fuzzDescending(new IntDescendingQuickSort(SelectTwoMeanAsPivot.INSTANCE));
        // fuzzDescending(new IntDescendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        // fuzzDescending(new IntDescendingQuickSort(new SelectRandomIntAsPivot(fc)));
        fuzzDescending(new IntDescendingRadixSort(10));
        fuzzDescending(IntDescendingSelectionSort.INSTANCE);
        fuzzDescending(new IntDescendingTournamentSort(true));
    }

    private void fuzzDescendingUnique(@NotNull SortingAlgorithm<int[]> algorithm) {
        Assert.assertEquals(MonotonicityDirection.DESCENDING, algorithm.monotonicity());
        final S32 slot = new S32();
        for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
            int mainLength = fc.nextInt(2, MAIN_LENGTH);
            int prefixLength = fc.nextInt(ASIDE_LENGTH);
            int[] array = fc.nextIntArray(prefixLength + mainLength + fc.nextInt(ASIDE_LENGTH));
            IntSet set = new LinearClosedHashIntSet(null);
            for (int index = 0; index < mainLength; index++) {
                set.add(array[prefixLength + index]);
            }
            slot.setI32(prefixLength + mainLength);
            algorithm.sortUnique(array, prefixLength, slot);
            int endIndex = slot.getI32();
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
        // fuzzDescendingUnique(S32DescendingBubbleSort.INSTANCE);
        fuzzDescendingUnique(new IntDescendingCountSort(true));
        // fuzzDescendingUnique(S32DescendingHeapSort.INSTANCE);
        // fuzzDescendingUnique(S32DescendingInsertionSort.INSTANCE);
        fuzzDescendingUnique(new IntDescendingMergeSort(true));
        // fuzzDescendingUnique(new IntDescendingQuickSort(SelectFirstAsPivot.INSTANCE));
        // fuzzDescendingUnique(new IntDescendingQuickSort(SelectTwoMeanAsPivot.INSTANCE));
        // fuzzDescendingUnique(new IntDescendingQuickSort(SelectThreeIntMedianAsPivot.INSTANCE));
        // fuzzDescendingUnique(new IntDescendingQuickSort(new SelectRandomIntAsPivot(fc)));
        fuzzDescendingUnique(new IntDescendingRadixSort(10));
        fuzzDescendingUnique(IntDescendingSelectionSort.INSTANCE);
    }
}
