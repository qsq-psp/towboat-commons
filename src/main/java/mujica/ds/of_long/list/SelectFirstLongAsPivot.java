package mujica.ds.of_long.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/24")
public class SelectFirstLongAsPivot implements LongPivotSelector {

    public static final LongPivotSelector INSTANCE = new SelectFirstLongAsPivot();

    @Override
    public long select(@NotNull long[] array, int startIndex, int endIndex) {
        return array[startIndex];
    }
}
