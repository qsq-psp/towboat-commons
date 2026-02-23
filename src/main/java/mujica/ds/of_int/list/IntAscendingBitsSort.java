package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/7")
public class IntAscendingBitsSort extends SortingAlgorithm<int[]> {

    final int bitCount;

    @NotNull
    transient IntList[] buckets;

    public IntAscendingBitsSort(int bitCount) {
        super();
        if (!(0 < bitCount && bitCount < Short.SIZE)) {
            throw new IllegalArgumentException();
        }
        this.bitCount = bitCount;
        final int bucketCount = 1 << bitCount;
        final IntList[] buckets = new IntList[bucketCount];
        for (int index = 0; index < bucketCount; index++) {
            buckets[index] = new CopyOnResizeIntList(null);
        }
        this.buckets = buckets;
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @NotNull
    private int[] getFreeBits(@NotNull int[] array, int startIndex, int endIndex) {
        int and = -1;
        int or = 0;
        for (int index = startIndex; index < endIndex; index++) {
            int value = array[index];
            and &= value;
            or |= value;
        }
        final int free = ~and & or;
        if (free == 0) {
            return PublicIntList.EMPTY.array;
        }
        final int[] freeBits = new int[Integer.bitCount(free)];
        int pointer = 0;
        for (int shift = 0; shift < Integer.SIZE; shift++) {
            int bit = 1 << shift;
            if ((free & bit) != 0) {
                freeBits[pointer++] = bit;
            }
        }
        return freeBits;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        return 0L;
    }
}
