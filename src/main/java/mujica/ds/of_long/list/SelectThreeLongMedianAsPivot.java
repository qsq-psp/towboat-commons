package mujica.ds.of_long.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/25")
public class SelectThreeLongMedianAsPivot implements LongPivotSelector {

    @Override
    @SuppressWarnings("ManualMinMaxCalculation")
    public long select(@NotNull long[] array, int startIndex, int endIndex) {
        if (endIndex - startIndex >= 3) {
            long a = array[startIndex];
            long b = array[startIndex + 1];
            long c = array[startIndex + 2];
            if (a <= b) {
                if (b <= c) {
                    return b;
                } else if (c <= a) {
                    return a;
                } else {
                    return c;
                }
            } else {
                if (a < c) {
                    return a;
                } else if (c < b) {
                    return b;
                } else {
                    return c;
                }
            }
        } else {
            return array[startIndex];
        }
    }
}
