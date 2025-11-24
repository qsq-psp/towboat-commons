package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/24")
public abstract class Ordering<T> implements OrderingConstants {

    public abstract int orderingFlags(@NotNull T array);

    public boolean isAscending(@NotNull T array) {
        return (orderingFlags(array) & STRICT_DESCENDING) == 0;
    }

    public boolean isDescending(@NotNull T array) {
        return (orderingFlags(array) & STRICT_ASCENDING) == 0;
    }

    public boolean isStrictAscending(@NotNull T array) {
        final int flags = orderingFlags(array);
        return (flags & STRICT_ASCENDING) == flags;
    }

    public boolean isStrictDescending(@NotNull T array) {
        final int flags = orderingFlags(array);
        return (flags & STRICT_DESCENDING) == flags;
    }
}
