package mujica.ds.sort;

import mujica.ds.i32.list.PublicIntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/2/7", name = "IntAscendingTournamentSort")
@CodeHistory(date = "2026/7/29")
public class S32AscendingTournamentSort implements Sort<int[]> {

    transient int[] auxiliaryArray;

    public S32AscendingTournamentSort(boolean shareAuxiliary) {
        super();
        if (shareAuxiliary) {
            auxiliaryArray = PublicIntList.EMPTY.array;
        }
    }

    @NotNull
    int[] getAuxiliaryArray(int minLength) {
        int[] array = auxiliaryArray;
        if (array != null) {
            if (array.length < minLength) {
                array = new int[minLength];
                auxiliaryArray = array;
            }
            return array;
        }
        return new int[minLength];
    }

    @Override
    public boolean isDescending() {
        return false;
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
        final int[] arena = getAuxiliaryArray(doubleOffset);
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
