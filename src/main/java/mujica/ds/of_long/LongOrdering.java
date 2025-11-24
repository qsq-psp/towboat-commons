package mujica.ds.of_long;

import mujica.ds.Ordering;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/20")
public class LongOrdering extends Ordering<long[]> {

    public static final LongOrdering INSTANCE = new LongOrdering();

    @Override
    public int orderingFlags(@NotNull long[] array) {
        final int length = array.length;
        if (length < 2) {
            return 0;
        }
        int flags = 0;
        long v0 = array[0];
        for (int index = 1; index < length; index++) {
            long v1 = array[index];
            if (v0 < v1) {
                flags |= STRICT_ASCENDING;
            } else if (v0 > v1) {
                flags |= STRICT_DESCENDING;
            } else {
                flags |= EQUAL;
            }
            v0 = v1;
        }
        return flags;
    }
}
