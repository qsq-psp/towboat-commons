package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/4")
@ReferencePage(title = "基数排序", href = "https://oi-wiki.org/basic/radix-sort/")
public class IntAscendingRadixSort extends SortingAlgorithm<int[]> {

    private final int modulo;

    @NotNull
    private transient IntList[] buckets0;

    @NotNull
    private transient IntList[] buckets1;

    public IntAscendingRadixSort(int modulo) {
        super();
        if (modulo < 3) {
            throw new IllegalArgumentException();
        }
        this.modulo = modulo;
        final IntList[] buckets = new IntList[modulo];
        for (int index = 0; index < modulo; index++) {
            buckets[index] = new CopyOnResizeIntList(null);
        }
        this.buckets0 = buckets;
        this.buckets1 = new IntList[modulo];
    }

    @Override
    @NotNull
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public boolean stable() {
        return true;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        int round = 0;
        int step = 1;
        while (true) {
            for (IntList bucket : buckets0) {
                bucket.clear();
            }
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int index = startIndex; index < endIndex; index++) {
                int number = array[index];
                int bucketNumber = Math.floorDiv(number, step);
                if (bucketNumber < min) {
                    min = bucketNumber;
                }
                if (bucketNumber > max) {
                    max = bucketNumber;
                }
                bucketNumber %= modulo;
                if (bucketNumber < 0) {
                    bucketNumber += modulo;
                }
                buckets0[bucketNumber].offerLast(number);
            }
            if (((long) max) - min < modulo) {
                min %= modulo;
                if (min < 0) {
                    min += modulo;
                }
                max %= modulo;
                if (max < 0) {
                    max += modulo;
                }
                if (max < min) {
                    max++;
                    System.arraycopy(buckets0, 0, buckets1, modulo - max, max);
                    System.arraycopy(buckets0, max, buckets1, 0, modulo - max);
                    IntList[] buckets = buckets0;
                    buckets0 = buckets1;
                    buckets1 = buckets;
                }
                step = 0;
            }
            int copyIndex = startIndex;
            for (IntList bucket : buckets0) {
                bucket.getAll(array, copyIndex);
                copyIndex += bucket.intLength();
            }
            round++;
            if (step == 0) {
                break;
            }
            step *= modulo;
        }
        return ((long) round) * length;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        if (endIndex - startIndex <= 1) {
            return 0L;
        }
        long operationCount = sort(array, startIndex, endIndex);
        int previous = array[startIndex++];
        int writeIndex = startIndex;
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            int current = array[readIndex];
            if (previous == current) {
                continue;
            }
            array[writeIndex++] = current;
            previous = current;
            operationCount++;
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
