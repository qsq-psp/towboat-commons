package mujica.ds.of_long.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/19.
 */
@CodeHistory(date = "2026/4/19")
public class SelectLongMedianOfMedianAsPivot implements LongPivotSelector {

    @Override
    public long select(@NotNull long[] array, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length >= 9) {
            int nextLength = length / 3;
            return median(
                    select(array, startIndex, startIndex + nextLength),
                    select(array, startIndex + nextLength, endIndex - nextLength),
                    select(array, endIndex - nextLength, endIndex)
            );
        }
        if (length >= 3) {
            return median(
                    array[startIndex],
                    array[startIndex + 1],
                    array[endIndex - 1]
            );
        }
        return array[startIndex];
    }

    @SuppressWarnings("ManualMinMaxCalculation")
    private long median(long a, long b, long c) {
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
    }
}
