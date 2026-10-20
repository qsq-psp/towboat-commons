package mujica.ds.i32;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2026/7/30")
public class PointI32SlotArrayAllocator implements SlotArrayAllocator<Point, int[]> {

    @NotNull
    @Override
    public Point newSlot() {
        return new Point();
    }

    @Override
    public void move(@NotNull Point src, @NotNull Point dst) {
        dst.x = src.x;
        dst.y = src.y;
    }

    @NotNull
    @Override
    public Point cloneSlot(@NotNull Point original) {
        return new Point(original);
    }

    @NotNull
    @Override
    public int[] newArray(int length) {
        if (length < 0) {
            throw new NegativeArraySizeException();
        }
        return new int[length << 1];
    }

    @Override
    public void load(@NotNull int[] array, int index, @NotNull Point slot) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        index <<= 1;
        slot.x = array[index];
        slot.y = array[index + 1];
    }

    @Override
    public void store(@NotNull int[] array, int index, @NotNull Point slot) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        index <<= 1;
        array[index] = slot.x;
        array[index + 1] = slot.y;
    }

    @Override
    @NotNull
    public String toString() {
        return "[dataType = i32*2, slotType = Point, arrayType = int[]]";
    }
}
