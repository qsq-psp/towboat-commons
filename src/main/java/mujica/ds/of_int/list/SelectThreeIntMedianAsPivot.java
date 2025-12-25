package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/21.
 */
@CodeHistory(date = "2025/12/21")
public class SelectThreeIntMedianAsPivot implements IntPivotSelector {

    @Override
    @SuppressWarnings("ManualMinMaxCalculation")
    public int select(@NotNull int[] array, int startIndex, int endIndex) {
        if (endIndex - startIndex >= 3) {
            int a = array[startIndex];
            int b = array[startIndex + 1];
            int c = array[startIndex + 2];
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
