package mujica.ds.of_long.list;

import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.generic.list.SortingAlgorithm;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/4.
 */
public class LongAscendingTournamentSort extends SortingAlgorithm<long[]> {

    private transient long[] shared;

    public LongAscendingTournamentSort(boolean hasShared) {
        super();
        if (hasShared) {
            shared = new long[0]; // PublicLongList.EMPTY.array;
        }
    }

    @NotNull
    private long[] getShared(int minLength) {
        long[] array = this.shared;
        if (array != null) {
            if (array.length < minLength) {
                array = new long[minLength];
                this.shared = array;
            }
            return array;
        }
        return new long[minLength];
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull long[] target, int startIndex, int endIndex) {
        return 0;
    }
}
