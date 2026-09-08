package mujica.ds.sort;

import mujica.ds.i32.CopyOnResizeI32SlotList;
import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/4", name = "IntAscendingRadixSort")
@CodeHistory(date = "2026/8/7")
@Name(value = "32位有符号整数递增基数排序", language = "zh")
public class S32AscendingRadixSort implements Sort<int[]> {

    private final int modulo;

    final CopyOnResizeI32SlotList[] buckets;

    public S32AscendingRadixSort(int modulo) {
        super();
        if (modulo < 3) {
            throw new IllegalArgumentException();
        }
        this.modulo = modulo;
        final CopyOnResizeI32SlotList[] buckets = new CopyOnResizeI32SlotList[modulo * 2];
        for (int index = 0; index < modulo; index++) {
            buckets[index] = new CopyOnResizeI32SlotList();
        }
        this.buckets = buckets;
    }

    @Override
    public boolean isDescending() {
        return false;
    }

    @Override
    public boolean isStable() {
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
            for (int bucketIndex = 0; bucketIndex < modulo; bucketIndex++) {
                buckets[bucketIndex].clear();
            }
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int arrayIndex = startIndex; arrayIndex < endIndex; arrayIndex++) {
                int number = array[arrayIndex];
                int bucketIndex = Math.floorDiv(number, step);
                if (bucketIndex < min) {
                    min = bucketIndex;
                }
                if (bucketIndex > max) {
                    max = bucketIndex;
                }
                bucketIndex %= modulo;
                if (bucketIndex < 0) {
                    bucketIndex += modulo;
                }
                buckets[bucketIndex].insertLast(array, arrayIndex, arrayIndex + 1); // one item
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
                    System.arraycopy(buckets, 0, buckets, modulo, max);
                    System.arraycopy(buckets, max, buckets, 0, modulo);
                }
                step = 0;
            }
            int copyIndex = startIndex;
            for (int bucketIndex = 0; bucketIndex < modulo; bucketIndex++) {
                CopyOnResizeI32SlotList bucket = buckets[bucketIndex];
                bucket.getAll(array, copyIndex);
                copyIndex += bucket.getSize();
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
