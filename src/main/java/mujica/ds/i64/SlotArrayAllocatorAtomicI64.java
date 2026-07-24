package mujica.ds.i64;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

@CodeHistory(date = "2026/7/13")
public class SlotArrayAllocatorAtomicI64 implements SlotArrayAllocator<AtomicLong, AtomicLongArray> {

    @NotNull
    @Override
    public AtomicLong newSlot() {
        return new AtomicLong();
    }

    @Override
    public void move(@NotNull AtomicLong src, @NotNull AtomicLong dst) {
        dst.set(src.get());
    }

    @NotNull
    @Override
    public AtomicLong cloneSlot(@NotNull AtomicLong original) {
        return new AtomicLong(original.get());
    }

    @NotNull
    @Override
    public AtomicLongArray newArray(int length) {
        return new AtomicLongArray(length);
    }

    @Override
    public void load(@NotNull AtomicLongArray array, int index, @NotNull AtomicLong slot) {
        slot.set(array.get(index));
    }

    @Override
    public void store(@NotNull AtomicLongArray array, int index, @NotNull AtomicLong slot) {
        array.set(index, slot.get());
    }
}
