package mujica.ds.of_long.list;

import mujica.math.algebra.discrete.CastToZero;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/24")
public class SelectTwoLongMeanAsPivot implements LongPivotSelector {

    public static final LongPivotSelector INSTANCE = new SelectTwoLongMeanAsPivot();

    @Override
    public long select(@NotNull long[] array, int startIndex, int endIndex) {
        if (endIndex - startIndex >= 2) {
            return CastToZero.INSTANCE.mean(array[startIndex], array[startIndex + 1]);
        } else {
            return array[startIndex];
        }
    }
}
