package mujica.ds.f32;

import mujica.ds.i32.SlotArrayAllocatorI32;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/23")
public class SlotArrayAllocatorIndexedF32ForTest implements SlotArrayAllocator<IndexedF32ForTest, int[]> {

    public SlotArrayAllocatorIndexedF32ForTest() {
        super();
    }

    @NotNull
    @Override
    public IndexedF32ForTest newSlot() {
        return new IndexedF32ForTest();
    }

    @Override
    public void move(@NotNull IndexedF32ForTest src, @NotNull IndexedF32ForTest dst) {
        dst.value = src.value;
        dst.index = src.index;
    }

    @NotNull
    @Override
    public int[] newArray(int length) {
        if (length < 0 || length > Integer.MAX_VALUE / 2) {
            throw new IllegalArgumentException();
        }
        if (length == 0) {
            return SlotArrayAllocatorI32.EMPTY_ARRAY;
        }
        return new int[length << 1]; // length * 2
    }

    @Override
    public void load(@NotNull int[] array, int index, @NotNull IndexedF32ForTest slot) {
        index <<= 1;
        slot.value = Float.intBitsToFloat(array[index]);
        slot.index = array[index + 1];
    }

    @Override
    public void store(@NotNull int[] array, int index, @NotNull IndexedF32ForTest slot) {
        index <<= 1;
        array[index] = Float.floatToRawIntBits(slot.value);
        array[index + 1] = slot.index;
    }

    @Override
    public int[] cloneArray(@NotNull int[] original) {
        return Arrays.copyOf(original, original.length);
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = (f32, i32), slotType = IndexedF32ForTest, arrayType = int[]]";
    }

    @CodeHistory(date = "2026/7/24")
    public static class OrderByValue extends SlotArrayAllocatorIndexedF32ForTest implements SlotArrayComparator<IndexedF32ForTest, int[]> {

        @Override
        public int compareSlot(@NotNull IndexedF32ForTest left, @NotNull IndexedF32ForTest right) {
            if (left.value < right.value) {
                return -1;
            }
            if (left.value > right.value) {
                return 1;
            }
            if (left.value == left.value) {
                if (right.value == right.value) {
                    return 0;
                } else {
                    return 1; // NaN is smaller
                }
            } else {
                if (right.value == right.value) {
                    return -1; // NaN is smaller
                } else {
                    return 0;
                }
            }
        }
    }

    @CodeHistory(date = "2026/7/24")
    public static class OrderByValueThenIndex extends SlotArrayAllocatorIndexedF32ForTest implements SlotArrayComparator<IndexedF32ForTest, int[]> {

        @Override
        public int compareSlot(@NotNull IndexedF32ForTest left, @NotNull IndexedF32ForTest right) {
            if (left.value < right.value) {
                return -1;
            }
            if (left.value > right.value) {
                return 1;
            }
            if (left.value == left.value) {
                if (right.value == right.value) {
                    return Integer.compare(left.index, right.index);
                } else {
                    return -1; // the rule changed; NaN is larger
                }
            } else {
                if (right.value == right.value) {
                    return 1; // the rule changed; NaN is larger
                } else {
                    return Integer.compare(left.index, right.index);
                }
            }
        }
    }
}
