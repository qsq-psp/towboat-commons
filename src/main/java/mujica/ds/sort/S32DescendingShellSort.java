package mujica.ds.sort;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/25", name = "ShellSort")
@CodeHistory(date = "2025/11/6", name = "IntDescendingShellSort")
@CodeHistory(date = "2026/7/30")
@Name(value = "32位有符号整数递减希尔排序", language = "zh")
public class S32DescendingShellSort implements Sort<int[]> {

    public static final S32DescendingShellSort INSTANCE = new S32DescendingShellSort();

    public S32DescendingShellSort() {
        super();
    }

    @Override
    public boolean isDescending() {
        return true;
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
                int k = j - step;
                if (k >= startIndex && array[k] < value) {
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
}
