package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i32.*;
import mujica.ds.i64.I64SlotArrayAllocator;
import mujica.ds.i64.S64;
import mujica.ds.i64.S64Arithmetic;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;

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

    private void fuzzSortRange(@NotNull Sort<long[]> algorithm) {
        long[] expected = I64SlotArrayAllocator.EMPTY_ARRAY;
        long[] actual = I64SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int mainLength = fc.nextInt(2, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                expected = fc.nextLongArray(leftLength + mainLength + rightLength);
                actual = expected.clone();
                Arrays.sort(expected, leftLength, leftLength + mainLength);
                algorithm.sort(actual, leftLength, leftLength + mainLength);
                if (algorithm.isDescending()) {
                    I64SlotArrayAllocator.INSTANCE.reverse(expected, leftLength, leftLength + mainLength);
                    Assert.assertTrue(S64Arithmetic.INSTANCE.isDescending(actual, leftLength, leftLength + mainLength));
                } else {
                    Assert.assertTrue(S64Arithmetic.INSTANCE.isAscending(actual, leftLength, leftLength + mainLength));
                }
                Assert.assertArrayEquals(expected, actual);
            }
        } catch (AssertionError e) {
            System.out.println("expected " + Arrays.toString(expected));
            System.out.println("actual " + Arrays.toString(actual));
            throw e;
        }
    }

    private void fuzzSortPart(@NotNull Sort<long[]> algorithm) {
        long[] expected = I64SlotArrayAllocator.EMPTY_ARRAY;
        long[] actual = I64SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int outLength = fc.nextInt(1, MAIN_LENGTH);
                int inLength = fc.nextInt(1, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                expected = fc.nextLongArray(leftLength + outLength + inLength + rightLength);
                actual = expected.clone();
                Arrays.sort(expected, leftLength, leftLength + outLength + inLength);
                algorithm.sortPart(actual, leftLength, leftLength + outLength, leftLength + outLength + inLength);
                if (algorithm.isDescending()) {
                    I64SlotArrayAllocator.INSTANCE.reverse(expected, leftLength, leftLength + outLength + inLength);
                    Assert.assertTrue(S64Arithmetic.INSTANCE.isDescending(actual, leftLength, leftLength + outLength));
                } else {
                    Assert.assertTrue(S64Arithmetic.INSTANCE.isAscending(actual, leftLength, leftLength + outLength));
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

    private void fuzzSortUnique(@NotNull Sort<long[]> algorithm) {
        long[] actual = I64SlotArrayAllocator.EMPTY_ARRAY;
        final HashSet<Long> set = new HashSet<>();
        final I32Slot endSlot = new I32();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int mainLength = fc.nextInt(2, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                actual = fc.nextLongArray(leftLength + mainLength + rightLength);
                for (int index = leftLength; index < leftLength + mainLength; index++) {
                    set.add(actual[index]);
                }
                endSlot.setI32(leftLength + mainLength);
                algorithm.sortUnique(actual, leftLength, endSlot);
                if (algorithm.isDescending()) {
                    Assert.assertTrue(S64Arithmetic.INSTANCE.isStrictDescending(actual, leftLength, endSlot.getI32()));
                } else {
                    Assert.assertTrue(S64Arithmetic.INSTANCE.isStrictAscending(actual, leftLength, endSlot.getI32()));
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

    private void sort(@NotNull Sort<long[]> algorithm) {
        fuzzSortAll(algorithm);
        fuzzSortRange(algorithm);
        fuzzSortPart(algorithm);
        fuzzSortUnique(algorithm);
    }

    private void quickSort(@NotNull SlotArrayComparator<S64, long[]> comparator, SelectPivot<S64, long[]> selectPivot) {
        sort(new QuickSort<>(comparator, selectPivot));
        sort(new BaselineQuickSort<>(comparator, selectPivot));
    }

    @Test
    public void sort() {
        final S64Arithmetic arithmetic = S64Arithmetic.INSTANCE;
        sort(new BubbleSort<>(arithmetic));
        sort(new InsertionSort<>(arithmetic));
        sort(new BinaryInsertionSort<>(arithmetic));
        sort(new SelectionSort<>(arithmetic));
        sort(new ShellSort<>(arithmetic));
        sort(new HeapSort<>(arithmetic));
        sort(new MergeSort<>(arithmetic, false));
        sort(new MergeSort<>(arithmetic, true));
        quickSort(arithmetic, new SelectFirstAsPivot<>(arithmetic));
        quickSort(arithmetic, new SelectTwoMeanAsPivot<>(arithmetic, arithmetic, RoundingMode.DOWN));
        quickSort(arithmetic, new SelectThreeMedianAsPivot<>(arithmetic));
        quickSort(arithmetic, new SelectMedianOfMedianAsPivot<>(arithmetic));
        quickSort(arithmetic, new SelectRangeCenterAsPivot<>(arithmetic, arithmetic, RoundingMode.DOWN));
        quickSort(arithmetic, new SelectRandomAsPivot<>(arithmetic, fc));
    }
}
