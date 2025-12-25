package mujica.ds.of_double.list;

import mujica.ds.Ordering;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/25")
public class DoubleOrdering extends Ordering<double[]> {

    public static final DoubleOrdering INSTANCE = new DoubleOrdering();

    @Override
    public int orderingFlags(@NotNull double[] array) {
        final int length = array.length;
        if (length < 2) {
            return 0;
        }
        int flags = 0;
        for (int index = 0; index < length; index++) {
            double v0 = array[index];
            if (v0 != v0) {
                flags |= NAN;
                continue;
            }
            for (index++; index < length; index++) {
                double v1 = array[index];
                if (v1 != v1) {
                    index++;
                    continue;
                }
                if (v0 < v1) {
                    flags |= STRICT_ASCENDING;
                } else if (v0 > v1) {
                    flags |= STRICT_DESCENDING;
                } else {
                    flags |= EQUAL;
                }
                v0 = v1;
            }
        }
        return flags;
    }
}
