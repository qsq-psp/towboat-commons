package mujica.ds.sort;

import mujica.ds.i32.I32Slot;
import mujica.ds.i32.list.PublicIntList;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/5/25", name = "IntDescendingCountSort")
@CodeHistory(date = "2026/7/25")
@Name(value = "32位有符号整数递减计数排序", language = "zh")
public class S32DescendingCountSort implements Sort<int[]> {

    private final int[] countArray = new int[1 << Byte.SIZE];

    private transient int[] auxiliaryArray;

    public S32DescendingCountSort(boolean shareAuxiliary) {
        super();
        if (shareAuxiliary) {
            auxiliaryArray = PublicIntList.EMPTY.array;
        }
    }

    @NotNull
    int[] getAuxiliaryArray(int minLength) {
        int[] array = auxiliaryArray;
        if (array != null) {
            if (array.length < minLength) {
                array = new int[minLength];
                auxiliaryArray = array;
            }
            return array;
        }
        return new int[minLength];
    }

    @Override
    public boolean isDescending() {
        return true;
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public long sort(@NotNull int[] target, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        final int[] temp = getAuxiliaryArray(length);
        for (int shift = 0; shift < Integer.SIZE; shift += Byte.SIZE) {
            Arrays.fill(countArray, 0);
            for (int index = startIndex; index < endIndex; index++) {
                int unsigned = target[index] - Integer.MIN_VALUE;
                countArray[0xff & (unsigned >> shift)]++;
            }
            {
                int sum = 0;
                for (int masked = (1 << Byte.SIZE) - 1; masked >= 0; masked--) {
                    int oldSum = sum;
                    sum += countArray[masked];
                    countArray[masked] = oldSum;
                }
            }
            for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
                int signed = target[readIndex];
                int writeIndex = countArray[0xff & ((signed - Integer.MIN_VALUE) >> shift)]++;
                temp[writeIndex] = signed;
            }
            System.arraycopy(temp, 0, target, startIndex, length);
        }
        return 4L * length;
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
