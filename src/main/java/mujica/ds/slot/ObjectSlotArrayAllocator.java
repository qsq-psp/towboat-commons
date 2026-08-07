package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/29")
public class ObjectSlotArrayAllocator implements SlotArrayAllocator<PlainObjectSlot, Object[]> {

    public ObjectSlotArrayAllocator() {
        super();
    }

    @NotNull
    @Override
    public PlainObjectSlot newSlot() {
        return new PlainObjectSlot();
    }

    @Override
    public void move(@NotNull PlainObjectSlot src, @NotNull PlainObjectSlot dst) {
        dst.setObject(src.getObject());
    }

    @NotNull
    @Override
    public Object[] newArray(int length) {
        return new Object[length];
    }

    @Override
    public void load(@NotNull Object[] array, int index, @NotNull PlainObjectSlot slot) {
        slot.setObject(array[index]);
    }

    @Override
    public void store(@NotNull Object[] array, int index, @NotNull PlainObjectSlot slot) {
        array[index] = slot.getObject();
    }
}
