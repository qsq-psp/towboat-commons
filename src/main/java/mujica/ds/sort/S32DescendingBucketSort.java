package mujica.ds.sort;

import mujica.ds.i32.CopyOnResizeI32SlotList;
import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/6", name = "IntDescendingBucketSort")
@CodeHistory(date = "2026/8/3")
@Name(value = "32位有符号整数递减桶排序", language = "zh")
public class S32DescendingBucketSort implements Sort<int[]> {

    @NotNull
    final Sort<int[]> next;

    final int start, step;

    final CopyOnResizeI32SlotList[] buckets;

    public S32DescendingBucketSort(@NotNull Sort<int[]> next, int start, int step, int bucketCount) {
        super();
        if (this.isDescending() != next.isDescending()) {
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
        final CopyOnResizeI32SlotList[] buckets = new CopyOnResizeI32SlotList[bucketCount];
        for (int index = 0; index < bucketCount; index++) {
            buckets[index] = new CopyOnResizeI32SlotList();
        }
        this.buckets = buckets;
    }

    @Override
    public boolean isDescending() {
        return true;
    }

    @Override
    public boolean isStable() {
        return next.isStable();
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        for (CopyOnResizeI32SlotList bucket : buckets) {
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
            buckets[lastBucketIndex - bucketIndex].insertLast(array, index, index + 1); // one item
        }
        long operationCount = endIndex - startIndex;
        int copyIndex = startIndex;
        for (CopyOnResizeI32SlotList bucket : buckets) {
            operationCount += bucket.sort(next);
            bucket.getAll(array, copyIndex);
            copyIndex += bucket.getSize();
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull int[] target) {
        return sort(target, 0, target.length);
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
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
        endSlot.setI32(writeIndex);
        return operationCount;
    }
}
