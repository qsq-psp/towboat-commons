package mujica.ds.sort;

import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/16", name = "BubbleSort")
@CodeHistory(date = "2025/10/25", name = "IntAscendingBubbleSort")
@CodeHistory(date = "2026/7/16")
@Name(value = "32位有符号整数递增冒泡排序", language = "zh")
public class S32AscendingBubbleSort implements Sort<int[]> {

    public static final S32AscendingBubbleSort INSTANCE = new S32AscendingBubbleSort();

    public S32AscendingBubbleSort() {
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
        while (startIndex < endIndex) {
            for (int index = startIndex + 1; index < endIndex; index++) {
                if (array[index - 1] > array[index]) {
                    int temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operationCount++;
                }
            }
            endIndex--;
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull int[] target) {
        return sort(target, 0, target.length);
    }

    @Override
    public long sortPart(@NotNull int[] array, int startIndex, int midIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < midIndex) {
            for (int index = endIndex - 1; index > startIndex; index--) {
                if (array[index - 1] > array[index]) {
                    int temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operationCount++;
                }
            }
            startIndex++;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
        long operationCount = 0L;
        int writeIndex = startIndex;
        int divIndex = startIndex;
        while (divIndex < endIndex) {
            for (int index = endIndex - 1; index > divIndex; index--) {
                if (array[index - 1] > array[index]) {
                    int temp = array[index - 1];
                    array[index - 1] = array[index];
                    array[index] = temp;
                    operationCount++;
                }
            }
            if (writeIndex == startIndex || array[writeIndex - 1] != array[divIndex]) {
                array[writeIndex++] = array[divIndex];
            }
            divIndex++;
        }
        endSlot.setI32(writeIndex);
        return operationCount;
    }
}
