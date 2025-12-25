package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/19.
 */
@CodeHistory(date = "2025/12/19")
public class SelectFirstIntAsPivot implements IntPivotSelector {

    public static final IntPivotSelector INSTANCE = new SelectFirstIntAsPivot();

    @Override
    public int select(@NotNull int[] array, int startIndex, int endIndex) {
        return array[startIndex];
    }
}
