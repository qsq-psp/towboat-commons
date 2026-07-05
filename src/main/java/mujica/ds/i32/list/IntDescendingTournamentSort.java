package mujica.ds.i32.list;

import mujica.ds.any.list.SortingAlgorithm;
import mujica.ds.any.list.MonotonicityDirection;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/2/8")
@ReferencePage(title = "锦标赛排序", href = "https://oi-wiki.org/basic/tournament-sort/")
public class IntDescendingTournamentSort extends SortingAlgorithm<int[]> {

    private transient int[] shared;

    public IntDescendingTournamentSort(boolean hasShared) {
        super();
        if (hasShared) {
            shared = PublicIntList.EMPTY.array;
        }
    }

    @NotNull
    private int[] getShared(int minLength) {
        int[] array = this.shared;
        if (array != null) {
            if (array.length < minLength) {
                array = new int[minLength];
                this.shared = array;
            }
            return array;
        }
        return new int[minLength];
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.DESCENDING;
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
        final int[] arena = getShared(doubleOffset);
        Arrays.fill(arena, 0, doubleOffset, Integer.MIN_VALUE);
        System.arraycopy(target, startIndex, arena, offset, length);
        for (int leafIndex = offset + length; leafIndex >= offset; leafIndex--) {
            int value = arena[leafIndex];
            int currentIndex = leafIndex;
            while (currentIndex > 1) {
                int parentIndex = currentIndex >> 1;
                if (arena[parentIndex] > value) {
                    break;
                }
                arena[parentIndex] = value;
                currentIndex = parentIndex;
            }
        }
        int width = 0;
        for (int targetIndex = startIndex; targetIndex < endIndex; targetIndex++) {
            int currentIndex = 1;
            int value = arena[currentIndex];
            target[targetIndex] = value;
            if (value == Integer.MIN_VALUE) {
                continue;
            }
            width++;
            while (currentIndex < offset) {
                currentIndex <<= 1;
                if (value != arena[currentIndex]) {
                    currentIndex++;
                }
            }
            arena[currentIndex] = Integer.MIN_VALUE;
            while (currentIndex > 1) {
                int parentIndex = currentIndex >> 1;
                if ((currentIndex & 1) == 0) {
                    arena[parentIndex] = Math.max(arena[currentIndex], arena[currentIndex + 1]);
                } else {
                    arena[parentIndex] = Math.max(arena[currentIndex], arena[currentIndex - 1]);
                }
                currentIndex = parentIndex;
            }
        }
        return ((long) width) * height;
    }
}
