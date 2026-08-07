package mujica.ds.sort;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/25", name = "ShellSort")
@CodeHistory(date = "2025/11/6", name = "IntAscendingShellSort")
@CodeHistory(date = "2026/7/30")
@Name(value = "32位有符号整数递增希尔排序", language = "zh")
public class S32AscendingShellSort implements Sort<int[]> {

    public static final S32AscendingShellSort INSTANCE = new S32AscendingShellSort();

    public S32AscendingShellSort() {
        super();
    }

    @Override
    public boolean isDescending() {
        return false;
    }

    private long sort(@NotNull int[] array, int startIndex, int endIndex, int step) {
        if (startIndex + step >= endIndex) {
            return 0L;
        }
        long operationCount = sort(array, startIndex, endIndex, step * 3 + 1);
        for (int i = startIndex + step; i < endIndex; i++) {
            int j = i;
            int value = array[j];
            while (true) {
                int index2 = j - step;
                if (index2 >= startIndex && array[index2] > value) {
                    array[j] = array[j - step];
                    j -= step;
                    operationCount++;
                } else {
                    break;
                }
            }
            array[j] = value; // insertion
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull int[] array, int startIndex, int endIndex) {
        return sort(array, startIndex, endIndex, 1);
    }

    @Override
    public long sort(@NotNull int[] target) {
        return sort(target, 0, target.length);
    }
}
