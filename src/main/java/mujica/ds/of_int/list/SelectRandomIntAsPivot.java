package mujica.ds.of_int.list;

import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/28")
public class SelectRandomIntAsPivot implements IntPivotSelector {

    @NotNull
    final RandomContext rc;

    public SelectRandomIntAsPivot(@NotNull RandomContext rc) {
        super();
        this.rc = rc;
    }

    @Override
    public int select(@NotNull int[] array, int startIndex, int endIndex) {
        return array[rc.nextInt(startIndex, endIndex)];
    }
}
