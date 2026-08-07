package mujica.ds.sort;

import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/25")
@Name(value = "8位无符号整数递减计数排序", language = "zh")
public class U8DescendingCountSort implements Sort<byte[]> {

    final int[] countArray = new int[1 << Byte.SIZE];

    public U8DescendingCountSort() {
        super();
    }

    @Override
    public long sort(@NotNull byte[] array, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        Arrays.fill(countArray, 0);
        for (int index = startIndex; index < endIndex; index++) {
            countArray[0xff & array[index]]++;
        }
        for (int index = (1 << Byte.SIZE) - 1; index >= 0; index--) {
            for (int remain = countArray[index]; remain > 0; remain--) {
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

    @Override
    public long sortPart(@NotNull byte[] array, int startIndex, int midIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        Arrays.fill(countArray, 0);
        for (int index = startIndex; index < endIndex; index++) {
            countArray[0xff & array[index]]++;
        }
        for (int index = (1 << Byte.SIZE) - 1; index >= 0; index--) {
            int remain = countArray[index];
            if (startIndex + remain < midIndex) {
                while (remain > 0) {
                    array[startIndex++] = (byte) index;
                    remain--;
                }
            } else {
                while (startIndex < midIndex) {
                    array[startIndex++] = (byte) index;
                }
                break;
            }
        }
        assert startIndex == midIndex;
        return length;
    }

    @Override
    public long sortUnique(@NotNull byte[] array, int startIndex, @NotNull I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        Arrays.fill(countArray, 0);
        for (int index = startIndex; index < endIndex; index++) {
            countArray[0xff & array[index]] = 1;
        }
        for (int index = (1 << Byte.SIZE) - 1; index >= 0; index--) {
            if (countArray[index] != 0) {
                array[startIndex++] = (byte) index;
            }
        }
        assert startIndex <= endIndex;
        endSlot.setI32(startIndex);
        return length;
    }
}
