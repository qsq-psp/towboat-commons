package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i32.*;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;

@CodeHistory(date = "2025/3/24", name = "SortingAlgorithmTest")
@CodeHistory(date = "2026/2/2", name = "IntSortingAlgorithmTest")
@CodeHistory(date = "2026/7/13")
public class S32SortTest {

    private static final int REPEAT_COUNT = 45;

    private static final int MAIN_LENGTH = 533;

    private static final int ASIDE_LENGTH = 21;

    private final FuzzyContext fc = new FuzzyContext();

    private void checkSortCardinal(@NotNull Sort<int[]> algorithm) {
        int[] actual = I32SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int length = fc.nextInt(2, MAIN_LENGTH);
                actual = fc.shuffledCardinal(length);
                algorithm.sort(actual);
                if (algorithm.isDescending()) {
                    for (int index = 0; index < length; index++) {
                        Assert.assertEquals(length - 1 - index, actual[index]);
                    }
                } else {
                    for (int index = 0; index < length; index++) {
                        Assert.assertEquals(index, actual[index]);
                    }
                }
            }
        } catch (AssertionError e) {
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void checkSortSame(@NotNull Sort<int[]> algorithm) {
        int[] actual = I32SlotArrayAllocator.EMPTY_ARRAY;
        final I32Slot endSlot = new I32();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int mainLength = fc.nextInt(2, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                actual = fc.nextIntArray(leftLength + mainLength + rightLength);
                int value = fc.nextInt();
                Arrays.fill(actual, leftLength, leftLength + mainLength, value);
                endSlot.setI32(leftLength + mainLength);
                algorithm.sortUnique(actual, leftLength, endSlot);
                Assert.assertEquals(leftLength + 1, endSlot.getI32());
                Assert.assertEquals(value, actual[leftLength]);
            }
        } catch (AssertionError e) {
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void fuzzSortSorted(@NotNull Sort<int[]> algorithm) {
        int[] expected = I32SlotArrayAllocator.EMPTY_ARRAY;
        int[] actual = I32SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                expected = fc.nextIntArray(fc.nextInt(2, MAIN_LENGTH));
                Arrays.sort(expected);
                if (algorithm.isDescending()) {
                    I32SlotArrayAllocator.INSTANCE.reverse(expected);
                }
                actual = expected.clone();
                algorithm.sort(actual);
                Assert.assertArrayEquals(expected, actual);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

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

    private void fuzzSortPart(@NotNull Sort<int[]> algorithm) {
        int[] expected = I32SlotArrayAllocator.EMPTY_ARRAY;
        int[] actual = I32SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int outLength = fc.nextInt(1, MAIN_LENGTH);
                int inLength = fc.nextInt(1, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                expected = fc.nextIntArray(leftLength + outLength + inLength + rightLength);
                actual = expected.clone();
                Arrays.sort(expected, leftLength, leftLength + outLength + inLength);
                algorithm.sortPart(actual, leftLength, leftLength + outLength, leftLength + outLength + inLength);
                if (algorithm.isDescending()) {
                    I32SlotArrayAllocator.INSTANCE.reverse(expected, leftLength, leftLength + outLength + inLength);
                    Assert.assertTrue(S32Arithmetic.INSTANCE.isDescending(actual, leftLength, leftLength + outLength));
                } else {
                    Assert.assertTrue(S32Arithmetic.INSTANCE.isAscending(actual, leftLength, leftLength + outLength));
                }
                System.arraycopy(expected, leftLength + outLength, actual, leftLength + outLength, inLength);
                Assert.assertArrayEquals(expected, actual);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void fuzzSortUnique(@NotNull Sort<int[]> algorithm) {
        int[] actual = I32SlotArrayAllocator.EMPTY_ARRAY;
        final HashSet<Integer> set = new HashSet<>();
        final I32Slot endSlot = new I32();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int mainLength = fc.nextInt(2, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                actual = fc.nextIntArray(leftLength + mainLength + rightLength);
                for (int index = leftLength; index < leftLength + mainLength; index++) {
                    set.add(actual[index]);
                }
                endSlot.setI32(leftLength + mainLength);
                algorithm.sortUnique(actual, leftLength, endSlot);
                if (algorithm.isDescending()) {
                    Assert.assertTrue(S32Arithmetic.INSTANCE.isStrictDescending(actual, leftLength, endSlot.getI32()));
                } else {
                    Assert.assertTrue(S32Arithmetic.INSTANCE.isStrictAscending(actual, leftLength, endSlot.getI32()));
                }
                Assert.assertEquals(leftLength + set.size(), endSlot.getI32());
                for (int index = leftLength; index < endSlot.getI32(); index++) {
                    set.remove(actual[index]);
                }
                Assert.assertTrue(set.isEmpty());
            }
        } catch (AssertionError e) {
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void sort(@NotNull Sort<int[]> algorithm) {
        checkSortCardinal(algorithm);
        fuzzSortSorted(algorithm);
        checkSortSame(algorithm);
        fuzzSortAll(algorithm);
        fuzzSortRange(algorithm);
        fuzzSortPart(algorithm);
        fuzzSortUnique(algorithm);
    }

    private void quickSort(@NotNull SlotArrayComparator<S32, int[]> comparator, SelectPivot<S32, int[]> selectPivot) {
        sort(new S32AscendingQuickSort(selectPivot));
        sort(new S32DescendingQuickSort(selectPivot));
        sort(new QuickSort<>(comparator, selectPivot));
        sort(new BaselineQuickSort<>(comparator, selectPivot));
    }

    @Test
    public void sort() {
        final S32Arithmetic arithmetic = S32Arithmetic.INSTANCE;
        sort(S32AscendingBubbleSort.INSTANCE);
        sort(S32DescendingBubbleSort.INSTANCE);
        sort(new BubbleSort<>(arithmetic));
        sort(S32AscendingInsertionSort.INSTANCE);
        sort(S32DescendingInsertionSort.INSTANCE);
        sort(new InsertionSort<>(arithmetic));
        sort(new BinaryInsertionSort<>(arithmetic));
        sort(S32AscendingSelectionSort.INSTANCE);
        sort(S32DescendingSelectionSort.INSTANCE);
        sort(new SelectionSort<>(arithmetic));
        sort(S32AscendingShellSort.INSTANCE);
        sort(S32DescendingShellSort.INSTANCE);
        sort(new ShellSort<>(arithmetic));
        sort(S32AscendingHeapSort.INSTANCE);
        sort(S32DescendingHeapSort.INSTANCE);
        sort(new HeapSort<>(arithmetic));
        sort(new S32AscendingMergeSort(false));
        sort(new S32DescendingMergeSort(false));
        sort(new MergeSort<>(arithmetic, false));
        sort(new S32AscendingMergeSort(true));
        sort(new S32DescendingMergeSort(true));
        sort(new MergeSort<>(arithmetic, true));
        sort(new GallopMergeSort<>(arithmetic, false));
        sort(new GallopMergeSort<>(arithmetic, true));
        sort(new S32AscendingCountSort(false));
        sort(new S32DescendingCountSort(false));
        sort(new S32AscendingCountSort(true));
        sort(new S32DescendingCountSort(true));
        sort(new S32AscendingTournamentSort(false));
        sort(new S32DescendingTournamentSort(false));
        sort(new S32AscendingTournamentSort(true));
        sort(new S32DescendingTournamentSort(true));
        sort(new S32AscendingRadixSort(10));
        sort(new S32DescendingRadixSort(10));
        sort(new S32AscendingBucketSort(S32AscendingShellSort.INSTANCE, -1000, 1000, 36));
        sort(new S32DescendingBucketSort(S32DescendingShellSort.INSTANCE, -1000, 1000, 36));
        quickSort(arithmetic, new SelectFirstAsPivot<>(arithmetic));
        quickSort(arithmetic, new SelectTwoMeanAsPivot<>(arithmetic, arithmetic, RoundingMode.DOWN));
        quickSort(arithmetic, new SelectThreeMedianAsPivot<>(arithmetic));
        quickSort(arithmetic, new SelectMedianOfMedianAsPivot<>(arithmetic));
        quickSort(arithmetic, new SelectRangeCenterAsPivot<>(arithmetic, arithmetic, RoundingMode.DOWN));
        quickSort(arithmetic, new SelectRandomAsPivot<>(arithmetic, fc));
    }
}
