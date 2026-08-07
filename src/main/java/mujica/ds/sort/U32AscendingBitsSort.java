package mujica.ds.sort;

import mujica.ds.i32.CopyOnResizeI32SlotList;
import mujica.ds.i32.I32;
import mujica.ds.i32.list.PublicIntList;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/7", name = "IntAscendingBitsSort")
@CodeHistory(date = "2026/8/4")
@Name(value = "32位无符号整数递增位压缩排序", language = "zh")
public class U32AscendingBitsSort implements Sort<int[]> {

    final int bitCount;

    @NotNull
    final CopyOnResizeI32SlotList[] buckets;

    @NotNull
    final I32 slot;

    public U32AscendingBitsSort(int bitCount) {
        super();
        if (!(0 < bitCount && bitCount < Short.SIZE)) {
            throw new IllegalArgumentException();
        }
        this.bitCount = bitCount;
        final int bucketCount = 1 << bitCount;
        final CopyOnResizeI32SlotList[] buckets = new CopyOnResizeI32SlotList[bucketCount];
        for (int index = 0; index < bucketCount; index++) {
            buckets[index] = new CopyOnResizeI32SlotList();
        }
        this.buckets = buckets;
        this.slot = new I32();
    }

    @Override
    public boolean isDescending() {
        return false;
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
