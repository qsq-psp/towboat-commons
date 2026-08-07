package mujica.ds.sort;

import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/2", name = "IntAscendingSelectionSort")
@CodeHistory(date = "2026/8/5")
@Name(value = "32位有符号整数递增选择排序", language = "zh")
public class S32AscendingSelectionSort implements Sort<int[]> {

    public static final S32AscendingSelectionSort INSTANCE = new S32AscendingSelectionSort();

    public S32AscendingSelectionSort() {
        super();
    }

    @Override
    public boolean isDescending() {
        return false;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < endIndex) {
            int selectedIndex = startIndex;
            int selectedValue = array[selectedIndex]; // select max
            for (int index = startIndex + 1; index < endIndex; index++) {
                int value = array[index];
                if (value > selectedValue) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            endIndex--;
            if (selectedIndex != endIndex) {
                array[selectedIndex] = array[endIndex];
                array[endIndex] = selectedValue;
            }
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
            int selectedIndex = startIndex;
            int selectedValue = array[selectedIndex]; // select min
            for (int index = startIndex + 1; index < endIndex; index++) {
                int value = array[index];
                if (value < selectedValue) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            if (selectedIndex != startIndex) {
                array[selectedIndex] = array[startIndex];
                array[startIndex] = selectedValue;
            }
            startIndex++;
        }
        return operationCount;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
        long operationCount = 0L;
        int readIndex = startIndex;
        int writeIndex = startIndex;
        while (readIndex < endIndex) {
            int selectedIndex = readIndex;
            int selectedValue = array[selectedIndex]; // select min
            for (int index = readIndex + 1; index < endIndex; index++) {
                int value = array[index];
                if (value < selectedValue) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            array[selectedIndex] = array[readIndex];
            if (writeIndex == startIndex || array[writeIndex - 1] != selectedValue) {
                array[writeIndex++] = selectedValue;
            }
            readIndex++;
        }
        endSlot.setI32(writeIndex);
        return operationCount;
    }
}
