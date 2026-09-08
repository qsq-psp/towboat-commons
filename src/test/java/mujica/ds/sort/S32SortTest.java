package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i32.I32SlotArrayAllocator;
import mujica.ds.i32.S32;
import mujica.ds.i32.S32Arithmetic;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.Arrays;

@CodeHistory(date = "2025/3/24", name = "SortingAlgorithmTest")
@CodeHistory(date = "2026/2/2", name = "IntSortingAlgorithmTest")
@CodeHistory(date = "2026/7/13")
public class S32SortTest {

    private static final int REPEAT_COUNT = 45;

    private static final int MAIN_LENGTH = 533;

    private static final int ASIDE_LENGTH = 21;

    private final FuzzyContext fc = new FuzzyContext();

    private void fuzzSortAll(@NotNull Sort<int[]> algorithm) {
        int[] expected = I32SlotArrayAllocator.EMPTY_ARRAY;
        int[] actual = I32SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                expected = fc.nextIntArray(fc.nextInt(2, MAIN_LENGTH));
                actual = expected.clone();
                Arrays.sort(expected);
                algorithm.sort(actual);
                if (algorithm.isDescending()) {
                    I32SlotArrayAllocator.INSTANCE.reverse(expected);
                    Assert.assertTrue(S32Arithmetic.INSTANCE.isDescending(actual));
                } else {
                    Assert.assertTrue(S32Arithmetic.INSTANCE.isAscending(actual));
                }
                Assert.assertArrayEquals(expected, actual);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    @Test
    public void fuzzSortAll() {
        final S32Arithmetic arithmetic = S32Arithmetic.INSTANCE;
        fuzzSortAll(S32AscendingBubbleSort.INSTANCE);
        fuzzSortAll(S32DescendingBubbleSort.INSTANCE);
        fuzzSortAll(new BubbleSort<>(arithmetic));
        fuzzSortAll(S32AscendingInsertionSort.INSTANCE);
        fuzzSortAll(S32DescendingInsertionSort.INSTANCE);
        fuzzSortAll(new InsertionSort<>(arithmetic));
        fuzzSortAll(S32AscendingSelectionSort.INSTANCE);
        fuzzSortAll(S32DescendingSelectionSort.INSTANCE);
        fuzzSortAll(new SelectionSort<>(arithmetic));
        fuzzSortAll(S32AscendingShellSort.INSTANCE);
        fuzzSortAll(S32DescendingShellSort.INSTANCE);
        fuzzSortAll(new ShellSort<>(arithmetic));
        fuzzSortAll(S32AscendingHeapSort.INSTANCE);
        fuzzSortAll(S32DescendingHeapSort.INSTANCE);
        fuzzSortAll(new HeapSort<>(arithmetic));
        fuzzSortAll(new S32AscendingMergeSort(false));
        fuzzSortAll(new S32DescendingMergeSort(false));
        fuzzSortAll(new MergeSort<>(arithmetic, false));
        fuzzSortAll(new S32AscendingMergeSort(true));
        fuzzSortAll(new S32DescendingMergeSort(true));
        fuzzSortAll(new MergeSort<>(arithmetic, true));
        fuzzSortAll(new S32AscendingCountSort(false));
        fuzzSortAll(new S32DescendingCountSort(false));
        fuzzSortAll(new S32AscendingCountSort(true));
        fuzzSortAll(new S32DescendingCountSort(true));
        fuzzSortAll(new S32AscendingTournamentSort(false));
        fuzzSortAll(new S32DescendingTournamentSort(false));
        fuzzSortAll(new S32AscendingTournamentSort(true));
        fuzzSortAll(new S32DescendingTournamentSort(true));
        fuzzSortAll(new S32AscendingRadixSort(10));
        fuzzSortAll(new S32DescendingRadixSort(10));
        fuzzSortAll(new S32AscendingBucketSort(S32AscendingShellSort.INSTANCE, -1000, 1000, 36));
        fuzzSortAll(new S32DescendingBucketSort(S32DescendingShellSort.INSTANCE, -1000, 1000, 36));
        SelectPivot<S32, int[]> selectPivot = new SelectFirstAsPivot<>(arithmetic);
        fuzzSortAll(new S32AscendingQuickSort(selectPivot));
        fuzzSortAll(new S32DescendingQuickSort(selectPivot));
        fuzzSortAll(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectTwoMeanAsPivot<>(arithmetic, arithmetic, RoundingMode.DOWN);
        fuzzSortAll(new S32AscendingQuickSort(selectPivot));
        fuzzSortAll(new S32DescendingQuickSort(selectPivot));
        fuzzSortAll(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectThreeMedianAsPivot<>(arithmetic);
        fuzzSortAll(new S32AscendingQuickSort(selectPivot));
        fuzzSortAll(new S32DescendingQuickSort(selectPivot));
        fuzzSortAll(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectMedianOfMedianAsPivot<>(arithmetic);
        fuzzSortAll(new S32AscendingQuickSort(selectPivot));
        fuzzSortAll(new S32DescendingQuickSort(selectPivot));
        fuzzSortAll(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectRangeCenterAsPivot<>(arithmetic, arithmetic, RoundingMode.DOWN);
        fuzzSortAll(new S32AscendingQuickSort(selectPivot));
        fuzzSortAll(new S32DescendingQuickSort(selectPivot));
        fuzzSortAll(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectRandomAsPivot<>(arithmetic, fc);
        fuzzSortAll(new S32AscendingQuickSort(selectPivot));
        fuzzSortAll(new S32DescendingQuickSort(selectPivot));
        fuzzSortAll(new QuickSort<>(arithmetic, selectPivot));
    }

    private void fuzzSortRange(@NotNull Sort<int[]> algorithm) {
        int[] expected = I32SlotArrayAllocator.EMPTY_ARRAY;
        int[] actual = I32SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int mainLength = fc.nextInt(2, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                expected = fc.nextIntArray(leftLength + mainLength + rightLength);
                actual = expected.clone();
                Arrays.sort(expected, leftLength, leftLength + mainLength);
                algorithm.sort(actual, leftLength, leftLength + mainLength);
                if (algorithm.isDescending()) {
                    I32SlotArrayAllocator.INSTANCE.reverse(expected, leftLength, leftLength + mainLength);
                    Assert.assertTrue(S32Arithmetic.INSTANCE.isDescending(actual, leftLength, leftLength + mainLength));
                } else {
                    Assert.assertTrue(S32Arithmetic.INSTANCE.isAscending(actual, leftLength, leftLength + mainLength));
                }
                Assert.assertArrayEquals(expected, actual);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    @Test
    public void fuzzSortRange() {
        final S32Arithmetic arithmetic = S32Arithmetic.INSTANCE;
        fuzzSortRange(S32AscendingBubbleSort.INSTANCE);
        fuzzSortRange(S32DescendingBubbleSort.INSTANCE);
        fuzzSortRange(new BubbleSort<>(arithmetic));
        fuzzSortRange(S32AscendingInsertionSort.INSTANCE);
        fuzzSortRange(S32DescendingInsertionSort.INSTANCE);
        fuzzSortRange(new InsertionSort<>(arithmetic));
        fuzzSortRange(S32AscendingSelectionSort.INSTANCE);
        fuzzSortRange(S32DescendingSelectionSort.INSTANCE);
        fuzzSortRange(new SelectionSort<>(arithmetic));
        fuzzSortRange(S32AscendingShellSort.INSTANCE);
        fuzzSortRange(S32DescendingShellSort.INSTANCE);
        fuzzSortRange(new ShellSort<>(arithmetic));
        fuzzSortRange(S32AscendingHeapSort.INSTANCE);
        fuzzSortRange(S32DescendingHeapSort.INSTANCE);
        fuzzSortRange(new HeapSort<>(arithmetic));
        fuzzSortRange(new S32AscendingMergeSort(false));
        fuzzSortRange(new S32DescendingMergeSort(false));
        fuzzSortRange(new MergeSort<>(arithmetic, false));
        fuzzSortRange(new S32AscendingMergeSort(true));
        fuzzSortRange(new S32DescendingMergeSort(true));
        fuzzSortRange(new MergeSort<>(arithmetic, true));
        fuzzSortRange(new S32AscendingCountSort(false));
        fuzzSortRange(new S32DescendingCountSort(false));
        fuzzSortRange(new S32AscendingCountSort(true));
        fuzzSortRange(new S32DescendingCountSort(true));
        fuzzSortRange(new S32AscendingTournamentSort(false));
        fuzzSortRange(new S32DescendingTournamentSort(false));
        fuzzSortRange(new S32AscendingTournamentSort(true));
        fuzzSortRange(new S32DescendingTournamentSort(true));
        fuzzSortRange(new S32AscendingRadixSort(10));
        fuzzSortRange(new S32DescendingRadixSort(10));
        fuzzSortRange(new S32AscendingBucketSort(S32AscendingShellSort.INSTANCE, -1000, 1000, 36));
        fuzzSortRange(new S32DescendingBucketSort(S32DescendingShellSort.INSTANCE, -1000, 1000, 36));
        SelectPivot<S32, int[]> selectPivot = new SelectFirstAsPivot<>(arithmetic);
        fuzzSortRange(new S32AscendingQuickSort(selectPivot));
        fuzzSortRange(new S32DescendingQuickSort(selectPivot));
        fuzzSortRange(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectTwoMeanAsPivot<>(arithmetic, arithmetic, RoundingMode.DOWN);
        fuzzSortAll(new S32AscendingQuickSort(selectPivot));
        fuzzSortAll(new S32DescendingQuickSort(selectPivot));
        fuzzSortRange(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectThreeMedianAsPivot<>(arithmetic);
        fuzzSortRange(new S32AscendingQuickSort(selectPivot));
        fuzzSortRange(new S32DescendingQuickSort(selectPivot));
        fuzzSortRange(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectMedianOfMedianAsPivot<>(arithmetic);
        fuzzSortRange(new S32AscendingQuickSort(selectPivot));
        fuzzSortRange(new S32DescendingQuickSort(selectPivot));
        fuzzSortRange(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectRangeCenterAsPivot<>(arithmetic, arithmetic, RoundingMode.DOWN);
        fuzzSortAll(new S32AscendingQuickSort(selectPivot));
        fuzzSortAll(new S32DescendingQuickSort(selectPivot));
        fuzzSortRange(new QuickSort<>(arithmetic, selectPivot));
        selectPivot = new SelectRandomAsPivot<>(arithmetic, fc);
        fuzzSortRange(new S32AscendingQuickSort(selectPivot));
        fuzzSortRange(new S32DescendingQuickSort(selectPivot));
        fuzzSortRange(new QuickSort<>(arithmetic, selectPivot));
    }
}
