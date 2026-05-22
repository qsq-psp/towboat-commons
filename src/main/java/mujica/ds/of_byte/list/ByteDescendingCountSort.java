package mujica.ds.of_byte.list;

import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.generic.list.SortingAlgorithm;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created on 2026/5/21.
 */
public class ByteDescendingCountSort extends SortingAlgorithm<byte[]> {

    private final int[] count = new int[0x100];

    public ByteDescendingCountSort() {
        super();
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.DESCENDING;
    }

    @Override
    public long sort(@NotNull byte[] array, int startIndex, int endIndex) {
        Arrays.fill(count, 0);
        for (int index = startIndex; index < endIndex; index++) {
            count[0xff & array[index]]++;
        }
        {
            int index = startIndex;
            for (int value = 0x100 - 1; value >= 0; value--) {
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
