package mujica.ds.of_long.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/5/14.
 */
@CodeHistory(date = "2026/5/14")
public class SelectLongRangeCenterAsPivot implements LongPivotSelector {

    @Override
    public long select(@NotNull long[] array, int startIndex, int endIndex) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (int index = startIndex; index < endIndex; index++) {
            long value = array[index];
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        return ((min + max) >>> 1);
    }
}
