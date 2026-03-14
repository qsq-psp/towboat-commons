package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/6")
@ReferencePage(title = "桶排序", href = "https://oi-wiki.org/basic/bucket-sort/")
public class IntDescendingBucketSort extends SortingAlgorithm<int[]> {

    @NotNull
    final SortingAlgorithm<int[]> next;

    final int start, step;

    final IntList[] buckets;

    public IntDescendingBucketSort(@NotNull SortingAlgorithm<int[]> next, int start, int step, int bucketCount) {
        super();
        if (this.monotonicity() != next.monotonicity()) {
            throw new IllegalArgumentException();
        }
        if (step <= 0 || bucketCount < 2) {
            throw new IllegalArgumentException();
        }
        final long end = start + ((long) step) * bucketCount;
        if (Integer.MAX_VALUE + 1L < end) {
            throw new IllegalArgumentException();
        }
        this.next = next;
        this.start = start;
        this.step = step;
        final IntList[] buckets = new IntList[bucketCount];
        for (int index = 0; index < bucketCount; index++) {
            buckets[index] = new CopyOnResizeIntList(null);
        }
        this.buckets = buckets;
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.DESCENDING;
    }

    @Override
    public boolean stable() {
        return next.stable();
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        for (IntList bucket : buckets) {
            bucket.clear();
        }
        final int lastBucketIndex = buckets.length - 1;
        for (int index = startIndex; index < endIndex; index++) {
            int value = array[index];
            int bucketIndex;
            if (value <= start) {
                bucketIndex = 0;
            } else {
                bucketIndex = value - start;
                if (bucketIndex <= 0) { // integer overflow
                    bucketIndex = (int) ((((long) value) - start) / step);
                } else {
                    bucketIndex /= step;
                }
                if (bucketIndex > lastBucketIndex) {
                    bucketIndex = lastBucketIndex;
                }
            }
            buckets[lastBucketIndex - bucketIndex].offerLast(value);
        }
        long operationCount = endIndex - startIndex;
        int copyIndex = startIndex;
        for (IntList bucket : buckets) {
            operationCount += bucket.sort(next);
            bucket.getAll(array, copyIndex);
            copyIndex += bucket.intLength();
        }
        return operationCount;
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
