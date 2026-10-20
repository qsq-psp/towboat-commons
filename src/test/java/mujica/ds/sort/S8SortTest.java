package mujica.ds.sort;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i32.I32;
import mujica.ds.i32.I32Slot;
import mujica.ds.i8.S8;
import mujica.ds.i8.I8SlotArrayAllocator;
import mujica.ds.i8.S8SlotArrayComparator;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

@CodeHistory(date = "2026/7/23")
public class S8SortTest {

    private static final int REPEAT_COUNT = 35;

    private static final int MAIN_LENGTH = 998;

    private static final int ASIDE_LENGTH = 74;

    private final FuzzyContext fc = new FuzzyContext();

    private void checkSortSame(@NotNull Sort<byte[]> algorithm) {
        byte[] actual = I8SlotArrayAllocator.EMPTY_ARRAY;
        final I32Slot endSlot = new I32();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int mainLength = fc.nextInt(2, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                actual = fc.nextByteArray(leftLength + mainLength + rightLength);
                byte value = fc.nextByte();
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

    private void fuzzSortSorted(@NotNull Sort<byte[]> algorithm) {
        byte[] expected = I8SlotArrayAllocator.EMPTY_ARRAY;
        byte[] actual = I8SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                expected = fc.nextByteArray(fc.nextInt(2, MAIN_LENGTH));
                Arrays.sort(expected);
                if (algorithm.isDescending()) {
                    I8SlotArrayAllocator.INSTANCE.reverse(expected);
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

    private void fuzzSortPart(@NotNull Sort<byte[]> algorithm) {
        byte[] expected = I8SlotArrayAllocator.EMPTY_ARRAY;
        byte[] actual = I8SlotArrayAllocator.EMPTY_ARRAY;
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int outLength = fc.nextInt(1, MAIN_LENGTH);
                int inLength = fc.nextInt(1, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                expected = fc.nextByteArray(leftLength + outLength + inLength + rightLength);
                actual = expected.clone();
                Arrays.sort(expected, leftLength, leftLength + outLength + inLength);
                algorithm.sortPart(actual, leftLength, leftLength + outLength, leftLength + outLength + inLength);
                if (algorithm.isDescending()) {
                    I8SlotArrayAllocator.INSTANCE.reverse(expected, leftLength, leftLength + outLength + inLength);
                    Assert.assertTrue(S8SlotArrayComparator.INSTANCE.isDescending(actual, leftLength, leftLength + outLength));
                } else {
                    Assert.assertTrue(S8SlotArrayComparator.INSTANCE.isAscending(actual, leftLength, leftLength + outLength));
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

    private void fuzzSortUnique(@NotNull Sort<byte[]> algorithm) {
        byte[] actual = I8SlotArrayAllocator.EMPTY_ARRAY;
        final HashSet<Byte> set = new HashSet<>();
        final I32Slot endSlot = new I32();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                int mainLength = fc.nextInt(2, MAIN_LENGTH);
                int leftLength = fc.nextInt(ASIDE_LENGTH);
                int rightLength = fc.nextInt(ASIDE_LENGTH);
                actual = fc.nextByteArray(leftLength + mainLength + rightLength);
                for (int index = leftLength; index < leftLength + mainLength; index++) {
                    set.add(actual[index]);
                }
                endSlot.setI32(leftLength + mainLength);
                algorithm.sortUnique(actual, leftLength, endSlot);
                if (algorithm.isDescending()) {
                    Assert.assertTrue(S8SlotArrayComparator.INSTANCE.isStrictDescending(actual, leftLength, endSlot.getI32()));
                } else {
                    Assert.assertTrue(S8SlotArrayComparator.INSTANCE.isStrictAscending(actual, leftLength, endSlot.getI32()));
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

    private void sort(@NotNull Sort<byte[]> algorithm) {
        fuzzSortSorted(algorithm);
        checkSortSame(algorithm);
        fuzzSortAll(algorithm);
        fuzzSortRange(algorithm);
        fuzzSortPart(algorithm);
        fuzzSortUnique(algorithm);
    }

    private void quickSort(@NotNull SlotArrayComparator<S8, byte[]> comparator, SelectPivot<S8, byte[]> selectPivot) {
        sort(new QuickSort<>(comparator, selectPivot));
        sort(new BaselineQuickSort<>(comparator, selectPivot));
    }

    @Test
    public void sort() {
        final S8SlotArrayComparator comparator = S8SlotArrayComparator.INSTANCE;
        sort(new BubbleSort<>(comparator));
        sort(new InsertionSort<>(comparator));
        sort(new BinaryInsertionSort<>(comparator));
        sort(new SelectionSort<>(comparator));
        sort(new ShellSort<>(comparator));
        sort(new HeapSort<>(comparator));
        sort(new MergeSort<>(comparator, false));
        sort(new MergeSort<>(comparator, true));
        sort(new S8AscendingCountSort());
        sort(new S8DescendingCountSort());
        quickSort(comparator, new SelectFirstAsPivot<>(comparator));
        quickSort(comparator, new SelectThreeMedianAsPivot<>(comparator));
        quickSort(comparator, new SelectMedianOfMedianAsPivot<>(comparator));
        quickSort(comparator, new SelectRandomAsPivot<>(comparator, fc));
    }
}
