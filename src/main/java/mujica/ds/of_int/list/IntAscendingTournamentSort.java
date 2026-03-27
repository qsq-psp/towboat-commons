package mujica.ds.of_int.list;

import mujica.ds.SortingAlgorithm;
import mujica.ds.generic.list.MonotonicityDirection;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/2/7")
@ReferencePage(title = "锦标赛排序", href = "https://oi-wiki.org/basic/tournament-sort/")
public class IntAscendingTournamentSort extends SortingAlgorithm<int[]> {

    @NotNull
    private transient int[] auxiliary = PublicIntList.EMPTY.array;

    public IntAscendingTournamentSort() {
        super();
    }

    @NotNull
    private int[] getAuxiliary(int minLength) {
        int[] auxiliary = this.auxiliary;
        if (auxiliary.length < minLength) {
            auxiliary = new int[minLength];
            this.auxiliary = auxiliary;
        }
        return auxiliary;
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull int[] target, int startIndex, int endIndex) {
        return sortPart(target, startIndex, endIndex, endIndex);
    }

    @Override
    public long sortPart(@NotNull int[] target, int startIndex, int midIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        if (length >= 0x20000000) {
            throw new IndexOutOfBoundsException();
        }
        final int height = Integer.SIZE - Integer.numberOfLeadingZeros(length);
        final int offset = 1 << height;
        final int doubleOffset = offset << 1;
        final int[] arena = getAuxiliary(doubleOffset);
        Arrays.fill(arena, 0, doubleOffset, Integer.MAX_VALUE);
        System.arraycopy(target, startIndex, arena, offset, length);
        for (int leafIndex = offset + length; leafIndex >= offset; leafIndex--) {
            int value = arena[leafIndex];
            int currentIndex = leafIndex;
            while (currentIndex > 1) {
                int parentIndex = currentIndex >> 1;
                if (arena[parentIndex] < value) {
                    break;
                }
                arena[parentIndex] = value;
                currentIndex = parentIndex;
            }
        }
        int width = 0;
        for (int targetIndex = startIndex; targetIndex < midIndex; targetIndex++) {
            int currentIndex = 1;
            int value = arena[currentIndex];
            target[targetIndex] = value;
            if (value == Integer.MAX_VALUE) {
                continue;
            }
            width++;
            while (currentIndex < offset) {
                currentIndex <<= 1;
                if (value != arena[currentIndex]) {
                    currentIndex++;
                }
            }
            arena[currentIndex] = Integer.MAX_VALUE;
            while (currentIndex > 1) {
                int parentIndex = currentIndex >> 1;
                if ((currentIndex & 1) == 0) {
                    arena[parentIndex] = Math.min(arena[currentIndex], arena[currentIndex + 1]);
                } else {
                    arena[parentIndex] = Math.min(arena[currentIndex], arena[currentIndex - 1]);
                }
                currentIndex = parentIndex;
            }
        }
        return ((long) width) * height;
    }
}
