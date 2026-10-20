package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/8/14")
public class VoidSlotArrayAllocator implements SlotArrayAllocator<VoidSlot, VoidArray> {

    private static final long serialVersionUID = 0x8cf50620d723cab4L;

    public static final VoidSlotArrayAllocator INSTANCE = new VoidSlotArrayAllocator();

    @NotNull
    private Object readResolve() {
        return INSTANCE;
    }

    @NotNull
    @Override
    public VoidSlot newSlot() {
        return VoidSlot.INSTANCE;
    }

    @Override
    public void move(@NotNull VoidSlot src, @NotNull VoidSlot dst) {
        // pass
    }

    @NotNull
    @Override
    public VoidSlot cloneSlot(@NotNull VoidSlot original) {
        return VoidSlot.INSTANCE;
    }

    @Override
    public void exchange(@NotNull VoidSlot first, @NotNull VoidSlot second) {
        // pass
    }

    @NotNull
    @Override
    public VoidArray newArray(int length) {
        if (length == 0) {
            return VoidArray.EMPTY_ARRAY;
        }
        return new VoidArray(length);
    }

    @Override
    public int length(@NotNull VoidArray array) {
        return array.length;
    }

    @Override
    public void load(@NotNull VoidArray array, int index, @NotNull VoidSlot slot) {
        // pass
    }

    @Override
    public void store(@NotNull VoidArray array, int index, @NotNull VoidSlot slot) {
        // pass
    }

    @Override
    public void exchange(@NotNull VoidArray array, int index, @NotNull VoidSlot slot) {
        // pass
    }

    @Override
    public void exchange(@NotNull VoidArray array, int firstIndex, int secondIndex) {
        // pass
    }

    @Override
    public void reverse(@NotNull VoidArray array, int startIndex, int endIndex) {
        // pass
    }

    @Override
    public void reverse(@NotNull VoidArray array) {
        // pass
    }

    @Override
    public void copyWithin(@NotNull VoidArray array, int srcPosition, int dstPosition, int length) {
        // pass
    }

    @Override
    public void copy(@NotNull VoidArray src, int srcPosition, @NotNull VoidArray dst, int dstPosition, int length) {
        // pass
    }

    @Override
    public VoidArray cloneArray(@NotNull VoidArray oldArray) {
        return oldArray;
    }
}
