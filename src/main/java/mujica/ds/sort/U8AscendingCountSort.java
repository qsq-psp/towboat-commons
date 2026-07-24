package mujica.ds.sort;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/24")
@Name(value = "8位无符号整数递增计数排序", language = "zh")
public class U8AscendingCountSort implements Sort<byte[]> {

    final int[] count = new int[1 << Byte.SIZE];

    public U8AscendingCountSort() {
        super();
    }

    @Override
    public long sort(@NotNull byte[] array, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        Arrays.fill(count, 0);
        for (int index = startIndex; index < endIndex; index++) {
            count[0xff & array[index]]++;
        }
        for (int index = 0; index < (1 << Byte.SIZE); index++) {
            for (int remain = count[index]; remain > 0; remain--) {
                array[startIndex++] = (byte) index;
            }
        }
        assert startIndex == endIndex;
        return length;
    }

    @Override
    public long sort(@NotNull byte[] target) {
        return sort(target, 0, target.length);
    }
}
