package mujica.ds.i32;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/16")
public class SlotArrayAllocatorIndexedI32ForTest implements SlotArrayAllocator<IndexedI32ForTest, int[]> {

    public SlotArrayAllocatorIndexedI32ForTest() {
        super();
    }

    @NotNull
    @Override
    public IndexedI32ForTest newSlot() {
        return new IndexedI32ForTest();
    }

    @Override
    public void move(@NotNull IndexedI32ForTest src, @NotNull IndexedI32ForTest dst) {
        dst.setI32(src.getI32());
        dst.setIndex(src.getIndex());
    }

    @NotNull
    @Override
    public int[] newArray(int length) {
        if (length == 0) {
            return SlotArrayAllocatorI32.EMPTY_ARRAY;
        }
        return new int[length << 1];
    }

    @Override
    public int length(@NotNull int[] array) {
        return array.length >>> 1;
    }

    @Override
    public void load(@NotNull int[] array, int index, @NotNull IndexedI32ForTest slot) {
        index <<= 1;
        slot.setI32(array[index]);
        slot.setIndex(array[index + 1]);
    }

    @Override
    public void store(@NotNull int[] array, int index, @NotNull IndexedI32ForTest slot) {
        index <<= 1;
        array[index] = slot.getI32();
        array[index + 1] = slot.getIndex();
    }

    @Override
    public int[] cloneArray(@NotNull int[] original) {
        if (original.length == 0) {
            return SlotArrayAllocatorI32.EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = (i32, i32), slotType = IndexedI32ForTest, arrayType = int[]]";
    }
}
