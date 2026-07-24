package mujica.ds.sort;

import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/21", name = "InsertionSort")
@CodeHistory(date = "2025/10/30", name = "IntDescendingInsertionSort")
@CodeHistory(date = "2026/7/20")
@Name(value = "32位有符号整数递减插入排序", language = "zh")
public class S32DescendingInsertionSort implements Sort<int[]> {

    public static final S32DescendingInsertionSort INSTANCE = new S32DescendingInsertionSort();

    public S32DescendingInsertionSort() {
        super();
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
        long operationCount = 0L;
        int readIndex = startIndex;
        while (readIndex < endIndex) {
            int key = array[readIndex];
            int writeIndex = readIndex - 1;
            while (writeIndex >= startIndex && array[writeIndex] < key) {
                array[writeIndex + 1] = array[writeIndex];
                writeIndex--;
                operationCount++;
            }
            array[writeIndex + 1] = key;
            readIndex++;
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
        long operationCount = 0L;
        int writeIndex = startIndex;
        LABEL:
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            int key = array[readIndex];
            for (int index = writeIndex - 1; index >= startIndex; index--) {
                if (array[index] >= key) {
                    if (array[index] > key) {
                        index++;
                        int length = writeIndex - index;
                        System.arraycopy(array, index, array, index + 1, length);
                        array[index] = key;
                        writeIndex++;
                        operationCount += length;
                    }
                    continue LABEL;
                }
            }
            System.arraycopy(array, startIndex, array, startIndex + 1, writeIndex - startIndex);
            array[startIndex] = key;
            writeIndex++;
            operationCount += writeIndex - startIndex;
        }
        endSlot.setI32(writeIndex);
        return operationCount;
    }
}
