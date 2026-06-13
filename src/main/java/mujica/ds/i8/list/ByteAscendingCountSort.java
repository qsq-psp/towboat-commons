package mujica.ds.i8.list;

import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.generic.list.SortingAlgorithm;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created on 2026/5/21.
 */
@CodeHistory(date = "2026/5/21")
public class ByteAscendingCountSort extends SortingAlgorithm<byte[]> {

    private final int[] count = new int[1 << Byte.SIZE];

    public ByteAscendingCountSort() {
        super();
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull byte[] array, int startIndex, int endIndex) {
        Arrays.fill(count, 0);
        for (int index = startIndex; index < endIndex; index++) {
            count[0xff & array[index]]++;
        }
        {
            int index = startIndex;
            for (int value = 0; value < (1 << Byte.SIZE); value++) {
                int remain = count[value];
                while (remain > 0) {
                    array[index++] = (byte) value;
                    remain--;
                }
            }
            assert index == endIndex;
        }
        return endIndex - startIndex;
    }
}
