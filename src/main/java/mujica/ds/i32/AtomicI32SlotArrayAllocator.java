package mujica.ds.i32;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

@CodeHistory(date = "2026/7/30")
public class AtomicI32SlotArrayAllocator implements SlotArrayAllocator<AtomicInteger, AtomicIntegerArray> {

    @NotNull
    @Override
    public AtomicInteger newSlot() {
        return new AtomicInteger();
    }

    @Override
    public void move(@NotNull AtomicInteger src, @NotNull AtomicInteger dst) {
        dst.set(src.get());
    }

    @NotNull
    @Override
    public AtomicInteger cloneSlot(@NotNull AtomicInteger original) {
        return new AtomicInteger(original.get());
    }

    @NotNull
    @Override
    public AtomicIntegerArray newArray(int length) {
        return new AtomicIntegerArray(length);
    }

    @Override
    public void load(@NotNull AtomicIntegerArray array, int index, @NotNull AtomicInteger slot) {
        slot.set(array.get(index));
    }

    @Override
    public void store(@NotNull AtomicIntegerArray array, int index, @NotNull AtomicInteger slot) {
        array.set(index, slot.get());
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = i32, slotType = AtomicInteger, arrayType = AtomicIntegerArray]";
    }
}
