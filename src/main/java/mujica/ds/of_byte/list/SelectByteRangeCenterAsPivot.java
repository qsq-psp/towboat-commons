package mujica.ds.of_byte.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/19.
 */
@CodeHistory(date = "2026/4/19")
public class SelectByteRangeCenterAsPivot implements BytePivotSelector {

    @Override
    public byte select(@NotNull byte[] array, int startIndex, int endIndex) {
        int min = Byte.MAX_VALUE;
        int max = Byte.MIN_VALUE;
        for (int index = startIndex; index < endIndex; index++) {
            int value = array[index];
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        return (byte) ((min + max) >>> 1);
    }
}
