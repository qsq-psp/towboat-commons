package mujica.ds.i32;

import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.ds.slot.CopyOnResizeSlotList;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/8/3")
public class CopyOnResizeI32SlotList extends CopyOnResizeSlotList<I32Slot, int[]> {

    private static final long serialVersionUID = 0x899031B6918D6413L;

    public static final SlotArrayAllocator.PolicySAA<I32Slot, int[]> ALLOCATOR = new SlotArrayAllocatorI32.WithHashI32(TwiceCapacityPolicy.INSTANCE);

    public CopyOnResizeI32SlotList() {
        super(ALLOCATOR);
    }
}
