package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/7/4", name = "ClosedHashSet")
@CodeHistory(date = "2026/7/23")
public abstract class ClosedHashSlotSet<S, A> extends HashSlotSet<S, A> {

    @NotNull
    A array;

    protected ClosedHashSlotSet(@NotNull SlotArrayAllocator.WithHash<S, A> hashFunction) {
        super(hashFunction);
        array = hashFunction.newArray(hashFunction.getCapacityPolicy().initialCapacity());
    }
}
