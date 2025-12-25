package mujica.ds.of_int.list;

import mujica.math.algebra.discrete.CastToZero;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/19.
 */
@CodeHistory(date = "2025/12/19")
public class SelectTwoIntMeanAsPivot implements IntPivotSelector {

    public static final IntPivotSelector INSTANCE = new SelectTwoIntMeanAsPivot();

    @Override
    public int select(@NotNull int[] array, int startIndex, int endIndex) {
        if (endIndex - startIndex >= 2) {
            return CastToZero.INSTANCE.mean(array[startIndex], array[startIndex + 1]);
        } else {
            return array[startIndex];
        }
    }
}
