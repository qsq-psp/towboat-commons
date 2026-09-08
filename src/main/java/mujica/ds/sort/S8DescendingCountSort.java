package mujica.ds.sort;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/9/5")
@Name(value = "8位有符号整数递减计数排序", language = "zh")
public class S8DescendingCountSort implements Sort<byte[]> {

    final int[] countArray = new int[1 << Byte.SIZE];

    public S8DescendingCountSort() {
        super();
    }

    @Override
    public boolean isDescending() {
        return true;
    }

    @Override
    public long sort(@NotNull byte[] dataArray, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        Arrays.fill(countArray, 0);
        for (int index = startIndex; index < endIndex; index++) {
            countArray[0xff & dataArray[index]]++;
        }
        for (int index = Byte.MAX_VALUE; index >= 0; index--) {
            for (int remain = countArray[index]; remain > 0; remain--) {
                dataArray[startIndex++] = (byte) index;
            }
        }
        for (int index = (1 << Byte.SIZE) - 1; index >= (0xff & Byte.MIN_VALUE); index--) {
            for (int remain = countArray[index]; remain > 0; remain--) {
                dataArray[startIndex++] = (byte) index;
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
