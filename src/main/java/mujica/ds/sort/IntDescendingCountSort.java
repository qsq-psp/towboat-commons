package mujica.ds.sort;

import mujica.ds.any.list.MonotonicityDirection;
import mujica.ds.any.list.SortingAlgorithm;
import mujica.ds.i32.I32Slot;
import mujica.ds.i32.list.PublicIntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/5/25")
public class IntDescendingCountSort extends SortingAlgorithm<int[]> {

    private final int[] count = new int[1 << Byte.SIZE];

    private transient int[] shared;

    public IntDescendingCountSort(boolean hasShared) {
        super();
        if (hasShared) {
            shared = PublicIntList.EMPTY.array;
        }
    }

    @NotNull
    private int[] getShared(int minLength) {
        int[] array = shared;
        if (array != null) {
            if (array.length < minLength) {
                array = new int[minLength];
                shared = array;
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
    public boolean stable() {
        return true;
    }

    @Override
    public long sort(@NotNull int[] target, int startIndex, int endIndex) {
        final int length = endIndex - startIndex;
        if (length < 2) {
            return 0L;
        }
        final int[] temp = getShared(length);
        for (int shift = 0; shift < Integer.SIZE; shift += Byte.SIZE) {
            Arrays.fill(count, 0);
            for (int index = startIndex; index < endIndex; index++) {
                int unsigned = target[index] - Integer.MIN_VALUE;
                count[0xff & (unsigned >> shift)]++;
            }
            {
                int sum = 0;
                for (int masked = (1 << Byte.SIZE) - 1; masked >= 0; masked--) {
                    int oldSum = sum;
                    sum += count[masked];
                    count[masked] = oldSum;
                }
            }
            for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
                int signed = target[readIndex];
                int writeIndex = count[0xff & ((signed - Integer.MIN_VALUE) >> shift)]++;
                temp[writeIndex] = signed;
            }
            System.arraycopy(temp, 0, target, startIndex, length);
        }
        return 4L * length;
    }

    @Override
    public long sortUnique(@NotNull int[] array, int startIndex, @NotNull I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
        if (endIndex - startIndex <= 1) {
            return 0L;
        }
        long operationCount = sort(array, startIndex, endIndex);
        int previous = array[startIndex++];
        int writeIndex = startIndex;
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            int current = array[readIndex];
            if (previous == current) {
                continue;
            }
            array[writeIndex++] = current;
            previous = current;
            operationCount++;
        }
        endSlot.setI32(writeIndex);
        return operationCount;
    }
}
