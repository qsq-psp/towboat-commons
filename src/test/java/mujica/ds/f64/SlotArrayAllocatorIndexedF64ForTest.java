package mujica.ds.f64;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/7/17")
public class SlotArrayAllocatorIndexedF64ForTest implements SlotArrayAllocator<IndexedF64ForTest, IndexedF64ForTest[]> {

    public SlotArrayAllocatorIndexedF64ForTest() {
        super();
    }

    @NotNull
    @Override
    public IndexedF64ForTest newSlot() {
        return new IndexedF64ForTest();
    }

    @Override
    public void move(@NotNull IndexedF64ForTest src, @NotNull IndexedF64ForTest dst) {
        dst.setF64(src.getF64());
        dst.setIndex(src.getIndex());
    }

    @NotNull
    @Override
    public IndexedF64ForTest[] newArray(int length) {
        final IndexedF64ForTest[] array = new IndexedF64ForTest[length];
        for (int index = 0; index < length; index++) {
            array[index] = new IndexedF64ForTest();
        }
        return array;
    }

    @Override
    public void load(@NotNull IndexedF64ForTest[] array, int index, @NotNull IndexedF64ForTest slot) {
        move(array[index], slot);
    }

    @Override
    public void store(@NotNull IndexedF64ForTest[] array, int index, @NotNull IndexedF64ForTest slot) {
        move(slot, array[index]);
    }

    @Override
    public IndexedF64ForTest[] cloneArray(@NotNull IndexedF64ForTest[] original) {
        return Arrays.copyOf(original, original.length);
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = (f64, i32), slotType = IndexedF64ForTest, arrayType = IndexedF64ForTest[]]";
    }
}
