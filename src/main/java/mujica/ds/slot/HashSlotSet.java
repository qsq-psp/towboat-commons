package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/6/7", name = "AbstractHashSet")
@CodeHistory(date = "2026/7/21")
public abstract class HashSlotSet<S, A> extends GapIteratorBasedSlotCollection<S, A> {

    @NotNull
    final SlotArrayAllocator.WithHash<S, A> hashFunction;

    int size;

    @Name(value = "modCount", language = "en")
    int version;

    protected HashSlotSet(@NotNull SlotArrayAllocator.WithHash<S, A> hashFunction) {
        super();
        this.hashFunction = hashFunction;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
